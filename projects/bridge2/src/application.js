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
    //adapt to old style of registering window event handlers
    window.onUnload = curry(onUnload, window);
    //include logger.js
    //include cookie.js
    //include delay.js
    //include element.js
    namespace.$element = $element;
    namespace.$elementWithID = $elementWithID;
    namespace.captureAndRedirectSubmit = captureAndRedirectSubmit;
    namespace.enclosingBridge = enclosingBridge;
    //include event.js
    namespace.$event = $event;
    //include http.js
    //include script.js
    //include synchronizer.js
    //include command.js
    //include heartbeat.js
    //include status.js
    namespace.ComponentIndicators = ComponentIndicators;
    //include connection.async.js
    //include focus.js
    window.setFocus = recordFocus;
    namespace.Focus = { setFocus: applyFocus };
    //include submit.js
    window.iceSubmit = iceSubmit;
    window.iceSubmitPartial = iceSubmitPartial;
    window.formOf = formOf;

    var handler = window.console && window.console.firebug ? FirebugLogHandler(debug) : WindowLogHandler(debug, window.location.href);
    window.logger = Logger([ 'window' ], handler);

    //adapt to old style logger
    window.logger.debug = curry(debug, window.logger);
    window.logger.info = curry(info, window.logger);
    window.logger.warn = curry(warn, window.logger);
    window.logger.error = curry(error, window.logger);
    window.logger.child = function(category) {
        return window.logger;
    };

    namespace.connection = operator();
    namespace.resetIndicators = operator();
    namespace.disposeBridge = operator();
    namespace.disposeBridgeAndNotify = operator();

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

    namespace.Application = function(configuration, container) {
        var sessionID = configuration.session;
        var viewID = configuration.view;
        var logger = childLogger(window.logger, sessionID.substring(0, 4) + '#' + viewID);
        var indicators = DefaultIndicators(configuration, container);
        var evaluateScripts = ScriptEvaluator(logger);
        var commandDispatcher = CommandDispatcher();
        var documentSynchronizer = DocumentSynchronizer(logger, client, sessionID, viewID);
        var asyncConnection = AsyncConnection(logger, sessionID, viewID, configuration.connection, commandDispatcher);

        function replaceHTMLAndEvaluateScripts(html) {
            replaceContainerHTML(container, html);
            evaluateScripts(container);
        }

        function dispose() {
            dispose = noop;
            shutdown(documentSynchronizer);
            shutdown(asyncConnection);
        }

        onUnload(window, dispose);
        enlistView(sessionID, viewID);

        register(commandDispatcher, 'noop', noop);
        register(commandDispatcher, 'set-cookie', SetCookie);
        register(commandDispatcher, 'parsererror', ParsingError);
        register(commandDispatcher, 'redirect', Redirect);
        register(commandDispatcher, 'reload', broadcaster([ delistWindowViews, Reload ]));
        register(commandDispatcher, 'macro', function(message) {
            each(message.childNodes, curry(deserializeAndExecute, commandDispatcher));
        });
        register(commandDispatcher, 'updates', function(element) {
            each(element.getElementsByTagName('update'), function(e) {
                try {
                    var address = e.getAttribute('address');
                    var update = Update(e);
                    updateElement($elementWithID(address), update);
                    debug(logger, 'applied update : ' + asString(update));
                    evaluateScripts(document.getElementById(address));
                    //todo: move this into a listener
                    if (Ice.StateMon) {
                        Ice.StateMon.checkAll();
                        Ice.StateMon.rebuild();
                    }
                } catch (e) {
                    error(logger, 'failed to insert element: ' + asString(update), e);
                }
            });
        });
        register(commandDispatcher, 'session-expired', function() {
            warn(logger, 'Session has expired');
            on(indicators.sessionExpired);
            //avoid sending "dispose-views" request, the view is disposed by the server on session expiry
            delistView(sessionID, viewID);
            dispose();
        });

        onSend(asyncConnection, function() {
            on(indicators.busy);
        }, function() {
            off(indicators.busy);
        });

        onReceive(asyncConnection, function(response) {
            var mimeType = getHeader(response, 'Content-Type');
            if (mimeType && startsWith(mimeType, 'text/html')) {
                replaceHTMLAndEvaluateScripts(contentAsText(response));
            } else if (mimeType && startsWith(mimeType, 'text/xml')) {
                deserializeAndExecute(commandDispatcher, contentAsDOM(response).documentElement);
                synchronize(documentSynchronizer);
            } else {
                warn(logger, 'unknown content in response');
            }
            off(indicators.connectionTrouble);
        });

        onServerError(asyncConnection, function (response) {
            warn(logger, 'server side error');
            disposeView(sessionID, viewID);
            if (blank(contentAsText(response))) {
                on(indicators.serverError);
            } else {
                replaceHTMLAndEvaluateScripts(contentAsText(response));
            }
            dispose();
        });

        whenDown(asyncConnection, function() {
            warn(logger, 'connection to server was lost');
            on(indicators.connectionLost);
            dispose();
        });

        whenTrouble(asyncConnection, function() {
            warn(logger, 'connection in trouble');
            on(indicators.connectionTrouble);
        });

        info(logger, 'bridge loaded!');

        return object(function(method) {
            //public method
            method(namespace.connection, function(self) {
                return asyncConnection;
            });
            //public method used to modify bridge's status manager
            method(namespace.resetIndicators, function(self, setup) {
                each([indicators.busy, indicators.sessionExpired, indicators.serverError, indicators.connectionLost, indicators.connectionTrouble], off);
                indicators = setup(DefaultIndicators(configuration, container));
                info(logger, "status indicators were updated");
            });
            //public method
            method(namespace.disposeBridge, function(self) {
                dispose();
            });
            //public method
            method(namespace.disposeBridgeAndNotify, function(self) {
                disposeView(sessionID, viewID);
                dispose();
            });
        });
    };

    onKeyPress(document, function(ev) {
        var e = $event(ev);
        if (isEscKey(e)) cancelDefaultAction(e);
    });
})(window.Ice = new Object());
