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

var handler = window.console && window.console.firebug ? FirebugLogHandler(debug) : WindowLogHandler(debug, window.location.href);
window.logger = Logger([ 'window' ], handler);

//adapt to old style logger
each(['debug', 'info', 'warn', 'error'], function(level) {
    window.logger[level] = function() {
        apply(curry(window[level], window.logger), arguments);
    };
});
window.logger.child = function(category) {
    return window.logger;
};

function FormPost(request) {
    setHeader(request, 'Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
}

var connection = operator();
var attachStatusManager = operator();
var disposeBridge = operator();
var disposeBridgeAndNotify = operator();

[ Ice.Community ].as(function(This) {
    var client = Client(true);
    var views = window.views = window.views ? window.views : [];

    function enlistView(session, view) {
        append(views, Parameter(session, view));
    }

    function delistView(session, view) {
        views = reject(views, function(i) {
            return key(i) == session && value(i) == view;
        });
    }

    function delistWindowViews() {
        views = [];
    }

    function sendDisposeViews(parameters) {
        if (notEmpty(parameters)) {
            try {
                postSynchronously(client, window.disposeViewsURI, function(query) {
                    each(parameters, curry(addParameter, query));
                }, FormPost, noop);
            } catch (e) {
                warn(logger, 'Failed to notify view disposal', e);
            }
        }
    }

    function disposeView(session, view) {
        sendDisposeViews([Parameter(session, view)]);
        delistView(session, view);
    }

    function disposeWindowViews() {
        sendDisposeViews(views);
        delistWindowViews();
    }

    onBeforeUnload(window, disposeWindowViews);

    This.Application = function(configuration, container) {
        var sessionID = configuration.session;
        var viewID = configuration.view;
        enlistView(sessionID, viewID);
        var logger = childLogger(window.logger, sessionID.substring(0, 4) + '#' + viewID);
        var statusManager = Ice.Status.DefaultStatusManager(configuration, container);
        var searchAndEvaluateScripts = Ice.Script.Loader(logger);
        var commandDispatcher = Ice.Command.Dispatcher();
        var documentSynchronizer = new Ice.Document.Synchronizer(window.logger, sessionID, viewID);

        function replaceContainerHTML(html) {
            Ice.Document.replaceContainerHTML(container, html);
            searchAndEvaluateScripts(container);
        }

        var asyncConnection = This.Connection.AsyncConnection(logger, sessionID, viewID, configuration.connection, commandDispatcher);

        function dispose() {
            dispose = noop;
            documentSynchronizer.shutdown();
            shutdown(asyncConnection);
        }

        register(commandDispatcher, 'noop', noop);
        register(commandDispatcher, 'set-cookie', Ice.Command.SetCookie);
        register(commandDispatcher, 'parsererror', Ice.Command.ParsingError);
        register(commandDispatcher, 'redirect', function(element) {
            //replace ampersand entities incorrectly decoded by Safari 2.0.4
            var url = element.getAttribute("url").replace(/&#38;/g, "&");
            info(logger, 'Redirecting to ' + url);
            window.location.href = url;
        });
        register(commandDispatcher, 'reload', function(element) {
            info(logger, 'Reloading');
            var url = window.location.href;
            delistWindowViews();
            if (containsSubstring(url, 'rvn=')) {
                window.location.reload();
            } else {
                var view = element.getAttribute('view');
                if (view == '') {
                    window.location.reload();
                } else {
                    var queryPrefix = containsSubstring(url, '?') ? '&' : '?';
                    window.location.href = url + queryPrefix + 'rvn=' + view;
                }
            }
        });
        register(commandDispatcher, 'macro', function(message) {
            each(message.childNodes, curry(deserializeAndExecute, commandDispatcher));
        });
        register(commandDispatcher, 'updates', function(element) {
            each(element.getElementsByTagName('update'), function(updateElement) {
                try {
                    var address = updateElement.getAttribute('address');
                    var update = new Ice.ElementModel.Update(updateElement);
                    address.asExtendedElement().updateDOM(update);
                    debug(logger, 'applied update : ' + update.asString());
                    searchAndEvaluateScripts(address.asElement());
                    //todo: move this into a listener
                    if (Ice.StateMon) {
                        Ice.StateMon.checkAll();
                        Ice.StateMon.rebuild();
                    }
                } catch (e) {
                    error(logger, 'failed to insert element: ' + update.asString(), e);
                }
            });
        });
        register(commandDispatcher, 'session-expired', function() {
            warn(logger, 'Session has expired');
            on(statusManager.sessionExpired);
            //avoid sending "dispose-views" request, the view is disposed by the server on session expiry
            delistView(sessionID, viewID);
            dispose();
        });

        onUnload(window, dispose);

        onSend(asyncConnection, function() {
            on(statusManager.busy);
        }, function() {
            off(statusManager.busy);
        });

        onReceive(asyncConnection, function(response) {
            var mimeType = getHeader(response, 'Content-Type');
            if (mimeType && startsWith(mimeType, 'text/html')) {
                replaceContainerHTML(contentAsText(response));
            } else if (mimeType && startsWith(mimeType, 'text/xml')) {
                deserializeAndExecute(commandDispatcher, contentAsDOM(response).documentElement);
                documentSynchronizer.synchronize();
            } else {
                warn(logger, 'unknown content in response');
            }
            off(statusManager.connectionTrouble);
        });

        onServerError(asyncConnection, function (response) {
            warn(logger, 'server side error');
            disposeView(sessionID, viewID);
            if (blank(contentAsText(response))) {
                on(statusManager.serverError);
            } else {
                replaceContainerHTML(contentAsText(response));
            }
            dispose();
        });

        whenDown(asyncConnection, function() {
            warn(logger, 'connection to server was lost');
            on(statusManager.connectionLost);
            dispose();
        });

        whenTrouble(asyncConnection, function() {
            warn(logger, 'connection in trouble');
            on(statusManager.connectionTrouble);
        });

        info(logger, 'bridge loaded!');

        return object(function(method) {
            //public method
            method(connection, function(self) {
                return asyncConnection;
            });
            //public method used to modify bridge's status manager
            method(attachStatusManager, function(self, setup) {
                each([statusManager.busy, statusManager.sessionExpired, statusManager.serverError, statusManager.connectionLost, statusManager.connectionTrouble], off);
                statusManager = setup(Ice.Status.DefaultStatusManager(configuration, container));
                info(logger, "status indicators were updated");
            });
            //public method
            method(disposeBridge, function(self) {
                dispose();
            });
            //public method
            method(disposeBridgeAndNotify, function(self) {
                disposeView(sessionID, viewID);
                dispose();
            });
        });
    };
});

onKeyPress(document, function(ev) {
    var e = $event(ev);
    if (e.isEscKey()) e.cancelDefaultAction();
});
