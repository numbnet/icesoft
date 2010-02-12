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

if (!window.ice) {
    window.ice = new Object;
}

if (!window.ice.icepush) {
    (function(namespace) {
        window.ice.icepush = true;
        //include functional.js
        //include oo.js
        //include collection.js
        //include string.js
        //include window.js
        //include logger.js
        //include cookie.js
        //include delay.js
        //include element.js
        //include event.js
        //include http.js
        //include command.js
        //include connection.async.js

        var notificationListeners = [];
        namespace.onNotification = function(callback) {
            append(notificationListeners, callback);
        };

        var serverErrorListeners = [];
        namespace.onBlockingConnectionServerError = function(callback) {
            append(serverErrorListeners, callback);
        };

        var blockingConnectionUnstableListeners = [];
        namespace.onBlockingConnectionUnstable = function(callback) {
            append(blockingConnectionUnstableListeners, callback);
        };

        var blockingConnectionLostListeners = [];
        namespace.onBlockingConnectionLost = function(callback) {
            append(blockingConnectionLostListeners, callback);
        };

        var handler = window.console && window.console.firebug ? FirebugLogHandler(debug) : WindowLogHandler(debug, window.location.href);
        namespace.windowID = namespace.windowID || substring(Math.random().toString(16), 2, 7);
        namespace.logger = Logger([ 'icepush' ], handler);
        namespace.info = info;
        var pushIdentifiers = [];

        function enlistPushIDsWithBrowser(ids) {
            try {
                var idsCookie = lookupCookie('ice.pushids');
                var registeredIDs = split(value(idsCookie), ' ');
                update(idsCookie, join(concatenate(registeredIDs, ids), ' '));
            } catch (e) {
                Cookie('ice.pushids', join(ids, ' '));
            }
        }

        function delistPushIDsWithBrowser(ids) {
            if (existsCookie('ice.pushids')) {
                var idsCookie = lookupCookie('ice.pushids');
                var registeredIDs = split(value(idsCookie), ' ');
                update(idsCookie, join(complement(registeredIDs, ids), ' '));
            }
        }

        function enlistPushIDsWithWindow(ids) {
            enlistPushIDsWithBrowser(ids);
            pushIdentifiers = concatenate(pushIdentifiers, ids);
        }

        function delistPushIDsWithWindow(ids) {
            delistPushIDsWithBrowser(ids);
            pushIdentifiers = complement(pushIdentifiers, ids);
        }

        onBeforeUnload(window, function() {
            delistPushIDsWithBrowser(pushIdentifiers);
            pushIdentifiers = [];
        });

        function throwServerError(response) {
            throw 'Server internal error: ' + contentAsText(response);
        }

        function calculateURI(uri) {
            return (namespace.push.uriPrefix || '') + uri + (namespace.push.uriSuffix || '');
        }

        var currentNotifications = [];
        var apiChannel = Client(true);
        //public API
        namespace.uriextension = '';
        namespace.push = {
            register: function(pushIds, callback) {
                if ((typeof callback) == 'function') {
                    enlistPushIDsWithWindow(pushIds);
                    namespace.onNotification(function(ids) {
                        currentNotifications = asArray(intersect(ids, pushIds));
                        if (notEmpty(currentNotifications)) {
                            try {
                                callback(currentNotifications);
                            } catch (e) {
                                error(namespace.logger, 'error thrown by push notification callback', e);
                            }
                        }
                    });
                } else {
                    throw 'the callback is not a function';
                }
            },

            deregister: delistPushIDsWithWindow,

            getCurrentNotifications: function() {
                return currentNotifications;
            },

            createPushId: function() {
                var id;
                postSynchronously(apiChannel, calculateURI('create-push-id.icepush'), noop, FormPost, $witch(function (condition) {
                    condition(OK, function(response) {
                        id = contentAsText(response);
                    });
                    condition(ServerInternalError, throwServerError);
                }));
                return id;
            },

            notify: function(ids) {
                postAsynchronously(apiChannel, calculateURI('notify.icepush'), function(q) {
                    each(ids, curry(addNameValue, q, 'id'));
                }, FormPost, $witch(function(condition) {
                    condition(ServerInternalError, throwServerError);
                }));
            },

            addGroupMember: function(group, id) {
                postAsynchronously(apiChannel, calculateURI('add-group-member.icepush'), function(q) {
                    addNameValue(q, 'group', group);
                    addNameValue(q, 'id', id);
                }, FormPost, $witch(function(condition) {
                    condition(ServerInternalError, throwServerError);
                }));
            },

            removeGroupMember: function(group, id) {
                postAsynchronously(apiChannel, calculateURI('remove-group-member.icepush'), function(q) {
                    addNameValue(q, 'group', group);
                    addNameValue(q, 'id', id);
                }, FormPost, $witch(function(condition) {
                    condition(ServerInternalError, throwServerError);
                }));
            },

            get: function(uri, parameters, responseCallback) {
                getAsynchronously(apiChannel, uri, function(query) {
                    parameters(curry(addNameValue, query));
                }, noop, $witch(function(condition) {
                    condition(OK, function(response) {
                        responseCallback(statusCode(response), contentAsText(response), contentAsDOM(response));
                    });
                    condition(ServerInternalError, throwServerError);
                }));
            },

            post: function(uri, parameters, responseCallback) {
                postAsynchronously(apiChannel, uri, function(query) {
                    parameters(curry(addNameValue, query));
                }, FormPost, $witch(function(condition) {
                    condition(OK, function(response) {
                        responseCallback(statusCode(response), contentAsText(response), contentAsDOM(response));
                    });
                    condition(ServerInternalError, throwServerError);
                }));
            },

            configuration: {
                uriSuffix: '',
                uriPrefix: ''
            }
        };

        function Bridge(configuration) {
            var windowID = configuration.window;
            var logger = childLogger(namespace.logger, windowID);
            var commandDispatcher = CommandDispatcher();
            var asyncConnection = AsyncConnection(logger, windowID, calculateURI('listen.icepush'));

            register(commandDispatcher, 'noop', function() {
                debug(logger, 'received noop');
            });
            register(commandDispatcher, 'parsererror', ParsingError);

            //purge discarded pushIDs from the notification list
            function purgeUnusedPushIDs(ids) {
                var registeredIDsCookie = lookupCookie('ice.pushids');
                var registeredIDs = split(value(registeredIDsCookie), ' ');
                return intersect(ids, registeredIDs);
            }

            //todo: factor out cookie & monitor into a communication bus abstraction
            //todo: move notifiedPushIDs out of the bridge to help removing deregistered pushIds from the list of notified pushIds
            //read/create cookie that contains the notified pushID
            var notifiedPushIDs = lookupCookie('ice.notified.pushids', function() {
                return Cookie('ice.notified.pushids', '');
            });

            //register command that handles the notified-pushids message
            register(commandDispatcher, 'notified-pushids', function(message) {
                var text = message.firstChild;
                if (text && !blank(text.data)) {
                    var ids = split(value(notifiedPushIDs), ' ');
                    var receivedPushIDs = split(text.data, ' ');
                    debug(logger, 'received notifications: ' + receivedPushIDs);
                    update(notifiedPushIDs, join(purgeUnusedPushIDs(asSet(concatenate(ids, receivedPushIDs))), ' '));
                } else {
                    warn(logger, "No notification was received.");
                }
            });

            //monitor & pick updates for this window
            var notificationMonitor = run(Delay(function() {
                try {
                    var ids = split(value(notifiedPushIDs), ' ');
                    if (notEmpty(ids)) {
                        broadcast(notificationListeners, [ ids ]);
                        debug(logger, 'picked up notifications for this window: ' + ids);
                        //remove only the pushIDs contained by this page since notificationListeners can only contain listeners from the current page
                        update(notifiedPushIDs, join(purgeUnusedPushIDs(complement(ids, pushIdentifiers)), ' '));
                    }
                } catch (e) {
                    warn(logger, 'failed to listen for updates', e);
                }
            }, 300));

            function dispose() {
                try {
                    dispose = noop;
                    stop(notificationMonitor);
                } finally {
                    shutdown(asyncConnection);
                }
            }

            onUnload(window, dispose);

            onReceive(asyncConnection, function(response) {
                var mimeType = getHeader(response, 'Content-Type');
                if (mimeType && startsWith(mimeType, 'text/xml')) {
                    deserializeAndExecute(commandDispatcher, contentAsDOM(response).documentElement);
                } else {
                    warn(logger, 'unknown content in response - ' + mimeType + ', expected text/xml');
                }
            });

            onServerError(asyncConnection, function(response) {
                try {
                    warn(logger, 'server side error');
                    broadcast(serverErrorListeners, [ statusCode(response), contentAsText(response), contentAsDOM(response) ]);
                } finally {
                    dispose();
                }
            });

            whenDown(asyncConnection, function(reconnectAttempts) {
                try {
                    warn(logger, 'connection to server was lost');
                    broadcast(blockingConnectionLostListeners, [ reconnectAttempts ]);
                } finally {
                    dispose();
                }
            });

            whenTrouble(asyncConnection, function() {
                warn(logger, 'connection in trouble');
                broadcast(blockingConnectionUnstableListeners);
            });

            info(logger, 'bridge loaded!');
        }

        onLoad(window, function() {
            Bridge({window: namespace.windowID, connection: {}});
        });

        onKeyPress(document, function(ev) {
            var e = $event(ev);
            if (isEscKey(e)) cancelDefaultAction(e);
        });
    })(window.ice);
}
