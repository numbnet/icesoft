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
    //include synchronizer.js
    //include command.js
    //include heartbeat.js
    //include connection.async.js
    //include submit.js
    namespace.submitEvent = submitEvent;

    namespace.onServerError = operator();
    namespace.onBlockingConnectionUnstable = operator();
    namespace.onBlockingConnectionLost = operator();
    namespace.onViewDisposal = operator();
    namespace.onSubmitSend = operator();
    namespace.onSubmitResponse = operator();
    namespace.onBeforeUpdate = operator();
    namespace.onAfterUpdate = operator();

    var handler = window.console && window.console.firebug ? FirebugLogHandler(debug) : WindowLogHandler(debug, window.location.href);
    namespace.logger = Logger([ 'window' ], handler);

    namespace.resetIndicators = operator();
    namespace.disposeBridge = operator();

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

    function replaceContainerHTML(container, html) {
        var start = new RegExp('\<body[^\<]*\>', 'g').exec(html);
        var end = new RegExp('\<\/body\>', 'g').exec(html);
        var body = html.substring(start.index, end.index + end[0].length)
        var bodyContent = body.substring(body.indexOf('>') + 1, body.lastIndexOf('<'));
        //strip <noscript> tag to fix Safari bug
        // #3131 If this is a response from an error code, there may not be a <noscript> tag.
        var startNoscript = new RegExp('\<noscript\>', 'g').exec(bodyContent);
        if (startNoscript == null) {
            container.innerHTML = bodyContent;
        } else {
            var endNoscript = new RegExp('\<\/noscript\>', 'g').exec(bodyContent);
            container.innerHTML = substring(bodyContent, 0, startNoscript.index) + substring(bodyContent, endNoscript.index + 11, bodyContent.length);
        }
    }

    onLoad(window, function() {
        each(document.getElementsByTagName('form'), function(f) {
            //hijack browser form submit, instead submit through an Ajax request
            f.submit = function() {
                submitEvent(null, f);
            };
            f.onsubmit = none;
            each(['onkeydown', 'onkeypress', 'onkeyup', 'onclick', 'ondblclick', 'onchange'], function(name) {
                f[name] = function(e) {
                    var event = e || window.event;
                    var element = event.target || event.srcElement;
                    f.onsubmit = function() {
                        submitEvent(event, element, f);
                        f.onsubmit = none;
                        return false;
                    };
                };
            });

            //propagate window ID -- this strategy works for POSTs sent by Mojarra
            var i = document.createElement('input');
            i.setAttribute('name', 'ice.window');
            i.setAttribute('value', window.ice.window);
            i.setAttribute('type', 'hidden');
            f.appendChild(i);
        });
    });

    onBeforeUnload(window, delistWindowViews);

    var asyncContext;
    onBeforeUnload(window, function() {
        postSynchronously(Client(true), asyncContext + 'dispose-window.icefaces.jsf', function(query) {
            addNameValue(query, 'ice.window', namespace.window);
        }, FormPost, noop);
    });

    namespace.Application = function(configuration, container) {
        var blockingConnectionLostListeners = [];
        var blockingConnectionUnstableListeners = [];
        var serverErrorListeners = [];
        var viewDisposedListeners = [];
        var submitSendListeners = [];
        var submitResponseListeners = [];
        var beforeUpdateListeners = [];
        var afterUpdateListeners = [];

        asyncContext = configuration.connection.context.async;

        var sessionID = configuration.session;
        //todo: can we rely on javax.faces.ViewState to identify the view?
        var viewID = document.getElementById('javax.faces.ViewState').value;
        var logger = childLogger(namespace.logger, sessionID.substring(0, 4) + '#' + viewID);
        var commandDispatcher = CommandDispatcher();
        var asyncConnection = AsyncConnection(logger, sessionID, viewID, configuration.connection, commandDispatcher, function(viewID) {
            try {
                var newForm = document.createElement('form');
                newForm.action = window.location.pathname;
                jsf.ajax.request(newForm, null, {'ice.session.donottouch': true,  render: '@all', 'javax.faces.ViewState': viewID, 'ice.window': namespace.window});
            } catch (e) {
                warn(logger, 'failed to pick updates', e);
            }
        });

        function dispose() {
            try {
                dispose = noop;
                delistView(sessionID, viewID);
                broadcast(viewDisposedListeners);
            } finally {
                shutdown(asyncConnection);
            }
        }

        onUnload(window, dispose);
        enlistView(sessionID, viewID);

        register(commandDispatcher, 'noop', noop);
        register(commandDispatcher, 'parsererror', ParsingError);

        onReceive(asyncConnection, function(response) {
            var mimeType = getHeader(response, 'Content-Type');
            if (mimeType && startsWith(mimeType, 'text/html')) {
                replaceContainerHTML(contentAsText(response), container);
            } else if (mimeType && startsWith(mimeType, 'text/xml')) {
                deserializeAndExecute(commandDispatcher, contentAsDOM(response).documentElement);
            } else {
                warn(logger, 'unknown content in response');
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

        jsf.ajax.addOnEvent(function(e) {
            switch (e.status) {
                case 'begin':
                    //todo: broadcast only when submit was triggered by child element of the container element
                    broadcast(submitSendListeners);
                    break;
                case 'complete':
                    //todo: broadcast only when submit was triggered by child element of the container element
                    broadcast(submitResponseListeners, [ e.responseText, e.responseXML ]);
                    //todo: broadcast only when update is for child element of the container element
                    broadcast(beforeUpdateListeners, [ e.responseXML.childNodes[0].childNodes[0].childNodes ]);
                    break;
                case 'success':
                    //todo: broadcast only when update is for child element of the container element
                    broadcast(afterUpdateListeners, [ e.responseXML.childNodes[0].childNodes[0].childNodes ]);
                    break;
            }
        });

        jsf.ajax.addOnError(function(e) {
            if (e.status == 'serverError')
                broadcast(serverErrorListeners, [ e.responseCode, e.responseText, e.responseXML ]);
        });

        info(logger, 'bridge loaded!');

        return object(function(method) {
            method(namespace.onServerError, function(self, callback) {
                append(serverErrorListeners, callback);
            });

            method(namespace.onBlockingConnectionUnstable, function(self, callback) {
                append(blockingConnectionUnstableListeners, callback);
            });

            method(namespace.onBlockingConnectionLost, function(self, callback) {
                append(blockingConnectionLostListeners, callback);
            });

            method(namespace.onViewDisposal, function(self, callback) {
                append(viewDisposedListeners, callback);
            });

            method(namespace.onSubmitSend, function(self, callback) {
                append(submitSendListeners, callback);
            });

            method(namespace.onSubmitResponse, function(self, callback) {
                append(submitResponseListeners, callback);
            });

            method(namespace.onBeforeUpdate, function(self, callback) {
                append(beforeUpdateListeners, callback);
            });

            method(namespace.onAfterUpdate, function(self, callback) {
                append(afterUpdateListeners, callback);
            });

            method(namespace.disposeBridge, dispose);
        });
    };

    onKeyPress(document, function(ev) {
        var e = $event(ev);
        if (isEscKey(e)) cancelDefaultAction(e);
    });
})(window.ice = window.ice || new Object);
