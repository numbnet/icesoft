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
var resetConnection = operator();

function AsyncConnection(logger, windowID, configuration) {
    var logger = childLogger(logger, 'async-connection');
    var channel = Client(false);
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
    var receiveURI = 'listen.icepush' + namespace.uriextension;

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

    //remove the blocking connection marker so that everytime a new
    //bridge instance is created the blocking connection will
    //be re-established
    //this strategy is mainly employed to fix the window.onunload issue
    //in Opera -- see http://jira.icefaces.org/browse/ICE-1872
    try {
        listening = lookupCookie('ice.connection.running');
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

    function registeredPushIds() {
        try {
            return split(lookupCookieValue('ice.pushids'), ' ');
        } catch (e) {
            return [];
        }
    }

    function connect() {
        try {
            debug(logger, "closing previous connection...");
            close(listener);
            debug(logger, "connect...");

            var ids = registeredPushIds();
            if (isEmpty(ids)) {
                //mark blocking connection as not started, with current window as first candidate to re-initiate the connection 
                offerCandidature();
            } else {
                listener = postAsynchronously(channel, receiveURI, function(q) {
                    each(ids, curry(addNameValue, q, 'ice.pushid'));
                }, function(request) {
                    FormPost(request);
                    sendXWindowCookie(request);
                }, $witch(function (condition) {
                    condition(OK, function(response) {
                        var reconnect = getHeader(response, 'X-Connection') != 'close';
                        var nonEmptyResponse = notEmpty(contentAsText(response));

                        if (reconnect) {
                            if (not(nonEmptyResponse)) warn(logger, 'empty response received');
                        } else {
                            info(logger, 'blocking connection stopped at server\'s request...');
                        }
                        if (nonEmptyResponse) receiveCallback(response);
                        receiveXWindowCookie(response);
                        if (reconnect) connect();
                    });
                    condition(ServerInternalError, retryOnServerError);
                }));
            }
        } catch (e) {
            error(logger, 'failed to re-initiate blocking connection', e);
        }
    }

    //build callbacks only after this.connection function was defined
    var retryOnServerError = timedRetryAbort(connect, serverErrorCallback, configuration.serverErrorRetryTimeouts || [1000, 2000, 4000]);

    //todo: implement client based timout alert for server side hearbeat to help detect when server does not respond anymore
    var heartbeatTimeout = configuration.heartbeat && configuration.heartbeat.timeout ? configuration.heartbeat.timeout : 30000;

    function initializeConnection() {
        connect();
    }

    //monitor if the blocking connection needs to be started
    var pollingPeriod = 1000;
    var leaseCookie = lookupCookie('ice.connection.lease', function() {
        return Cookie('ice.connection.lease', asString((new Date).getTime()));
    });
    var connectionCookie = listening = lookupCookie('ice.connection.running', function() {
        return Cookie('ice.connection.running', '');
    });

    function updateLease() {
        update(leaseCookie, (new Date).getTime() + pollingPeriod * 2);
    }

    function isLeaseExpired() {
        return asNumber(value(leaseCookie)) < (new Date).getTime();
    }

    function shouldEstablishBlockingConnection() {
        return !existsCookie('ice.connection.running') || isEmpty(lookupCookieValue('ice.connection.running'));
    }

    function offerCandidature() {
        update(connectionCookie, windowID);
    }

    function isWinningCandidate() {
        return startsWith(value(connectionCookie), windowID);
    }

    function markAsOwned() {
        update(connectionCookie, windowID + ':acquired');
    }

    function isOwner() {
        return value(connectionCookie) == (windowID + ':acquired');
    }

    function hasOwner() {
        return endsWith(value(connectionCookie), ':acquired');
    }

    var lastRegisteredPushIds = registeredPushIds();
    var blockingConnectionMonitor = run(Delay(function() {
        if (shouldEstablishBlockingConnection()) {
            offerCandidature();
            info(logger, 'blocking connection not initialized...candidate for its creation');
        } else {
            if (isWinningCandidate()) {
                if (!hasOwner()) {
                    markAsOwned();
                    //start blocking connection since no other window has started it
                    //but only when at least one pushId is registered
                    if (notEmpty(registeredPushIds())) {
                        initializeConnection();
                    }
                }
                updateLease();
            }
            if (hasOwner() && isLeaseExpired()) {
                offerCandidature();
                info(logger, 'blocking connection lease expired...candidate for its creation');
            }
        }

        if (isOwner()) {
            var currentlyRegisterdPushIds = registeredPushIds();
            if ((size(currentlyRegisterdPushIds) != size(lastRegisteredPushIds)) ||
                notEmpty(complement(currentlyRegisterdPushIds, lastRegisteredPushIds))) {
                //reconnect to send the current list of pushIDs
                connect();
            }
            lastRegisteredPushIds = currentlyRegisterdPushIds;
        }
    }, pollingPeriod));

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
            } catch (e) {
                error(logger, 'error during shutdown', e);
                //ignore, we really need to shutdown
            } finally {
                onReceiveListeners = connectionDownListeners = onServerErrorListeners = [];
                abort(listener);
                stop(blockingConnectionMonitor);
                remove(listening);
            }
        });
    });
}
