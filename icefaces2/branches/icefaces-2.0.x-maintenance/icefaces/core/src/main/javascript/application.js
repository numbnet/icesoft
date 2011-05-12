/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

if (!window.ice) {
    window.ice = new Object;
}

if (!window.ice.icefaces) {
    (function(namespace) {
        namespace.icefaces = true;
        namespace.configuration = new Object();
        namespace.disableDefaultErrorPopups = false;

        var sessionExpiryListeners = [];
        namespace.onSessionExpiry = function(callback) {
            append(sessionExpiryListeners, callback);
        };

        var networkErrorListeners = [];
        namespace.onNetworkError = function(callback) {
            append(networkErrorListeners, callback);
        };

        var serverErrorListeners = [];
        namespace.onServerError = function(callback) {
            append(serverErrorListeners, callback);
        };

        var viewDisposedListeners = [];
        namespace.onViewDisposal = function(callback) {
            append(viewDisposedListeners, callback);
        };

        var beforeSubmitListeners = [];
        namespace.onBeforeSubmit = function(callback) {
            append(beforeSubmitListeners, callback);
        };

        var beforeUpdateListeners = [];
        namespace.onBeforeUpdate = function(callback) {
            append(beforeUpdateListeners, callback);
        };

        var afterUpdateListeners = [];
        namespace.onAfterUpdate = function(callback) {
            append(afterUpdateListeners, callback);
        };

        var elementRemoveListeners = [];
        namespace.onElementRemove = function(id, callback) {
            append(elementRemoveListeners, Cell(id, callback));
        };

        function configurationOf(element) {
            return detect(parents(element),
                    function(e) {
                        return e.configuration;
                    }).configuration;
        }

        function deltaSubmit(element) {
            return configurationOf(element).deltaSubmit;
        }

        function viewIDOf(element) {
            return configurationOf(element).viewID;
        }

        function formOf(element) {
            return toLowerCase(element.nodeName) == 'form' ? element : enclosingForm(element);
        }

        function lookupElementById(id) {
            var e;
            if (id == 'javax.faces.ViewRoot') {
                e = document.documentElement;
            } else if (id == 'javax.faces.ViewBody') {
                e = document.body;
            } else {
                try {
                    e = document.getElementById(id);
                } catch (e) {
                    //element not found, error thrown only in IE
                }
            }

            return e;
        }

        //include functional.js
        //include oo.js
        //include collection.js
        //include hashtable.js
        //include string.js
        //include delay.js

        //include window.js
        namespace.onLoad = curry(onLoad, window);
        namespace.onUnload = curry(onUnload, window);

        //include event.js
        //include element.js
        //include logger.js
        var handler = window.console && window.console.log ? ConsoleLogHandler(debug) : WindowLogHandler(debug, window.location.href);
        var logger = Logger([ 'window' ], handler);
        namespace.log = logger;
        namespace.log.debug = debug;
        namespace.log.info = info;
        namespace.log.warn = warn;
        namespace.log.error = error;
        namespace.log.childLogger = childLogger;

        //include focus.js
        namespace.setFocus = setFocus;
        namespace.sf = setFocus;
        namespace.applyFocus = applyFocus;
        namespace.af = applyFocus;

        //include http.js
        //include submit.js
        namespace.se = singleSubmitExecuteThis;
        namespace.ser = singleSubmitExecuteThisRenderThis;
        namespace.submit = submit;
        namespace.s = submit;

        function appendHiddenInputElement(form, name, value, defaultValue) {
            var hiddenInput = document.createElement('input');
            hiddenInput.setAttribute('name', name);
            hiddenInput.setAttribute('value', value);
            hiddenInput.setAttribute('type', 'hidden');
            if (defaultValue) {
                hiddenInput.defaultValue = defaultValue;
            }
            form.appendChild(hiddenInput);
            return hiddenInput;
        }

        var viewIDs = [];

        function retrieveUpdate(viewID) {
            append(viewIDs, viewID);
            return function() {
                var form = document.getElementById(viewID);
                try {
                    debug(logger, 'picking updates for view ' + viewID);
                    jsf.ajax.request(form, null, {'ice.submit.type': 'ice.push', render: '@all', 'ice.view': viewID, 'ice.window': namespace.window});
                } catch (e) {
                    warn(logger, 'failed to pick updates', e);
                }
            };
        }

        var client = Client();

        function disposeWindow(viewID) {
            return function() {
                var form = document.getElementById(viewID);
                try {
                    //dispose is the final operation on this page, so no harm
                    //in modifying the action to remove CDI conversation id
                    var encodedURLElement = form['javax.faces.encodedURL'];
                    var url = encodedURLElement ? encodedURLElement.value : form.action;
                    form.action = url.replace(/(\?|&)cid=[0-9]+/, "$1");
                    debug(logger, 'dispose window and associated views ' + viewIDs);
                    postSynchronously(client, form.action, function(query) {
                        addNameValue(query, 'ice.submit.type', 'ice.dispose.window');
                        addNameValue(query, 'ice.window', namespace.window);
                        addNameValue(query, 'javax.faces.ViewState', form['javax.faces.ViewState'].value);
                        each(viewIDs, curry(addNameValue, query, 'ice.view'));
                    }, FormPost, noop);
                } catch (e) {
                    warn(logger, 'failed to notify window disposal', e);
                }
            };
        }

        function sessionExpired() {
            //stop retrieving updates
            retrieveUpdate = noop;
            //dregister pushIds to stop blocking connection
            each(viewIDs, namespace.push.deregister);
            //notify listeners
            broadcast(sessionExpiryListeners);
        }

        function containsXMLData(doc) {
            //test if document contains XML data since IE will return a XML document for dropped connection
            return doc && doc.documentElement;
        }

        //wire callbacks into JSF bridge
        jsf.ajax.addOnEvent(function(e) {
            switch (e.status) {
                case 'begin':
                    broadcast(beforeSubmitListeners, [ e.source ]);
                    break;
                case 'complete':
                    var xmlContent = e.responseXML;
                    if (containsXMLData(xmlContent)) {
                        broadcast(beforeUpdateListeners, [ xmlContent ]);
                    } else {
                        warn(logger, 'the response does not contain XML data');
                    }
                    break;
                case 'success':
                    var xmlContent = e.responseXML;
                    broadcast(afterUpdateListeners, [ xmlContent ]);
                    var updates = xmlContent.documentElement.firstChild.childNodes;
                    var updateDescriptions = collect(updates, function(update) {
                        var id = update.getAttribute('id');
                        return update.nodeName + (id ? '["' + id + '"]' : '') +
                                ': ' + substring(update.firstChild.data, 0, 40) + '....';
                    });
                    debug(logger, 'applied updates >>\n' + join(updateDescriptions, '\n'));
                    break;
            }
        });

        //notify errors captured by JSF bridge
        jsf.ajax.addOnError(function(e) {
            if (e.status == 'serverError') {
                var xmlContent = e.responseXML;
                if (containsXMLData(xmlContent)) {
                    var errorName = xmlContent.getElementsByTagName("error-name")[0].firstChild.nodeValue;
                    if (errorName && contains(errorName, 'org.icefaces.application.SessionExpiredException')) {
                        info(logger, 'received session expired message');
                        sessionExpired();
                        return;
                    }
                }

                info(logger, 'received error message [code: ' + e.responseCode + ']: ' + e.responseText);
                broadcast(serverErrorListeners, [ e.responseCode, e.responseText, containsXMLData(xmlContent) ? xmlContent : null]);
            } else if (e.status == 'httpError') {
                warn(logger, 'HTTP error [code: ' + e.responseCode + ']: ' + e.description);
                broadcast(networkErrorListeners, [ e.responseCode, e.description]);
            } else {
                //If the error falls through the other conditions, just log it.
                error(logger, 'Error [status: ' + e.status + ' code: ' + e.responseCode + ']: ' + e.description);
            }
        });


        namespace.setupBridge = function(setupID, viewID, windowID, configuration) {
            var container = document.getElementById(setupID).parentNode;
            container.configuration = configuration;
            container.configuration.viewID = viewID;
            namespace.window = windowID;
            if (configuration.sendDisposeWindow) {
                onBeforeUnload(window, disposeWindow(viewID));
            }
            setupDefaultIndicators(container, configuration);
        };

        namespace.setupPush = function(viewID) {
            ice.push.register([viewID], retrieveUpdate(viewID));
        };

        namespace.captureEnterKey = function(id) {
            var f = document.getElementById(id);
            f.onkeypress = function(ev) {
                var e = $event(ev, f);
                var element = triggeredBy(e);
                var type = toLowerCase(element.nodeName);
                if (isEnterKey(e) && (type != 'textarea' && type != 'a')) {
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
            f.nativeSubmit = f.submit;
            f.submit = function() {
                submit(null, f);
            };
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
        };

        namespace.enableSingleSubmit = function(id) {
            var f = document.getElementById(id);

            function submitForm(ev) {
                //need to investigate why $event did not provide type
                var eType;
                if (window.event) {
                    eType = window.event.type;
                } else {
                    eType = ev.type;
                }
                if (0 == eType.indexOf("on")) {
                    //strip "on" from front of event name
                    eType = eType.substr(2);
                }
                var e = $event(ev, f);
                var element = triggeredBy(e);

                var elementType = element.type;
                if (!elementType) {
                    return;
                }
                elementType = toLowerCase(elementType);

                if (elementType == 'image') {
                    return;
                }
                if (elementType == 'submit') {
                    return;
                }
                if ((null == element.id) || ("" == element.id)) {
                    return;
                }

                var isText = ( (elementType == "text") ||
                        (elementType == "password") ||
                        (elementType == "textarea") );
                if (isText) {
                    if ((eType == "click") || (eType == "blur")) {
                        //click events should not trigger text box submit
                        //blur events are mostly redundant with change events
                        return;
                    }
                }

                if (0 == elementType.indexOf("select-")) {
                    if (element.selectedIndex == element.previousSelectedIndex) {
                        //ignore clicks that do not change state
                        return;
                    } else {
                        element.previousSelectedIndex = element.selectedIndex;
                    }
                }

                ice.setFocus(null);
                ice.se(e, element);
            }

            if (f.addEventListener) {
                //events for most browsers
                f.addEventListener('blur', submitForm, true);
                f.addEventListener('change', submitForm, true);
            } else {
                //events for IE
                f.attachEvent('onfocusout', submitForm);
                f.attachEvent('onclick', submitForm);
            }
        };

        namespace.calculateInitialParameters = function(id) {
            var f = document.getElementById(id);
            f.previousParameters = HashSet(jsf.getViewState(f).split('&'));
        };

        function isComponentRendered(form) {
            return form['javax.faces.encodedURL'] ||
                    form['javax.faces.ViewState'] ||
                    form['ice.window'] ||
                    form['ice.view'] ||
                    (form.id && form[form.id] && form.id == form[form.id].value);
        }

        function findUpdatedForms(updates) {
            return inject(updates.getElementsByTagName('update'), [], function(result, update) {
                var id = update.getAttribute('id');
                var e = lookupElementById(id);
                if (e) {
                    if (toLowerCase(e.nodeName) == 'form') {
                        if (isComponentRendered(e)) {
                            append(result, e);//the form is the updated element
                        }
                    } else {
                        var updatedForms = asArray(select(e.getElementsByTagName('form'), isComponentRendered));//find the enclosed forms
                        result = concatenate(result, updatedForms);
                    }
                }

                return result;
            });
        }

        //store form parameters in case the form will be updated (and thus parameters lost)
        var previousFormParameters = HashTable();
        namespace.onBeforeUpdate(function(updates) {
            each(findUpdatedForms(updates), function(form) {
                if (deltaSubmit(form)) {
                    putAt(previousFormParameters, form.id, form.previousParameters);
                }
            });
        });

        //fix JSF issue: http://jira.icefaces.org/browse/ICE-5691 
        namespace.onAfterUpdate(function(updates) {
            var viewStateUpdate = detect(updates.getElementsByTagName('update'), function(update) {
                return update.getAttribute('id') == 'javax.faces.ViewState';
            });

            if (viewStateUpdate) {
                var viewState = viewStateUpdate.firstChild.data;
                var forms = findUpdatedForms(updates);
                each(forms, function(form) {
                    //add hidden input field to the updated forms that don't have it
                    if (!form['javax.faces.ViewState']) {
                        appendHiddenInputElement(form, 'javax.faces.ViewState', viewState, viewState);
                        debug(logger, 'append missing "javax.faces.ViewState" input element to form["' + form.id + '"]');
                    }

                    //recalculate previous parameters for the updated forms if necessary
                    if (deltaSubmit(form)) {
                        var previousParameters = at(previousFormParameters, form.id);
                        if (previousParameters) {
                            var recalculatedParameters = jsf.getViewState(form).split('&');
                            //add parameters corresponding to newly inserted form elements
                            each(recalculatedParameters, function(p) {
                                if (!contains(previousParameters, p)) {
                                    append(previousParameters, p);
                                }
                            });
                            form.previousParameters = previousParameters;
                        } else {
                            debug(logger, 'recalculate initial parameters for updated form["' + form.id + '"]');
                            form.previousParameters = HashSet(jsf.getViewState(form).split('&'));
                        }
                    }
                });
                previousFormParameters = HashTable();
            }
        });

        //determine which elements are not present after an update, invoke corresponding callback when element was removed
        namespace.onAfterUpdate(function(updates) {
            elementRemoveListeners = reject(elementRemoveListeners, function(idCallbackTuple) {
                var id = key(idCallbackTuple);
                var notFound = !document.getElementById(id);
                if (notFound) {
                    var callback = value(idCallbackTuple);
                    try {
                        callback();
                    } catch (e) {
                        //ignore exception thrown in callback
                        //to make sure that the corresponding entry is removed from the list 
                    }
                }

                return notFound;
            });
        });

        onKeyPress(document, function(ev) {
            var e = $event(ev);
            if (isEscKey(e)) cancelDefaultAction(e);
        });

        //include status.js
        //include blockui.js
        //include fixjsf.js
    })(window.ice);
}

