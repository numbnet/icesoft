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

//trick YUI compressor to minify names -- see http://developer.yahoo.com/yui/compressor/#work
window.evaluate = eval;

(function(namespace) {
    //include functional.js
    //include oo.js
    //include collection.js
    //include string.js
    //include window.js
    namespace.onLoad = curry(onLoad, window);
    namespace.onUnload = curry(onUnload, window);
    //include logger.js
    //include cookie.js
    //include delay.js
    //include element.js
    namespace.$elementWithID = $elementWithID;
    namespace.enclosingBridge = enclosingBridge;
    //include event.js
    namespace.$event = $event;
    //include http.js
    //include command.js
    //include connection.async.js

    var notificationListeners = [];
    namespace.onNotification = function(callback) {
        append(notificationListeners, callback);
    };

    var serverErrorListeners = [];
    namespace.onServerError = function(callback) {
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

    var viewDisposedListeners = [];
    namespace.onViewDisposal = function(callback) {
        append(viewDisposedListeners, callback);
    };

    namespace.disposeBridge = operator();

    var handler = window.console && window.console.firebug ? FirebugLogHandler(debug) : WindowLogHandler(debug, window.location.href);
    namespace.logger = Logger([ 'window' ], handler);
    namespace.info = info;
    var views = namespace.views = namespace.views || [];

    function enlistSession(sessionID, viewID) {
        try {
            var viewsCookie = lookupCookie('ice.views');
            var sessionViewTuples = split(value(viewsCookie), ' ');
            update(viewsCookie, join(append(sessionViewTuples, sessionID + ':' + viewID), ' '));
        } catch (e) {
            Cookie('ice.views', sessionID + ':' + viewID);
        }
    }

    function delistSession(sessionID, viewID) {
        if (existsCookie('ice.views')) {
            var viewsCookie = lookupCookie('ice.views');
            var sessionViewTuples = split(value(viewsCookie), ' ');
            var fullID = sessionID + ':' + viewID;
            update(viewsCookie, join(reject(sessionViewTuples, function(tuple) {
                return tuple == fullID;
            }), ' '));
        }
    }

    function enlistView(session, view) {
        enlistSession(session, view);
        append(views, Parameter(session, view));
    }

    function delistView(session, view) {
        delistSession(session, view);
        views = reject(views, function(i) {
            return key(i) == session && value(i) == view;
        });
    }

    function delistWindowViews() {
        each(views, function(v) {
            delistSession(key(v), value(v));
        });
        empty(views);
    }

    onBeforeUnload(window, delistWindowViews);

    namespace.Application = function(configuration) {
        var sessionID = configuration.session;
        var viewID = configuration.view;

        var logger = childLogger(namespace.logger, sessionID.substring(0, 4) + '#' + viewID);
        var commandDispatcher = CommandDispatcher();
        var asyncConnection = AsyncConnection(logger, sessionID, viewID, configuration.connection, commandDispatcher, function(viewID) {
            warn(logger, 'update needs to be picked: ' + viewID);
        });

        onUnload(window, dispose);
        enlistView(sessionID, viewID);

        register(commandDispatcher, 'noop', noop);
        register(commandDispatcher, 'parsererror', ParsingError);

        //todo: factor out cookie & monitor into a bus abstraction
        //read/create cookie that contains the updated views
        var updatedViews = lookupCookie('ice.updated.views', function() {
            return Cookie('ice.updated.views', '');
        });

        //register command that handles the updated-views message
        register(commandDispatcher, 'updated-views', function(message) {
            var views = split(message.firstChild.nodeValue, ' ');
            var text = message.firstChild;
            if (text && !blank(text.data)) {
                update(updatedViews, join(asSet(concatenate(views, split(text.data, ' '))), ' '));
            } else {
                warn(logger, "No updated views were returned.");
            }
        });

        //monitor & pick updates for this view
        var updatesMonitor = run(Delay(function() {
            try {
                var views = split(value(updatedViews), ' ');
                if (notEmpty(views)) {
                    broadcast(notificationListeners, [ views ]);
                    update(updatedViews, '');
                }
            } catch (e) {
                warn(logger, 'failed to listen for updates', e);
            }
        }, 300));

        function dispose() {
            try {
                dispose = noop;
                delistView(sessionID, viewID);
                broadcast(viewDisposedListeners, [ viewID ]);
                stop(updatesMonitor);
            } finally {
                shutdown(asyncConnection);
            }
        }

        onReceive(asyncConnection, function(response) {
            var mimeType = getHeader(response, 'Content-Type');
            if (mimeType && startsWith(mimeType, 'text/xml')) {
                deserializeAndExecute(commandDispatcher, contentAsDOM(response).documentElement);
            } else {
                warn(logger, 'unknown content in response >> ' + contentAsText(response));
            }
        });

        onServerError(asyncConnection, function(response) {
            try {
                warn(logger, 'server side error');
                broadcast(serverErrorListeners, [ statusCode(reponse), contentAsText(response), contentAsDOM(response) ]);
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

        return object(function(method) {
            method(namespace.disposeBridge, dispose);
        });
    };

    onKeyPress(document, function(ev) {
        var e = $event(ev);
        if (isEscKey(e)) cancelDefaultAction(e);
    });
})(window.ice = window.ice || new Object);
