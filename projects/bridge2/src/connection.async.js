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

function SyncConnection(logger, sessionID, viewID, configuration) {
    var logger = childLogger(logger, 'sync-connection');
    var channel = Client(true);
    var defaultQuery = Query();
    addNameValue(defaultQuery, 'ice.view', viewID);
    addNameValue(defaultQuery, 'ice.session', sessionID);
    var onSendListeners = [];
    var onReceiveListeners = [];
    var onServerErrorListeners = [];
    var connectionDownListeners = [];
    var sendURI = configuration.context.current + 'block/send-receive-updates';
    var timeout = configuration.timeout ? configuration.timeout : 60000;
    var serverErrorCallback = broadcaster(onServerErrorListeners);
    var timeoutBomb = object(function(method) {
        method(stop, noop);
    });

    return object(function(method) {
        method(send, function(self, query) {
            stop(timeoutBomb);
            timeoutBomb = runOnce(Delay(broadcaster(connectionDownListeners), timeout));
            broadcast(onSendListeners);
            debug(logger, 'send > ' + sendURI);
            postAsynchronously(channel, sendURI, function(q) {
                addQuery(q, query);
                addQuery(q, defaultQuery);
                addNameValue(q, 'ice.focus', currentFocus);

                debug(logger, '\n' + asString(q));
            }, FormPost, $witch(function(condition) {
                condition(OK, function(response, request) {
                    stop(timeoutBomb);
                    broadcast(onReceiveListeners, arguments);
                });

                condition(ServerInternalError, serverErrorCallback);
            }));
        });

        method(onSend, function(self, sendCallback, receiveCallback) {
            append(onSendListeners, sendCallback);
            if (receiveCallback)
                append(onReceiveListeners, receiveCallback);
        });

        method(onReceive, function(self, receiveCallback) {
            append(onReceiveListeners, receiveCallback);
        });

        method(onServerError, function(self, callback) {
            append(onServerErrorListeners, callback);
        });

        method(whenDown, function(self, callback) {
            append(connectionDownListeners, callback);
        });

        method(shutdown, function(self) {
            //shutdown once
            method(shutdown, noop);
            //avoid sending XMLHTTP requests that might create new sessions on the server
            method(send, noop);
            each([onSendListeners, onReceiveListeners, connectionDownListeners, onServerErrorListeners], empty);
        });
    });
}

function AsyncConnection(logger, sessionID, viewID, configuration, commandDispatcher) {
    var logger = childLogger(logger, 'async-connection');
    var channel = Client(false);
    var defaultQuery = Query();
    addNameValue(defaultQuery, 'ice.view', viewID);
    addNameValue(defaultQuery, 'ice.session', sessionID);
    var onReceiveListeners = [];
    var onServerErrorListeners = [];
    var connectionDownListeners = [];
    var connectionTroubleListeners = [];

    var listener = object(function(method) {
        method(close, noop);
        method(abort, noop);
    });
    var listening = object(function(method) {
        method(remove, noop);
    });
    var heartbeat = object(function(method) {
        method(stopBeat, noop);
    });

    var pingURI = configuration.context.current + 'block/ping';
    var getURI = configuration.context.current + 'block/receive-updates';
    var receiveURI = configuration.context.async + 'block/receive-updated-views';

    //clear connectionDownListeners to avoid bogus connection lost messages
    onBeforeUnload(window, function() {
        connectionDownListeners = [];
    });

    var serverErrorCallback = broadcaster(onServerErrorListeners);
    var receiveCallback = broadcaster(onReceiveListeners);
    var sendXWindowCookie = noop;
    var receiveXWindowCookie = function (response) {
        var xWindowCookie = getHeader(response, "X-Set-Window-Cookie");
        if (xWindowCookie) {
            sendXWindowCookie = function(request) {
                setHeader(request, "X-Window-Cookie", xWindowCookie);
            };
        }
    };

    //read/create cookie that contains the updated views
    var updatedViews = lookupCookie('updates', function() {
        return Cookie('updates', '');
    });

    //register command that handles the updated-views message
    register(commandDispatcher, 'updated-views', function(message) {
        info(logger, "Views update: " + value(updatedViews));
        var views = split(value(updatedViews), ' ');
        var text = message.firstChild;
        if (text && !blank(text.data)) {
            update(updatedViews, join(asSet(concatenate(views, split(text.data, ' '))), ' '));
        } else {
            warn(logger, "No updated views were returned.");
        }
    });


    //remove the blocking connection marker so that everytime a new
    //bridge instance is created the blocking connection will
    //be re-established
    //this strategy is mainly employed to fix the window.onunload issue
    //in Opera -- see http://jira.icefaces.org/browse/ICE-1872
    try {
        listening = lookupCookie('bconn');
        remove(listening);
    } catch (e) {
        //do nothing
    }

    //build up retry actions
    function timedRetryAbort(retryAction, abortAction, timeouts) {
        var index = 0;
        var errorActions = inject(timeouts, [abortAction], function(actions, interval) {
            return insert(actions, curry(runOnce, Delay(retryAction, interval)));
        });
        return function() {
            if (index < errorActions.length) {
                apply(errorActions[index], arguments);
                index++;
            }
        };
    }

    function connect() {
        debug(logger, "closing previous connection...");
        close(listener);
        debug(logger, "connect...");
        listener = postAsynchronously(channel, receiveURI, function(q) {
            each(window.sessions, curry(addNameValue, q, 'ice.session'));
        }, function(request) {
            FormPost(request);
            sendXWindowCookie(request);
        }, $witch(function (condition) {
            condition(OK, function(response) {
                if (notEmpty(contentAsText(response))) {
                    receiveCallback(response);
                }
                receiveXWindowCookie(response);
                if (getHeader(response, 'X-Connection') != 'close') {
                    connect();
                }
            });
            condition(ServerInternalError, retryOnServerError);
        }));
    }

    //build callbacks only after this.connection function was defined
    var retryOnServerError = timedRetryAbort(connect, serverErrorCallback, configuration.serverErrorRetryTimeouts || [1000, 2000, 4000]);

    //avoid error messages for 'pong' messages that arrive after blocking connection is closed
    register(commandDispatcher, 'pong', noop);
    //heartbeat setup
    var heartbeatInterval = configuration.heartbeat.interval ? configuration.heartbeat.interval : 50000;
    var heartbeatTimeout = configuration.heartbeat.timeout ? configuration.heartbeat.timeout : 30000;
    var heartbeatRetries = configuration.heartbeat.retries ? configuration.heartbeat.retries : 3;

    function initializeConnection() {
        //stop the previous heartbeat instance
        stopBeat(heartbeat);
        heartbeat = Heartbeat(heartbeatInterval, heartbeatTimeout, logger);
        onPing(heartbeat, function(pong) {
            //re-register a pong command on every ping
            register(commandDispatcher, 'pong', pong);
            postAsynchronously(channel, pingURI, function(q) {
                addQuery(q, defaultQuery);
            }, FormPost, noop);
        });

        onLostPongs(heartbeat, broadcaster(connectionDownListeners), heartbeatRetries);
        onLostPongs(heartbeat, broadcaster(connectionTroubleListeners));
        onLostPongs(heartbeat, connect);

        startBeat(heartbeat);
        //wire up keyboard shortcut to toggle heartbeat
        var heartbeatStarted = true;
        onKeyPress(document, function(evt) {
            var e = $event(evt, document.documentElement);
            if (keyCode(e) == 46 && isCtrlPressed(e) && isShiftPressed(e)) {
                heartbeatStarted ? stopBeat(heartbeat) : startBeat(heartbeat);
                heartbeatStarted = !heartbeatStarted;
            }
        });

        connect();
    }

    //monitor if the blocking connection needs to be started
    var pollingPeriod = 1000;
    var fullViewID = sessionID + ':' + viewID;
    var leaseCookie = lookupCookie('ice.lease', function() {
        return Cookie('ice.lease', asString((new Date).getTime()));
    });
    var connectionCookie = listening = lookupCookie('bconn', function() {
        return Cookie('bconn', '-');
    });

    function updateLease() {
        update(leaseCookie, (new Date).getTime() + pollingPeriod * 2);
    }

    function isLeaseExpired() {
        return asNumber(value(leaseCookie)) < (new Date).getTime();
    }

    function shouldEstablishBlockingConnection() {
        return !existsCookie('bconn') || !startsWith(lookupCookieValue('bconn'), sessionID);
    }

    function offerCandidature() {
        update(connectionCookie, fullViewID);
    }

    function isWinningCandidate() {
        return startsWith(value(connectionCookie), fullViewID);
    }

    function markAsOwned() {
        update(connectionCookie, fullViewID + ':acquired');
    }

    function hasOwner() {
        return endsWith(value(connectionCookie), ':acquired');
    }

    var blockingConnectionMonitor = run(Delay(function() {
        if (shouldEstablishBlockingConnection()) {
            offerCandidature();
            info(logger, 'blocking connection not initialized...candidate for its creation');
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
                info(logger, 'blocking connection lease expired...candidate for its creation');
            }
        }
    }, pollingPeriod));

    function pickUpdates() {
        postAsynchronously(channel, getURI, function(q) {
            addQuery(q, defaultQuery);
        }, FormPost, $witch(function(condition) {
            condition(OK, function(response) {
                if (notEmpty(contentAsText(response))) receiveCallback(response);
            });
        }));
    }

    //pick any updates that might be generated in between bridge re-initialization
    //todo: replace heuristic with more exact solution
    runOnce(Delay(pickUpdates, pollingPeriod));

    //monitor & pick updates for this view
    var updatesMonitor = run(Delay(function() {
        try {
            var views = split(value(updatedViews), ' ');
            if (contains(views, fullViewID)) {
                pickUpdates();
                update(updatedViews, join(complement(views, [ fullViewID ]), ' '));
            }
        } catch (e) {
            warn(logger, 'failed to listen for updates', e);
        }
    }, 300));

    info(logger, 'asynchronous mode');

    return object(function(method) {
        method(onReceive, function(self, callback) {
            append(onReceiveListeners, callback);
        });

        method(onServerError, function(self, callback) {
            append(onServerErrorListeners, callback);
        });

        method(whenDown, function(self, callback) {
            append(connectionDownListeners, callback);
        });

        method(whenTrouble, function(self, callback) {
            append(connectionTroubleListeners, callback);
        });

        method(shutdown, function(self) {
            try {
                //shutdown once
                method(shutdown, noop);
                connect = noop;
                stopBeat(heartbeat);
            } catch (e) {
                //ignore, we really need to shutdown
            } finally {
                each([onReceiveListeners, connectionDownListeners, onServerErrorListeners], empty);
                abort(listener);
                each([updatesMonitor, blockingConnectionMonitor], stop);
                remove(listening);
            }
        });
    });
}

