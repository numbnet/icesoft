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

if (!window.ice.icefaces) {
    (function(namespace) {
        namespace.icefaces = true;
        //include functional.js
        //include oo.js
        //include collection.js
        //include hashtable.js
        //include string.js
        //include window.js
        namespace.onLoad = curry(onLoad, window);
        namespace.onUnload = curry(onUnload, window);
        //include logger.js
        //include event.js
        //include element.js
        //include http.js
        //include submit.js
        namespace.se = singleSubmitExecuteThis;
        namespace.ser = singleSubmitExecuteThisRenderThis;
        namespace.submit = submit;
        namespace.s = submit;

        //todo: find better solution for configuring ICEpush
        //this is meant to be used by ICEpush JS code
        if (namespace.push) namespace.push.configuration.uriSuffix = '.jsf';

        var sessionExpiryListeners = [];
        namespace.onSessionExpiry = function(callback) {
            append(sessionExpiryListeners, callback);
        };

        var serverErrorListeners = [];
        namespace.onServerError = function(callback) {
            append(serverErrorListeners, callback);
        };

        var viewDisposedListeners = [];
        namespace.onViewDisposal = function(callback) {
            append(viewDisposedListeners, callback);
        };

        var submitSendListeners = [];
        namespace.onSubmitSend = function(callback) {
            append(submitSendListeners, callback);
        };

        var submitResponseListeners = [];
        namespace.onSubmitResponse = function(callback) {
            append(submitResponseListeners, callback);
        };

        var beforeUpdateListeners = [];
        namespace.onBeforeUpdate = function(callback) {
            append(beforeUpdateListeners, callback);
        };

        var afterUpdateListeners = [];
        namespace.onAfterUpdate = function(callback) {
            append(afterUpdateListeners, callback);
        };

        function configurationOf(element) {
            return detect(parents(element), function(e) {
                return e.configuration;
            }).configuration;
        }

        function deltaSubmit(element) {
            return configurationOf(element).deltaSubmit;
        }

        //wire callbacks into JSF bridge
        jsf.ajax.addOnEvent(function(e) {
            switch (e.status) {
                case 'begin':
                    broadcast(submitSendListeners);
                    break;
                case 'complete':
                    broadcast(submitResponseListeners, [ e.responseCode, e.responseText, e.responseXML ]);
                    broadcast(beforeUpdateListeners, [ e.responseXML ]);
                    break;
                case 'success':
                    broadcast(afterUpdateListeners, [ e.responseXML ]);
                    break;
            }
        });

        //notify errors captured by JSF bridge
        jsf.ajax.addOnError(function(e) {
            if (e.status == 'serverError' || e.status == 'httpError')
                broadcast(serverErrorListeners, [ e.responseCode, e.responseText, e.responseXML ]);
        });

        var handler = window.console && window.console.firebug ? FirebugLogHandler(debug) : WindowLogHandler(debug, window.location.href);
        var logger = Logger([ 'window' ], handler);

        //propagate window ID -- this strategy works for POSTs sent by Mojarra
        onLoad(window, function() {
            each(document.getElementsByTagName('form'), function(f) {
                var i = document.createElement('input');
                i.setAttribute('name', 'ice.window');
                i.setAttribute('value', window.ice.window);
                i.setAttribute('type', 'hidden');
                f.appendChild(i);
            });
        });

        var viewIDs = [];

        function retrieveUpdate(viewID) {
            append(viewIDs, viewID);
            return function() {
                var form = document.getElementById(viewID);
                var url = form.action;
                try {
                    form.action = form.action + ';ice.session.donottouch';
                    debug(logger, 'picking updates for view ' + viewID);
                    jsf.ajax.request(form, null, {render: '@all', 'ice.view': viewID});
                } catch (e) {
                    warn(logger, 'failed to pick updates', e);
                } finally {
                    form.action = url;
                }
            };
        }

        function sessionExpired(sessionExpiredPushID) {
            namespace.retrieveUpdate = noop;
            namespace.push.deregister(viewIDs);
            namespace.push.deregister(sessionExpiredPushID);
            broadcast(sessionExpiryListeners);
        }

        namespace.setupPush = function(viewID, sessionExpiryPushID) {
            ice.push.register([viewID], retrieveUpdate(viewID));
            ice.push.register([sessionExpiryPushID], sessionExpired);
        };

        namespace.captureEnterKey = function(id) {
            var f = document.getElementById(id);
            f.onkeypress = function(ev) {
                var e = $event(ev, f);
                var element = triggeredBy(e);
                if (isEnterKey(e) && toLowerCase(element.nodeName) != 'textarea') {
                    submit(ev || window.event, element);
                    return false;
                } else {
                    return true;
                }
            };
        };

        namespace.captureSubmit = function(id) {
            var f = document.getElementById(id);
            //hijack browser form submit, instead submit through an Ajax request
            f.submit = function() {
                submit(null, f);
            };
            f.onsubmit = none;
            each(['onkeydown', 'onkeypress', 'onkeyup', 'onclick', 'ondblclick', 'onchange'], function(name) {
                f[name] = function(e) {
                    var event = e || window.event;
                    var element = event.target || event.srcElement;
                    f.onsubmit = function() {
                        submit(event, element);
                        f.onsubmit = none;
                        return false;
                    };
                };
            });

            onLoad(window, function() {
                if (deltaSubmit(f)) {
                    f.previousParameters = HashSet(jsf.getViewState(f).split('&'));
                }
            });
        };

        var client = Client(true);
        onBeforeUnload(window, function() {
            postSynchronously(client, 'dispose-window.icefaces.jsf', function(query) {
                addNameValue(query, 'ice.window', namespace.window);
                each(viewIDs, curry(addNameValue, query, 'ice.view'));
            }, FormPost, noop);
        });

        //re-write DOM to persist current markup before navigating to a different page
        var documentChanged = false;
        namespace.onAfterUpdate(function() {
            documentChanged = true;
        });
        onBeforeUnload(window, function() {
            if (documentChanged) {
                document.write("<html>" + document.documentElement.innerHTML + "</html>");
                document.close();
            }
        });

        onKeyPress(document, function(ev) {
            var e = $event(ev);
            if (isEscKey(e)) cancelDefaultAction(e);
        });

        //include status.js        
    })(window.ice);
}

