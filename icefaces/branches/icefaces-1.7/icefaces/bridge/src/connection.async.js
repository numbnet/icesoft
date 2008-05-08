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

[ Ice.Community.Connection = new Object, Ice.Connection, Ice.Ajax, Ice.Reliability.Heartbeat, Ice.Cookie, Ice.Parameter.Query ].as(function(This, Connection, Ajax, Heartbeat, Cookie, Query) {
    This.AsyncConnection = Object.subclass({
        initialize: function(logger, sessionID, viewID, configuration, defaultQuery, commandDispatcher) {
            this.logger = logger.child('async-connection');
            this.sendChannel = new Ajax.Client(this.logger.child('ui'));
            this.receiveChannel = new Ajax.Client(this.logger.child('blocking'));
            this.defaultQuery = defaultQuery;
            this.onSendListeners = [];
            this.onReceiveListeners = [];
            this.onServerErrorListeners = [];
            this.connectionDownListeners = [];
            this.connectionTroubleListeners = [];

            this.listener = { close: Function.NOOP, abort: Function.NOOP };
            this.listening = { remove: Function.NOOP };
            this.timeoutBomb = { cancel: Function.NOOP };
            this.heartbeat = { stop: Function.NOOP };

            this.pingURI = configuration.context.current + 'block/ping';
            this.getURI = configuration.context.current + 'block/receive-updates';
            this.sendURI = configuration.context.current + 'block/send-receive-updates';
            this.receiveURI = configuration.context.async + 'block/receive-updated-views';
            this.disposeViewsURI = configuration.context.current + 'block/dispose-views';

            var timeout = configuration.timeout ? configuration.timeout : 60000;
            this.onSend(function() {
                this.timeoutBomb.cancel();
                this.timeoutBomb = this.connectionDownListeners.broadcaster().delayExecutionFor(timeout);
            }.bind(this));

            this.onReceive(function() {
                this.timeoutBomb.cancel();
            }.bind(this));

            this.badResponseCallback = this.connectionDownListeners.broadcaster();
            this.serverErrorCallback = this.onServerErrorListeners.broadcaster();
            this.receiveCallback = function(response) {
                try {
                    this.onReceiveListeners.broadcast(response);
                } catch (e) {
                    this.logger.error('receive broadcast failed', e);
                }
            }.bind(this);
            this.sendXWindowCookie = Function.NOOP;
            this.receiveXWindowCookie = function (response) {
                var xWindowCookie = response.getResponseHeader("X-Set-Window-Cookie");
                if (xWindowCookie) {
                    this.sendXWindowCookie = function(request) {
                        request.setRequestHeader("X-Window-Cookie", xWindowCookie);
                    };
                }
            }.bind(this);

            //read/create cookie that contains the updated views
            try {
                this.updatedViews = Cookie.lookup('updates');
            } catch (e) {
                this.updatedViews = new Cookie('updates', '');
            }

            //register command that handles the updated-views message
            commandDispatcher.register('updated-views', function(message) {
                var views = this.updatedViews.loadValue().split(' ');
                var text = message.firstChild;
                if (text && !text.data.blank()) {
                    this.updatedViews.saveValue(views.concat(text.data.split(' ')).asSet().join(' '));
                } else {
                    this.logger.warn("No updated views were returned.");
                }
            }.bind(this));


            //remove the blocking connection marker so that everytime a new
            //bridge instance is created the blocking connection will
            //be re-established
            //this strategy is mainly employed to fix the window.onunload issue
            //in Opera -- see http://jira.icefaces.org/browse/ICE-1872
            try {
                this.listening = Cookie.lookup('bconn');
                this.listening.remove();
            } catch (e) {
                //do nothing
            }

            this.connect = function() {
                this.logger.debug("closing previous connection...");
                this.listener.close();
                this.logger.debug("connect...");
                var query = new Query();
                window.sessions.each(function(sessionID) {
                    query.add('ice.session', sessionID);
                });
                this.listener = this.receiveChannel.postAsynchronously(this.receiveURI, query.asURIEncodedString(), function(request) {
                    this.sendXWindowCookie(request);
                    Connection.FormPost(request);
                    request.on(Connection.BadResponse, this.badResponseCallback);
                    request.on(Connection.ServerError, this.serverErrorCallback);
                    request.on(Connection.OK, this.receiveXWindowCookie);
                    request.on(Connection.OK, function(response) {
                        if (!response.isEmpty()) {
                            this.receiveCallback(response);
                        }
                        if (response.getResponseHeader('X-Connection') != 'close') {
                            this.connect();
                        }
                    }.bind(this));
                    request.on(Connection.OK, Connection.Close);
                }.bind(this));
            }.bind(this);

            //avoid error messages for 'pong' messages that arrive after blocking connection is closed
            commandDispatcher.register('pong', Function.NOOP);
            //heartbeat setup
            var heartbeatInterval = configuration.heartbeat.interval ? configuration.heartbeat.interval : 50000;
            var heartbeatTimeout = configuration.heartbeat.timeout ? configuration.heartbeat.timeout : 30000;
            var heartbeatRetries = configuration.heartbeat.retries ? configuration.heartbeat.retries : 3;
            var initializeConnection = function() {
                //stop the previous heartbeat instance
                this.heartbeat.stop();
                this.heartbeat = new Heartbeat(heartbeatInterval, heartbeatTimeout, this.logger);
                this.heartbeat.onPing(function(ping) {
                    //re-register a pong command on every ping
                    commandDispatcher.register('pong', function() {
                        ping.pong();
                    });
                    this.sendChannel.postAsynchronously(this.pingURI, this.defaultQuery.asURIEncodedString(), Connection.FormPost);
                }.bind(this));

                this.heartbeat.onLostPongs(this.connectionDownListeners.broadcaster(), heartbeatRetries);
                this.heartbeat.onLostPongs(this.connectionTroubleListeners.broadcaster());
                this.heartbeat.onLostPongs(function() {
                    this.logger.debug('retry to connect...');
                    this.connect();
                }.bind(this));

                this.heartbeat.start();
                this.connect();
            }.bind(this);

            //monitor if the blocking connection needs to be started
            //
            //the blocking connection will be started by the window noticing
            //that the connection is not started
            var fullViewID = sessionID + ':' + viewID;
            this.blockingConnectionMonitor = function() {
                try {
                    this.listening = Cookie.lookup('bconn');
                    if (this.listening.value == fullViewID) {
                        this.listening.saveValue('acquired');
                        //start blocking connection since no other window has started it
                        initializeConnection();
                    }
                } catch (e) {
                    this.listening = new Cookie('bconn', fullViewID);
                }
            }.bind(this).repeatExecutionEvery(1000);

            //get the updates for the view
            this.updatesMonitor = function() {
                try {
                    var views = this.updatedViews.loadValue().split(' ');
                    if (views.include(fullViewID)) {
                        this.sendChannel.postAsynchronously(this.getURI, this.defaultQuery.asURIEncodedString(), function(request) {
                            Connection.FormPost(request);
                            request.on(Connection.OK, this.receiveCallback);
                            request.on(Connection.OK, Connection.Close);
                        }.bind(this));
                        this.updatedViews.saveValue(views.complement([ fullViewID ]).join(' '));
                    }
                } catch (e) {
                    this.logger.warn('failed to listen for updates', e);
                }
            }.bind(this).repeatExecutionEvery(300);

            this.logger.info('asynchronous mode');
        },

        send: function(query) {
            var compoundQuery = new Query();
            compoundQuery.addQuery(query);
            compoundQuery.addQuery(this.defaultQuery);
            compoundQuery.add('ice.focus', window.currentFocus);

            this.logger.debug('send > ' + compoundQuery.asString());
            this.sendChannel.postAsynchronously(this.sendURI, compoundQuery.asURIEncodedString(), function(request) {
                Connection.FormPost(request);
                request.on(Connection.OK, this.receiveCallback);
                request.on(Connection.ServerError, this.serverErrorCallback);
                request.on(Connection.OK, Connection.Close);
                this.onSendListeners.broadcast(request);
            }.bind(this));
        },

        onSend: function(callback) {
            this.onSendListeners.push(callback);
        },

        onReceive: function(callback) {
            this.onReceiveListeners.push(callback);
        },

        onServerError: function(callback) {
            this.onServerErrorListeners.push(callback);
        },

        whenDown: function(callback) {
            this.connectionDownListeners.push(callback);
        },

        whenTrouble: function(callback) {
            this.connectionTroubleListeners.push(callback);
        },

        cancelDisposeViews: function() {
            this.sendDisposeViews = Function.NOOP;
        },

        sendDisposeViews: function() {
            try {
                this.sendChannel.postSynchronously(this.disposeViewsURI, this.defaultQuery.asURIEncodedString(), function(request) {
                    Connection.FormPost(request);
                    request.on(Connection.OK, Connection.Close);
                });
            } catch (e) {
                this.logger.warn('Failed to notify view disposal', e);
            } finally {
                this.cancelDisposeViews();
            }
        },

        shutdown: function() {
            try {
                //shutdown once
                this.shutdown = Function.NOOP;
                //avoid sending XMLHTTP requests that might create new sessions on the server
                this.send = Function.NOOP;
                this.connect = Function.NOOP;
                this.heartbeat.stop();
            } catch (e) {
                //ignore, we really need to shutdown
            } finally {
                [ this.onSendListeners, this.onReceiveListeners, this.connectionDownListeners, this.onServerErrorListeners ].eachWithGuard(function(listeners) {
                    listeners.clear();
                });
                this.listener.abort();

                [ this.updatesMonitor, this.blockingConnectionMonitor ].eachWithGuard(function(monitor) {
                    monitor.cancel();
                });
                this.listening.remove();
            }
        }
    });
});

