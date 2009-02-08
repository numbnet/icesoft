/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

var send = operator();
var onSend = operator();
var onReceive = operator();
var onServerError = operator();
var whenDown = operator();
var whenTrouble = operator();
var shutdown = operator();

[ Ice.Community.Connection = new Object, Ice.Connection, Ice.Ajax, Ice.Reliability.Heartbeat, Ice.Cookie, Ice.Parameter.Query ].as(function(This, Connection, Ajax, Heartbeat, Cookie, Query) {
    This.AsyncConnection = function(logger, sessionID, viewID, configuration, commandDispatcher) {
        var logger = logger.child('async-connection');
        var sendChannel = new Ajax.Client(logger.child('ui'));
        var receiveChannel = new Ajax.Client(logger.child('blocking'));
        var defaultQuery = Ice.Parameter.Query.create(function(q) {
            q.add('ice.view', viewID);
            q.add('ice.session', sessionID);
        });
        var onSendListeners = [];
        var onReceiveFromSendListeners = [];
        var onReceiveListeners = [];
        var onServerErrorListeners = [];
        var connectionDownListeners = [];
        var connectionTroubleListeners = [];

        var listener = { close: Function.NOOP, abort: Function.NOOP };
        var listening = { remove: Function.NOOP };
        var timeoutBomb = { cancel: Function.NOOP };
        var heartbeat = { stop: Function.NOOP };

        var pingURI = configuration.context.current + 'block/ping';
        var getURI = configuration.context.current + 'block/receive-updates';
        var sendURI = configuration.context.current + 'block/send-receive-updates';
        var receiveURI = configuration.context.async + 'block/receive-updated-views';

        //clear connectionDownListeners to avoid bogus connection lost messages
        onBeforeUnload(window, function() {
            connectionDownListeners = [];
        });

        var timeout = configuration.timeout ? configuration.timeout : 60000;

        var serverErrorCallback = onServerErrorListeners.broadcaster();
        var receiveCallback = function(response) {
            try {
                onReceiveListeners.broadcast(response);
            } catch (e) {
                logger.error('receive broadcast failed', e);
            }
        };
        var sendXWindowCookie = noop;
        var receiveXWindowCookie = function (response) {
            var xWindowCookie = response.getResponseHeader("X-Set-Window-Cookie");
            if (xWindowCookie) {
                sendXWindowCookie = function(request) {
                    request.setRequestHeader("X-Window-Cookie", xWindowCookie);
                };
            }
        };

        //read/create cookie that contains the updated views
        var updatedViews;
        try {
            updatedViews = Cookie.lookup('updates');
        } catch (e) {
            updatedViews = new Cookie('updates', '');
        }

        //register command that handles the updated-views message
        register(commandDispatcher, 'updated-views', function(message) {
            var views = updatedViews.loadValue().split(' ');
            var text = message.firstChild;
            if (text && !text.data.blank()) {
                updatedViews.saveValue(views.concat(text.data.split(' ')).asSet().join(' '));
            } else {
                logger.warn("No updated views were returned.");
            }
        });


        //remove the blocking connection marker so that everytime a new
        //bridge instance is created the blocking connection will
        //be re-established
        //this strategy is mainly employed to fix the window.onunload issue
        //in Opera -- see http://jira.icefaces.org/browse/ICE-1872
        try {
            listening = Cookie.lookup('bconn');
            listening.remove();
        } catch (e) {
            //do nothing
        }

        //build up retry actions
        var timedRetryAbort = function (retryAction, abortAction, timeouts) {
            var index = 0;
            var errorCallbacks = timeouts.inject([abortAction], function(callbacks, interval) {
                callbacks.unshift(retryAction.delayFor(interval));
                return callbacks;
            });
            return function() {
                if (index < errorCallbacks.length) {
                    errorCallbacks[index].apply(this, arguments);
                    index++;
                }
            };
        };

        var connect = function() {
            logger.debug("closing previous connection...");
            listener.close();
            logger.debug("connect...");
            var query = new Query();
            window.sessions.each(function(sessionID) {
                query.add('ice.session', sessionID);
            });
            listener = receiveChannel.postAsynchronously(receiveURI, query.asURIEncodedString(), function(request) {
                sendXWindowCookie(request);
                Connection.FormPost(request);
                request.on(Connection.ServerError, retryOnServerError);
                request.on(Connection.OK, receiveXWindowCookie);
                request.on(Connection.OK, function(response) {
                    if (!response.isEmpty()) {
                        receiveCallback(response);
                    }
                    if (response.getResponseHeader('X-Connection') != 'close') {
                        connect();
                    }
                });
                request.on(Connection.OK, Connection.Close);
            });
        };

        //build callbacks only after this.connetion function was defined
        var retryOnServerError = timedRetryAbort(connect, serverErrorCallback, configuration.serverErrorRetryTimeouts || [1000, 2000, 4000]);

        //avoid error messages for 'pong' messages that arrive after blocking connection is closed
        register(commandDispatcher, 'pong', Function.NOOP);
        //heartbeat setup
        var heartbeatInterval = configuration.heartbeat.interval ? configuration.heartbeat.interval : 50000;
        var heartbeatTimeout = configuration.heartbeat.timeout ? configuration.heartbeat.timeout : 30000;
        var heartbeatRetries = configuration.heartbeat.retries ? configuration.heartbeat.retries : 3;
        var initializeConnection = function() {
            //stop the previous heartbeat instance
            heartbeat.stop();
            heartbeat = new Heartbeat(heartbeatInterval, heartbeatTimeout, logger);
            heartbeat.onPing(function(ping) {
                //re-register a pong command on every ping
                register(commandDispatcher, 'pong', function() {
                    ping.pong();
                });
                sendChannel.postAsynchronously(pingURI, defaultQuery.asURIEncodedString(), Connection.FormPost);
            });

            heartbeat.onLostPongs(connectionDownListeners.broadcaster(), heartbeatRetries);
            heartbeat.onLostPongs(connectionTroubleListeners.broadcaster());
            heartbeat.onLostPongs(function() {
                logger.debug('retry to connect...');
                connect();
            });

            heartbeat.start();
            connect();
        };

        //monitor if the blocking connection needs to be started
        var pollingPeriod = 1000;
        var fullViewID = sessionID + ':' + viewID;
        var leaseCookie = Cookie.lookup('ice.lease', (new Date).getTime().toString());
        var connectionCookie = listening = Cookie.lookup('bconn', '-');
        function updateLease() {
            leaseCookie.saveValue((new Date).getTime() + pollingPeriod * 2);
        }
        function isLeaseExpired() {
            return leaseCookie.loadValue().asNumber() < (new Date).getTime();
        }
        function shouldEstablishBlockingConnection() {
            return !Cookie.exists('bconn') || !Cookie.lookup('bconn').value.startsWith(sessionID);
        }
        function offerCandidature() {
            connectionCookie.saveValue(fullViewID);
        }
        function isWinningCandidate() {
            return connectionCookie.loadValue().startsWith(fullViewID);
        }
        function markAsOwned() {
            connectionCookie.saveValue(fullViewID + ':acquired');
        }
        function hasOwner() {
            return connectionCookie.loadValue().endsWith(':acquired');
        }
        var blockingConnectionMonitor = function() {
            if (shouldEstablishBlockingConnection()) {
                offerCandidature();
                logger.info('blocking connection not initialized...candidate for its creation');
            } else {
                if (isWinningCandidate()) {
                    if (!hasOwner()) {
                        markAsOwned();
                        //start blocking connection since no other view has started it
                        initializeConnection();
                    }
                    updateLease();
                }
                if (hasOwner() && isLeaseExpired()) {
                    offerCandidature();
                    logger.info('blocking connection lease expired...candidate for its creation');
                }
            }
        }.repeatExecutionEvery(pollingPeriod);

        var pickUpdates = function() {
            sendChannel.postAsynchronously(getURI, defaultQuery.asURIEncodedString(), function(request) {
                Connection.FormPost(request);
                request.on(Connection.OK, receiveCallback);
                request.on(Connection.OK, Connection.Close);
            });
        };

        //pick any updates that might be generated in between bridge re-initialization
        //todo: replace heuristic with more exact solution
        pickUpdates.delayExecutionFor(pollingPeriod);

        //monitor & pick updates for this view
        var updatesMonitor = function() {
            try {
                var views = updatedViews.loadValue().split(' ');
                if (views.include(fullViewID)) {
                    pickUpdates();
                    updatedViews.saveValue(views.complement([ fullViewID ]).join(' '));
                }
            } catch (e) {
                logger.warn('failed to listen for updates', e);
            }
        }.repeatExecutionEvery(300);

        logger.info('asynchronous mode');

        return object(function(method) {
            method(send, function(self, query) {
                var compoundQuery = new Query();
                compoundQuery.addQuery(query);
                compoundQuery.addQuery(defaultQuery);
                compoundQuery.add('ice.focus', window.currentFocus);

                logger.debug('send > ' + compoundQuery.asString());
                sendChannel.postAsynchronously(sendURI, compoundQuery.asURIEncodedString(), function(request) {
                    Connection.FormPost(request);

                    timeoutBomb.cancel();
                    timeoutBomb = connectionDownListeners.broadcaster().delayExecutionFor(timeout);
                    request.on(Connection.OK, function() {
                        timeoutBomb.cancel();
                    });

                    request.on(Connection.OK, receiveCallback);
                    request.on(Connection.OK, onReceiveFromSendListeners.broadcaster());
                    request.on(Connection.ServerError, serverErrorCallback);
                    request.on(Connection.OK, Connection.Close);
                    onSendListeners.broadcast();
                });
            });

            method(onSend, function(self, sendCallback, receiveCallback) {
                onSendListeners.push(sendCallback);
                if (receiveCallback) onReceiveFromSendListeners.push(receiveCallback);
            });

            method(onReceive, function(self, callback) {
                onReceiveListeners.push(callback);
            });

            method(onReceive, function(self, callback) {
                onReceiveListeners.push(callback);
            });

            method(onServerError, function(self, callback) {
                onServerErrorListeners.push(callback);
            });

            method(whenDown, function(self, callback) {
                connectionDownListeners.push(callback);
            });

            method(whenTrouble, function(self, callback) {
                connectionTroubleListeners.push(callback);
            });

            method(shutdown, function(self) {
                try {
                    //shutdown once
                    method(shutdown, noop);
                    //avoid sending XMLHTTP requests that might create new sessions on the server
                    method(send, noop);
                    connect = noop;
                    heartbeat.stop();
                } catch (e) {
                    //ignore, we really need to shutdown
                } finally {
                    [ onSendListeners, onReceiveListeners, connectionDownListeners, onServerErrorListeners, onReceiveFromSendListeners ].eachWithGuard(function(listeners) {
                        listeners.clear();
                    });
                    listener.abort();

                    [ updatesMonitor, blockingConnectionMonitor ].eachWithGuard(function(monitor) {
                        monitor.cancel();
                    });
                    listening.remove();
                }
            });
        });
    };
});

