/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

if (!window.ice) {
    window.ice = new Object;
}

if (!window.ice.icefaces) {
    (function(namespace) {
        namespace.icefaces = true;
        namespace.configuration = new Object();
        namespace.disableDefaultErrorPopups = false;
        //define primitive submit function to allow overriding it later in special environments
        namespace.submitFunction = jsf.ajax.request;

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
        //alias for onBeforeSubmit
        //deprecated
        namespace.onSubmitSend = namespace.onBeforeSubmit;

        var beforeUpdateListeners = [];
        namespace.onBeforeUpdate = function(callback) {
            append(beforeUpdateListeners, callback);
        };
        //alias for onBeforeUpdate
        //deprecated
        namespace.onSubmitResponse = namespace.onBeforeUpdate;

        var afterUpdateListeners = [];
        namespace.onAfterUpdate = function(callback) {
            append(afterUpdateListeners, callback);
        };

        var elementRemoveListeners = [];
        namespace.onElementRemove = function(id, callback) {
            append(elementRemoveListeners, Cell(id, callback));
        };

        function configurationOf(element) {
            configParent = detect(parents(element),
                function(e) {
                    if (null != e) {
                        return e.configuration;
                    }
                    return {};
                });
            if (null != configParent) {
                return configParent.configuration;
            }
            debug(logger, 'configuration not found for ' + element.nodeName);
            return {};
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

        function appendOrReplaceHiddenInputElement(form, name, value, defaultValue) {
            var element = form[name];
            if (!element) {
                appendHiddenInputElement(form, name, value, defaultValue);
            } else if (element.value != value) {
                element.parentNode.removeChild(element);
                appendHiddenInputElement(form, name, value, defaultValue);
            }
        }

        var viewIDs = [];

        function retrieveUpdate(viewID) {
            append(viewIDs, viewID);
            var form = lookupElementById(viewID);
            appendOrReplaceHiddenInputElement(form, 'ice.view', viewID);
            appendOrReplaceHiddenInputElement(form, 'ice.window', namespace.window);

            return function() {
                var form = lookupElementById(viewID);
                //form is missing after navigating to a non-icefaces page
                if (form) {
                    try {
                        debug(logger, 'picking updates for view ' + viewID);
                        jsf.ajax.request(form, null, {'ice.submit.type': 'ice.push', render: '@all'});
                    } catch (e) {
                        warn(logger, 'failed to pick updates', e);
                    }
                }
            };
        }

        var client = Client();

        function disposeWindow(viewID) {
            return function() {
                var form = lookupElementById(viewID);
                //form is missing after navigating to a non-icefaces page
                if (form) {
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
                }
            };
        }

        function sessionExpired() {
            //stop retrieving updates
            retrieveUpdate = noop;
            //deregister pushIds to stop blocking connection, if ICEpush is present
            if (namespace.push) {
                each(viewIDs, namespace.push.deregister);
            }
            //notify listeners
            broadcast(sessionExpiryListeners);
        }

        function containsXMLData(doc) {
            //test if document contains XML data since IE will return a XML document for dropped connection
            return doc && doc.documentElement;
        }

        //define function to be wired as submit callback into JSF bridge
        function submitEventBroadcaster(perRequestOnBeforeSubmitListeners, perRequestOnBeforeUpdateListeners, perRequestOnAfterUpdateListeners) {
            perRequestOnBeforeSubmitListeners = perRequestOnBeforeSubmitListeners || [];
            perRequestOnBeforeUpdateListeners = perRequestOnBeforeUpdateListeners || [];
            perRequestOnAfterUpdateListeners = perRequestOnAfterUpdateListeners || [];
            return function(e) {
                var source = e.source;
                switch (e.status) {
                    case 'begin':
                        //trigger notification only when submit is user-initiated
                        if (!source || viewIDOf(source) != source.id) {
                            broadcast(perRequestOnBeforeSubmitListeners, [ source ]);
                        }
                        break;
                    case 'complete':
                        var xmlContent = e.responseXML;
                        if (containsXMLData(xmlContent)) {
                            broadcast(perRequestOnBeforeUpdateListeners, [ xmlContent, source ]);
                        } else {
                            warn(logger, 'the response does not contain XML data');
                        }
                        break;
                    case 'success':
                        var xmlContent = e.responseXML;
                        broadcast(perRequestOnAfterUpdateListeners, [ xmlContent, source ]);
                        break;
                }
            };
        }

        //define function to be wired as error callback into JSF bridge
        function submitErrorBroadcaster(perRequestNetworkErrorListeners, perRequestServerErrorListeners) {
            perRequestNetworkErrorListeners = perRequestNetworkErrorListeners || [];
            perRequestServerErrorListeners = perRequestServerErrorListeners || [];
            return function(e) {
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
                    broadcast(perRequestServerErrorListeners, [ e.responseCode, e.responseText, containsXMLData(xmlContent) ? xmlContent : null]);
                } else if (e.status == 'httpError') {
                    warn(logger, 'HTTP error [code: ' + e.responseCode + ']: ' + e.description);
                    broadcast(perRequestNetworkErrorListeners, [ e.responseCode, e.description]);
                } else {
                    //If the error falls through the other conditions, just log it.
                    error(logger, 'Error [status: ' + e.status + ' code: ' + e.responseCode + ']: ' + e.description);
                }
            };
        }

        function filterICEfacesEvents(f) {
            var isICEfacesEvent = false;
            return function(e) {
                try {
                    var source = e.source;
                    var form = formOf(source);
                    if (form.id) {
                        var foundForm = document.getElementById(form.id);
                        if (foundForm) {
                            form = foundForm;
                        }
                    }
                    isICEfacesEvent = form['ice.view'] || form['ice.window'];
                } catch (ex) {
                    //ignore failure to find forms since that usually occurs after the update is applied
                }
                //invoke callback only when event is triggered from an ICEfaces enabled form
                if (isICEfacesEvent) {
                    f(e);
                }
            };
        }


        function logReceivedUpdates(e) {
            if ('success' == e.status) {
                var xmlContent = e.responseXML;
                var updates = xmlContent.documentElement.firstChild.childNodes;
                var updateDescriptions = collect(updates, function(update) {
                    var id = update.getAttribute('id');
                    var updateType = update.nodeName;
                    var detail = updateType + (id ? '["' + id + '"]' : '');
                    //will require special case for insert operation
                    if ('update' == updateType) {
                        detail += ': ' + substring(update.firstChild.data, 0, 40) + '....';
                    } else if ('insert' == updateType) {
                        var location = update.firstChild.getAttribute('id');
                        var text = update.firstChild.firstChild.data;
                        detail += ': ' + update.firstChild.nodeName + ' ' + location + ': ' + substring(text, 0, 40) + '....';
                    } else if ('eval' == updateType) {
                        detail += ': ' + substring(update.firstChild.data, 0, 40) + '....';
                    }
                    return detail;
                });
                debug(logger, 'applied updates >>\n' + join(updateDescriptions, '\n'));
            }
        }

        jsf.ajax.addOnEvent(filterICEfacesEvents(submitEventBroadcaster(beforeSubmitListeners, beforeUpdateListeners, afterUpdateListeners)));
        jsf.ajax.addOnError(filterICEfacesEvents(submitErrorBroadcaster(networkErrorListeners, serverErrorListeners)));
        jsf.ajax.addOnEvent(logReceivedUpdates);

        //include submit.js
        namespace.se = singleSubmitExecuteThis;
        namespace.ser = singleSubmitExecuteThisRenderThis;
        namespace.submit = submit;
        namespace.s = submit;
        namespace.fullSubmit = fullSubmit;

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
                var theEvent = null;
                if (typeof(event) != 'undefined') {
                    theEvent = event;
                } else if (window.event) {
                    theEvent = window.event;
                } else {
                    //very bizarre hack to extract parameters from high up
                    //on the call stack to obtain the current Event on Firefox
                    //this is sensitive to the depth of the call stack so
                    //it may eventually be necessary to walk up rather than
                    //test a specific caller
                    var maybeEvent = arguments.callee.caller.caller.arguments[0];
                    if (typeof(maybeEvent.target) != 'undefined') {
                        theEvent = maybeEvent;
                    }
                }
                submit(theEvent, f);
            };
            each(['onkeydown', 'onkeypress', 'onkeyup', 'onclick', 'ondblclick', 'onchange'], function(name) {
                f[name] = function(e) {
                    var event = e || window.event;
                    var element = event.target || event.srcElement;
                    f.onsubmit = function() {
                        //fallback to using form as submitting element when the element was removed by a previous
                        //update and form.onsubmit callback is called directly (by application or third party library code)
                        if (element.name && !element.id) {
                            //verify that the id is not already in use
                            if (!document.getElementById(element.name)) {
                                element.id = element.name;
                            }
                        }
                        var elementExists = document.getElementById(element.id);
                        submit(event, elementExists ? element : f);
                        f.onsubmit = null;
                        return false;
                    };
                };
            });
        };

        namespace.enableSingleSubmit = function(id, useBlur) {
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
                    if ((eType == "click") || ((!useBlur) && (eType == "blur"))) {
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

        function ifViewStateUpdated(updates, callback) {
            var viewStateUpdate = detect(updates.getElementsByTagName('update'), function(update) {
                return update.getAttribute('id') == 'javax.faces.ViewState';
            });

            if (viewStateUpdate) {
                callback(viewStateUpdate.firstChild.data);
            }
        }

        function collectUpdatedForms(updates, iterator) {
            each(updates.getElementsByTagName('update'), function(update) {
                var id = update.getAttribute('id');
                var e = lookupElementById(id);
                if (e) {
                    if (toLowerCase(e.nodeName) == 'form') {
                        if (isComponentRendered(e)) {
                            iterator(e);//the form is the updated element
                        }
                    } else {
                        //find all the forms in the update's markup, just in case the executed javascript moved forms
                        //around after the update was applied
                        var formStartTags = update.firstChild.data.match(/\<form[^\<]*\>/g);
                        if (formStartTags) {
                            each(formStartTags, function(formStartTag) {
                                //find 'id' attribute in the form start tag
                                var match = formStartTag.match(/id="([\S]*?)"/im);
                                if (match && match[1]) {
                                    var id = match[1];
                                    var form = document.getElementById(id);
                                    if (form && isComponentRendered(form)) {
                                        iterator(form);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        //fix JSF issue: http://jira.icefaces.org/browse/ICE-5691 
        namespace.onAfterUpdate(function(updates) {
            ifViewStateUpdated(updates, function(viewState) {
                collectUpdatedForms(updates, function(form) {
                    //add hidden input field to the updated forms that don't have it
                    if (!form['javax.faces.ViewState']) {
                        appendHiddenInputElement(form, 'javax.faces.ViewState', viewState, viewState);
                        debug(logger, 'append missing "javax.faces.ViewState" input element to form["' + form.id + '"]');
                    }
                });
            });
        });

        function containsFormElements(e) {
            var type = toLowerCase(e.nodeName);
            if ((type == 'input' && e.name != 'javax.faces.ViewState') ||
                type == 'select' ||
                type == 'textarea' ||
                type == 'form') {
                return true;
            } else {
                var inputs = e.getElementsByTagName('input');
                if ((notEmpty(inputs) && inputs[0].name != 'javax.faces.ViewState') ||
                    notEmpty(e.getElementsByTagName('select')) ||
                    notEmpty(e.getElementsByTagName('textarea'))) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        //recalculate delta submit previous parameters for the updated forms, if necessary
        namespace.onAfterUpdate(function(updates) {
            var recalculatedForms = HashSet();
            each(updates.getElementsByTagName('update'), function(update) {
                var id = update.getAttribute('id');
                var e = lookupElementById(id);
                if (e && containsFormElements(e)) {
                    try {
                        var form = formOf(e);
                        if (not(contains(recalculatedForms, form)) && deltaSubmit(form)) {
                            debug(logger, 'recalculate initial parameters for updated form["' + form.id + '"]');
                            form.previousParameters = HashSet(jsf.getViewState(form).split('&'));
                            append(recalculatedForms, form);
                        }
                    } catch (ex) {
                        //cannot find enclosing form, some updates are for elements located outside forms
                    }
                }
            });
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

        //MyFaces uses a linked list of view state keys to track the changes in the view state -- the participating
        //forms need to have their view state key updated so that the next submit will work with the latest saved state
        //ICE-7188
        var formViewID;
        namespace.onBeforeSubmit(function(source) {
            formViewID = formOf(source)['ice.view'].value;
        });
        namespace.onAfterUpdate(function(updates) {
            ifViewStateUpdated(updates, function(viewState) {
                //update only the forms that have the same viewID with the one used by the submitting form
                each(document.getElementsByTagName('form'), function(form) {
                    var viewIDElement = form['ice.view'];
                    var viewStateElement = form['javax.faces.ViewState'];
                    if (viewStateElement && viewIDElement && viewIDElement.value == formViewID) {
                        viewStateElement.value = viewState;
                    }
                });
            });
        });

        //clear network listeners just before reloading or navigating away to avoid falsely notified errors
        onBeforeUnload(window, function() {
            networkErrorListeners = [];
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

