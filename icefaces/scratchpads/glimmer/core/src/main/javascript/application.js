/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
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

        function viewIDOf(element) {
            return configurationOf(element).viewID;
        }

        function appendHiddenInputElement(form, name, value) {
            var hiddenInput = document.createElement('input');
            hiddenInput.setAttribute('name', name);
            hiddenInput.setAttribute('value', value);
            hiddenInput.setAttribute('type', 'hidden');
            form.appendChild(hiddenInput);
            return hiddenInput;
        }

        //wire callbacks into JSF bridge
        jsf.ajax.addOnEvent(function(e) {
            switch (e.status) {
                case 'complete':
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

        var viewIDs = [];

        function retrieveUpdate(viewID) {
            append(viewIDs, viewID);
            return function() {
                var form = document.getElementById(viewID);
                var url = form.action;
                try {
                    form.action = form.action + ';ice.session.donottouch';
                    debug(logger, 'picking updates for view ' + viewID);
                    jsf.ajax.request(form, null, {render: '@all', 'ice.view': viewID, 'ice.window': namespace.window});
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

        namespace.captureSubmit = function(id, delta) {
            var f = document.getElementById(id);
            //hijack browser form submit, instead submit through an Ajax request
            f.submit = function() {
                submit(null, f);
            };
//            f.onsubmit = none;
            each(['onkeydown', 'onkeypress', 'onkeyup', 'onclick', 'ondblclick', 'onchange'], function(name) {
                f[name] = function(e) {
                    var event = e || window.event;
                    var element = event.target || event.srcElement;
                    var disabled = document.getElementById(id+":ajaxDisabled").value;
                    if (disabled.indexOf(" "+element.id+" ") > 0)  {
                        return true;
                    }  else {
                        f.onsubmit = function() {
                            submit(event, element);
                            f.onsubmit = none;
                            return false;
                        };
                    }
                };
            });

            if (delta) {
                f.previousParameters = HashSet(jsf.getViewState(f).split('&'));
            }
        };

        //fix JSF issue: http://jira.icefaces.org/browse/ICE-5691 
        namespace.onAfterUpdate(function(updates) {
            var viewState;
            var forms = inject(updates.getElementsByTagName('update'), [], function(result, update) {
                var id = update.getAttribute('id');
                if (id == 'javax.faces.ViewState') {
                    viewState = update.firstChild.data;
                } else {
                    var e;
                    if (id == 'javax.faces.ViewHead') {
                        return result;//ignore update
                    } else if (id == 'javax.faces.ViewRoot') {
                        e = document.documentElement;
                    } else if (id == 'javax.faces.ViewBody') {
                        e = document.body;
                    } else {
                        e = document.getElementById(id);
                    }

                    if (toLowerCase(e.nodeName) == 'form') {
                        append(result, e);//the form is the updated element
                    } else {
                        var updatedForms = asArray(e.getElementsByTagName('form'));//find the enclosed forms
                        result = concatenate(result, updatedForms);
                    }
                }

                return result;
            });

            if (viewState) {
                //add hidden input field to the updated forms that don't have it
                each(forms, function(form) {
                    if (!form['javax.faces.ViewState']) {
                        appendHiddenInputElement(form, 'javax.faces.ViewState', viewState);
                    }
                });
            }
        });

        var client = Client(true);
        onBeforeUnload(window, function() {
            postSynchronously(client, 'dispose-window.icefaces.jsf', function(query) {
                addNameValue(query, 'ice.window', namespace.window);
                each(viewIDs, curry(addNameValue, query, 'ice.view'));
            }, FormPost, noop);
        });

        onKeyPress(document, function(ev) {
            var e = $event(ev);
            if (isEscKey(e)) cancelDefaultAction(e);
        });

        //include status.js        
    })(window.ice);
}

