/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
    //the eval needs to be located outside the main closure to allow proper minification from YUI compressor
    window.ice.globalEval = function(src) {
        if (window.execScript) {
            window.execScript(src);
        } else {
            (function() {
                window.eval.call(window, src);
            })();
        }
    };


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

        var elementUpdateListeners = [];
        namespace.onElementUpdate = function(id, callback) {
            var element = lookupElementById(id);
            var ancestorIDs = [];
            var cursor = element;
            while (cursor)  {
                var cursorID;
                if (cursor == document.body) {
                    cursorID = 'javax.faces.ViewBody';
                } else if (cursor == document.documentElement) {
                    cursorID = 'javax.faces.ViewRoot';
                } else if (cursor == document.getElementsByTagName('head')[0]) {
                    cursorID = 'javax.faces.ViewHead';
                } else {
                    cursorID = cursor.id;
                }

                if (cursorID) {
                    ancestorIDs.push(cursorID);
                }
                cursor = cursor.parentNode;
            }
            append(elementUpdateListeners, {identifier: id, handler: callback, ancestors: ('*' + ancestorIDs.join('*') + '*')});
        };

        function configurationOf(element) {
            var result = configurationOfImpl(element);
            if (result)  {
                return result;
            }
            result = configurationOfImpl(document.getElementById(element.id));
            if (result)  {
                return result;
            }
            debug(logger, 'configuration not found for ' + element.nodeName);
            return {};
        }

        function configurationOfImpl(element) {
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
            return null;
        }

        function deltaSubmit(element) {
            return configurationOf(element).deltaSubmit;
        }

        function viewIDOf(element) {
            return configurationOf(element).viewID;
        }

        function formOf(element) {
            try {
                return formOfNode(element);
            } catch (e)  {
                //page update may have occurred
                return formOfNode(document.getElementById(element.id));
            }
        }

        function formOfNode(element) {
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

        //function used to safely retrieve named element -- form[name] sometimes fails in IE
        function lookupNamedInputElement(form, name) {
            var e = form[name];

            if (!e) {
                e = detect(form.getElementsByTagName('input'), function(input) {
                    return input.name && input.name == name;
                });
            }

            return e;
        }

        //function used to safely retrieve ViewState key element
        function lookupViewStateElement(element) {
            var e = lookupNamedInputElement(element, 'javax.faces.ViewState');
            if (e) {
                return e;
            } else {
                throw 'cannot find javax.faces.ViewState input element';
            }
        }

        function lookupViewState(element) {
            return lookupViewStateElement(element).value;
        }

        function retrieveUpdateFormID(viewID) {
            return viewID + '-retrieve-update';
        }

        function singleSubmitFormID(viewID) {
            return viewID + '-single-submit';
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
            var formID = retrieveUpdateFormID(viewID);
            var form = lookupElementById(formID);
            appendOrReplaceHiddenInputElement(form, 'ice.view', viewID);
            appendOrReplaceHiddenInputElement(form, 'ice.window', namespace.window);

            return function() {
                var form = lookupElementById(formID);
                //form is missing after navigating to a non-icefaces page
                if (form) {
                    try {
                        debug(logger, 'picking updates for view ' + viewID);
                        var options = {
                            'ice.submit.type': 'ice.push',
                            render: '@all'
                        };
                        jsf.ajax.request(form, null, options);
                    } catch (e) {
                        warn(logger, 'failed to pick updates', e);
                    }
                }
            };
        }

        var client = Client();

        function disposeWindow(viewID) {
            return function() {
                var form = lookupElementById(singleSubmitFormID(viewID));
                //form is missing after navigating to a non-icefaces page
                if (form) {
                    try {
                        //dispose is the final operation on this page, so no harm
                        //in modifying the action to remove CDI conversation id
                        var encodedURLElement = lookupNamedInputElement(form, 'javax.faces.encodedURL');
                        var url = encodedURLElement ? encodedURLElement.value : form.action;
                        form.action = url.replace(/(\?|&)cid=[0-9]+/, "$1");
                        debug(logger, 'dispose window and associated views ' + viewIDs);
                        postSynchronously(client, form.action, function(query) {
                            addNameValue(query, 'ice.submit.type', 'ice.dispose.window');
                            addNameValue(query, 'ice.window', namespace.window);
                            addNameValue(query, 'javax.faces.ViewState', lookupViewState(form));
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

        function containsHTMLData(doc) {
            return doc.documentElement.nodeName == 'html';
        }

        //define function to be wired as submit callback into JSF bridge
        function submitEventBroadcaster(perRequestOnBeforeSubmitListeners, perRequestOnBeforeUpdateListeners, perRequestOnAfterUpdateListeners) {
            perRequestOnBeforeSubmitListeners = perRequestOnBeforeSubmitListeners || [];
            perRequestOnBeforeUpdateListeners = perRequestOnBeforeUpdateListeners || [];
            perRequestOnAfterUpdateListeners = perRequestOnAfterUpdateListeners || [];
            // Cache iceEnabled for 'success' event as submitElement may be detached from configParent
            var viewID;
            return function(submitEvent) {
                var submitElement = submitEvent.source;
                //if we have the submit element and the view ID set (ICEfaces render enabled) then the callbacks are invoked
                if (submitElement) {
                    try {
                        viewID = viewIDOf(submitElement);
                    } catch (e) {
                        //ignore failure to traverse parents when trying to find the configured viewID
                        //assume that the traversal is done on a replaced DOM fragment
                    }

                    if (viewID) {
                        switch (submitEvent.status) {
                            case 'begin':
                                //Include parameter indicating if submission was triggered by client
                                var isUserInitiatedRequest = false;
                                if (submitElement.id != retrieveUpdateFormID(viewIDOf(submitElement))) {
                                    isUserInitiatedRequest = true;
                                }
                                broadcast(perRequestOnBeforeSubmitListeners, [ submitElement, isUserInitiatedRequest ]);
                                break;
                            case 'complete':
                                var xmlContent = submitEvent.responseXML;
                                if (containsXMLData(xmlContent)) {
                                    if (containsHTMLData(xmlContent)) {
                                        //reload page when html markup is received instead of the partial update
                                        document.location = document.location.href;
                                    } else {
                                        broadcast(perRequestOnBeforeUpdateListeners, [ xmlContent, submitElement ]);
                                    }
                                } else {
                                    warn(logger, 'the response does not contain XML data');
                                    if (configurationOf(submitElement).reloadOnUpdateFailure) {
                                        warn(logger, 'reloading page ...');
                                        document.location = document.location.href;
                                    }
                                }
                                break;
                            case 'success':
                                var xmlContent = submitEvent.responseXML;
                                broadcast(perRequestOnAfterUpdateListeners, [ xmlContent, submitElement ]);
                                break;
                        }
                    }
                } else {
                    warn(logger, 'Source element is undefined, cannot determine if this view is ICEfaces enabled.')
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

        //setup page scoped submit event broadcasters
        jsf.ajax.addOnEvent(submitEventBroadcaster(beforeSubmitListeners, beforeUpdateListeners, afterUpdateListeners));
        jsf.ajax.addOnError(submitErrorBroadcaster(networkErrorListeners, serverErrorListeners));

        //setup submit error logging
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
        jsf.ajax.addOnEvent(logReceivedUpdates);

        //include submit.js
        namespace.se = singleSubmitExecuteThis;
        namespace.ser = singleSubmitExecuteThisRenderThis;
        namespace.submit = submit;
        namespace.s = submit;
        namespace.fullSubmit = fullSubmit;

        namespace.ajaxRefresh = function(viewID) {
            viewID = viewID || (document.body.configuration ? document.body.configuration.viewID : null);
            if (!viewID) {
                throw 'viewID parameter required';
            }
            var c = configurationOf(lookupElementById(retrieveUpdateFormID(viewID)));
            //cache retrieve update callback
            if (!c.ajaxRefresh) {
                c.ajaxRefresh = retrieveUpdate(viewID);
            }
            c.ajaxRefresh();
        };

        function clearEventHandlers(element) {
            element.onkeypress = null;
            element.onmousedown = null;
            element.onmousemove = null;
            element.onmouseout = null;
            element.onmouseover = null;
            element.onclick = null;
            element.oncontextmenu = null;
            element.onchange = null;
            element.onfocus = null;
            element.onblur = null;
            element.submit = null;
            element.onsubmit = null;
            element.onkeydown = null;
            element.onkeyup = null;
            element.ondblclick = null;
            element.onfocusout = null;
        }

        namespace.setupBridge = function(setupID, viewID, windowID, configuration) {
            var container = document.getElementById(setupID).parentNode;
            container.setupCount = container.setupCount ? (container.setupCount + 1) : 1;

            if (container.setupCount == 1) {
                container.configuration = configuration;
                container.configuration.viewID = viewID;
                namespace.window = windowID;
                if (configuration.sendDisposeWindow) {
                    onBeforeUnload(window, disposeWindow(viewID));
                }
                if (configuration.focusManaged) {
                    monitorFocusChanges(container);
                    restoreMonitorFocusChangesOnUpdate(container);
                }
                setupDefaultIndicators(container, configuration);
                //clear the event handlers on the elements that will most likely create a memory leak
                onUnload(window, function() {
                    container.configuration = null;

                    each(['a', 'iframe'], function(type) {
                        each(container.getElementsByTagName(type), clearEventHandlers);
                    });

                    each(container.getElementsByTagName('form'), function(form) {
                        form.submit = null;
                        form.onsubmit = null;
                        each(form.elements, clearEventHandlers);
                    });
                });
            }
        };

        namespace.setupPush = function(viewID) {
            ice.push.register([viewID], retrieveUpdate(viewID));
        };

        namespace.setupRefresh = function(viewID, interval, duration) {
            var times = duration < 0 ? null : Math.floor(duration / interval);
            var requestUpdate = retrieveUpdate(viewID);
            var delay = Delay(requestUpdate, interval);
            run(delay, times);
            var stopDelay = curry(stop, delay);
            namespace.onSessionExpiry(stopDelay);
            namespace.onNetworkError(stopDelay);
            namespace.onServerError(stopDelay);
        };

        namespace.captureEnterKey = function(id, keyMap) {
            var f = document.getElementById(id);
            f.onkeypress = function(ev) {
                var e = $event(ev, f);
                var element = triggeredBy(e);
                var type = toLowerCase(element.nodeName);
                if (type != 'textarea' && type != 'a') {
                    //capture any key when default submit element is defined
                    if (keyMap) {
                        var elementID = keyMap[keyCode(e)];
                        if (elementID) {
                            element = lookupElementById(elementID);
                            submit(ev || window.event, element);
                            return false;
                        }
                        if (isEnterKey(e)) {
                            submit(ev || window.event, element);
                            return false;
                        }
                        return true;
                    }
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
                    //a
                    var maybeCaller = null;
                    maybeCaller = arguments.callee.caller.caller;
                    if (null == maybeCaller) {
                        maybeCaller = arguments.callee.caller;
                    }
                    var maybeEvent = maybeCaller.arguments[0];
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
                            //second check is for IE9 which will lookup by name also when getElementById is invoked
                            var lookedUpElement = document.getElementById(element.name);
                            if (!lookedUpElement || !lookedUpElement.id) {
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

                    //click events should not trigger text box submit
                    //blur events are mostly redundant with change events
                    if ((eType == "click") || !useBlur && eType == "blur") {
                        return;
                    }

                    //focusout is required for older IE versions
                    //only check for the value changing if we are not relying on blur
                    if( !useBlur && eType == "focusout"){

                        if(element.value == element.previousTextValue){
                            return;
                        }

                        element.previousTextValue = element.value;
                    }
                }

                //Single selects (handled differently than multiple selects)
                if (elementType == ("select-one")) {

                    // Most browsers provide a change event which should
                    // be enough to trigger submission. If not...
                    if (eType != 'change') {

                        //Pre IE9 sends a couple of click events when pulling down the list.  We need to
                        //ignore the first one.
                        if (eType == 'click' && element.selectedIndex <= 0 && !element.previouslySelected) {

                            //debug(logger, 'ignore first IE click');
                            element.previouslySelected = element.selectedIndex;
                            return;
                        }

                        //Determine if the actual selection has changed.
                        if (element.selectedIndex == element.previouslySelected) {
                            return;
                        }

                        element.previouslySelected = element.selectedIndex;
                    }
                }

                //Multiple selects (handled differently than single selects)
                if (elementType == ("select-multiple")) {

                    // Most browsers provide a change event which should
                    // be enough to trigger submission. If not...
                    if (eType != 'change') {

                        //Most browsers provide direct access to the element.selectedOptions but
                        //for pre IE9 browsers, we need to iterate through and see how many are
                        //selected.
                        var numberOfSelected = 0;
                        for (var i = 0; i < element.options.length; i++) {
                            if (element.options[i].selected) {
                                numberOfSelected++;
                            }
                        }

                        //If the number of items selected is the same and the index of the selected item is the same
                        //then there is no change and no submission required.
                        if (numberOfSelected == element.previousNumberOfSelected &&
                            element.selectedIndex == element.previouslySelected) {
                            return;
                        }

                        //Record the index or number of options for future comparisons.
                        element.previouslySelected = element.selectedIndex;
                        element.previousNumberOfSelected = numberOfSelected;
                    }
                }

                ice.setFocus(null);
                ice.se(e, element);
            }

            if (f.addEventListener) {
                //events for most browsers
                //use the event capture listener rather than the bubble listener if submitOnBlur=true
                //or the blur events will never arrive (default is false)
                f.addEventListener('blur', submitForm, useBlur);
                f.addEventListener('change', submitForm, false);
            } else {
                //events for IE
                f.attachEvent('onfocusout', submitForm);
                f.attachEvent('onclick', submitForm);
            }
        };

        namespace.cancelSingleSubmit = function(id) {
            if (typeof id != "string") return;
            var f = document.getElementById(id);
            if (!f) return;

            var cancelBubble = function (event) {
                event = event || window.event;
                if (event.stopPropagation) event.stopPropagation();
                event.cancelBubble = true;
            };

            if (f.addEventListener) {
                f.addEventListener('blur', cancelBubble, false);
                f.addEventListener('change', cancelBubble, false);
            } else {
                f.attachEvent('onfocusout', cancelBubble);
                f.attachEvent('onclick', cancelBubble);
            }
        };

        namespace.calculateInitialParameters = function(id) {
            var f = document.getElementById(id);
            f.previousParameters = HashSet(jsf.getViewState(f).split('&'));
        };

        namespace.fixViewState = function(id, viewState) {
            var form = lookupElementById(id);
            try {
                var viewStateElement = lookupViewStateElement(form);
                if (viewStateElement.value != viewState) {
                    viewStateElement.value = viewState;
                }
            } catch (ex) {
                appendHiddenInputElement(form, 'javax.faces.ViewState', viewState, viewState);
            }
        }

        function isComponentRendered(form) {
            return lookupNamedInputElement(form, 'javax.faces.encodedURL') ||
                lookupNamedInputElement(form, 'javax.faces.ViewState') ||
                lookupNamedInputElement(form, 'ice.window') ||
                lookupNamedInputElement(form, 'ice.view') ||
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
                        var markup = join(collect(update.childNodes, function(cdata) {
                            return cdata.data;
                        }), '');
                        var formStartTags = markup.match(/\<form[^\<]*\>/g);
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
                    try {
                        var viewStateElement = lookupViewStateElement(form);
                        if (viewStateElement.value != viewState) {
                            viewStateElement.value = viewState;
                        }
                    } catch (ex) {
                        appendHiddenInputElement(form, 'javax.faces.ViewState', viewState, viewState);
                        debug(logger, 'append missing "javax.faces.ViewState" input element to form["' + form.id + '"]');
                    }
                });
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

        function isAncestorOf(ancestor, element) {
            return ancestor == element || contains(parents(element), ancestor);
        }

        function findAndNotifyUpdatedElements(update) {
            var updatedElementId = update.getAttribute('id');
            if ("javax.faces.ViewState" === updatedElementId)  {
                return;
            }
            var fvsTail = updatedElementId.substr(updatedElementId.length - 13);
            if ("_fixviewstate" === fvsTail)  {
                return;
            }
            var updatedElement = lookupElementById(updatedElementId);
            if (updatedElement) {
                elementUpdateListeners = reject(elementUpdateListeners, function(idCallbackTuple) {
                    var id = idCallbackTuple.identifier;
                    var element = lookupElementById(id);
                    //test if inner element still exists, sometimes client side code can remove DOM fragments
                    if (element) {
                        var updated = contains(idCallbackTuple.ancestors, '*' + updatedElementId + '*');
                        if (updated) {
                            var callback = idCallbackTuple.handler;
                            try {
                                callback(element);
                            } catch (e) {
                                //ignore exception thrown in callback
                                //to make sure that the corresponding entry is removed from the list
                            }
                        }

                        return updated;
                    } else {
                        //confirm callback removal since the corresponding element is gone
                        return true;
                    }
                });
            }
        }
        // determine which elements are about to be removed by an update,
        // and clean them up while they're still in place
        namespace.onBeforeUpdate(function(updates) {
            each(updates.getElementsByTagName('update'), findAndNotifyUpdatedElements);
            each(updates.getElementsByTagName('delete'), findAndNotifyUpdatedElements);
        });

        //MyFaces uses a linked list of view state keys to track the changes in the view state -- the participating
        //forms need to have their view state key updated so that the next submit will work with the latest saved state
        //ICE-7188
        var formViewID;
        namespace.onBeforeSubmit(function(source) {
            formViewID = lookupNamedInputElement(formOf(source), 'ice.view').value;
        });
        namespace.onAfterUpdate(function(updates) {
            ifViewStateUpdated(updates, function(viewState) {
                //update only the forms that have the same viewID with the one used by the submitting form
                each(document.getElementsByTagName('form'), function(form) {
                    var viewIDElement = lookupNamedInputElement(form, 'ice.view');
                    var viewStateElement = lookupNamedInputElement(form, 'javax.faces.ViewState');
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

