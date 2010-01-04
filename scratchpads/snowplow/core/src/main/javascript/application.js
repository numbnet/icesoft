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

        function delistPushIDWithBrowser(id) {
            if (existsCookie('ice.pushids')) {
                var idsCookie = lookupCookie('ice.pushids');
                var registeredIDs = split(value(idsCookie), ' ');
                update(idsCookie, join(reject(registeredIDs, function(registeredID) {
                    return registeredID == id;
                }), ' '));
            }
        }

        function enlistPushIDWithWindow(id) {
            enlistPushIDsWithBrowser(id);
            pushIdentifiers = concatenate(pushIdentifiers, id);
        }

        function delistPushIDWithWindow(id) {
            delistPushIDWithBrowser(id);
            pushIdentifiers = complement(pushIdentifiers, id);
        }

        function delistWindowPushIDs() {
            each(pushIdentifiers, delistPushIDWithBrowser);
            pushIdentifiers = [];
        }

        onBeforeUnload(window, delistWindowPushIDs);

        var currentNotifications = [];
        var apiChannel = Client(true);
        //public API
        namespace.uriextension = '';
        namespace.push = {
            register: function(pushIds, callback) {
                enlistPushIDWithWindow(pushIds);
                namespace.onNotification(function(ids) {
                    currentNotifications = asArray(intersect(ids, pushIds));
                    if (notEmpty(currentNotifications)) {
                        callback(currentNotifications);
                    }
                });
            },

            deregister: delistPushIDWithWindow,

            getCurrentNotifications: function() {
                return currentNotifications;
            },

            createPushId: function() {
                var id;
                postSynchronously(apiChannel, 'create-push-id.icepush' + namespace.uriextension, noop, FormPost, function(response) {
                    id = contentAsText(response);
                });
                return id;
            },

            notify: function(ids) {
                postAsynchronously(apiChannel, 'notify.icepush' + namespace.uriextension, function(q) {
                    each(ids, curry(addNameValue, q, 'id'));
                }, FormPost, noop);
            },

            addGroupMember: function(group, id) {
                postAsynchronously(apiChannel, 'add-group-member.icepush' + namespace.uriextension, function(q) {
                    addNameValue(q, 'group', group);
                    addNameValue(q, 'id', id);
                }, FormPost, noop);
            },

            removeGroupMember: function(group, id) {
                postAsynchronously(apiChannel, 'remove-group-member.icepush' + namespace.uriextension, function(q) {
                    addNameValue(q, 'group', group);
                    addNameValue(q, 'id', id);
                }, FormPost, noop);
            },

            get: function(uri, parameters, responseCallback) {
                getAsynchronously(apiChannel, uri, function(query) {
                    parameters(curry(addNameValue, query));
                }, noop, function(response) {
                    responseCallback(statusCode(response), contentAsText(response), contentAsDOM(response));
                });
            },

            post: function(uri, parameters, responseCallback) {
                postAsynchronously(apiChannel, uri, function(query) {
                    parameters(curry(addNameValue, query));
                }, FormPost, function(response) {
                    responseCallback(statusCode(response), contentAsText(response), contentAsDOM(response));
                });
            }
        };

        function Bridge(configuration) {
            var windowID = configuration.window;
            var logger = childLogger(namespace.logger, windowID);
            var commandDispatcher = CommandDispatcher();
            var asyncConnection = AsyncConnection(logger, windowID, configuration.connection);

            register(commandDispatcher, 'noop', noop);
            register(commandDispatcher, 'parsererror', ParsingError);

            //todo: factor out cookie & monitor into a communication bus abstraction
            //read/create cookie that contains the notified pushID
            var notifiedPushIDs = lookupCookie('ice.updated.views', function() {
                return Cookie('ice.updated.views', '');
            });

            //register command that handles the updated-views message
            register(commandDispatcher, 'updated-views', function(message) {
                var text = message.firstChild;
                if (text && !blank(text.data)) {
                    var ids = split(value(notifiedPushIDs), ' ');
                    update(notifiedPushIDs, join(asSet(concatenate(ids, split(text.data, ' '))), ' '));
                } else {
                    warn(logger, "No notification was returned.");
                }
            });

            //monitor & pick updates for this window
            var notificationMonitor = run(Delay(function() {
                try {
                    var ids = split(value(notifiedPushIDs), ' ');
                    if (notEmpty(ids)) {
                        broadcast(notificationListeners, [ ids ]);
                        //remove only the pushIDs contained by this page since notificationListeners can only contain listeners from the current page
                        update(notifiedPushIDs, join(complement(ids, pushIdentifiers), ' '));
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
