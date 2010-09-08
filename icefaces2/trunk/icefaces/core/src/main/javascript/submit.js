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

var singleSubmitExecuteThis;
var singleSubmitExecuteThisRenderThis;
var submit;
(function() {
    function idOrElement(e) {
        return isString(e) ? document.getElementById(e) : e;
    }

    function formOf(element) {
        return toLowerCase(element.nodeName) == 'form' ? element : enclosingForm(element);
    }

    function standardFormSerialization(element) {
        return configurationOf(element).standardFormSerialization;
    }

    function serializeEventToOptions(event, options) {
        var collectingQuery = object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        });
        serializeOn(event, collectingQuery);
    }

    function serializeAdditionalParameters(additionalParameters, options) {
        if (additionalParameters) {
            additionalParameters(function(name, value) {
                options[name] = value;
            });
        }
    }

    var submitSendListeners = [];
    namespace.onSubmitSend = function(callback) {
        append(submitSendListeners, callback);
    };

    var submitResponseListeners = [];
    namespace.onSubmitResponse = function(callback) {
        append(submitResponseListeners, callback);
    };

    function requestCallback(e) {
        switch (e.status) {
            case 'begin':
                broadcast(submitSendListeners);
                break;
            case 'complete':
                broadcast(submitResponseListeners, [ e.responseCode, e.responseText, e.responseXML ]);
                break;
        }
    }

    function singleSubmit(execute, render, event, element, additionalParameters) {
        var viewID = viewIDOf(element);
        var form = document.getElementById(viewID);
        var elementParent = element.parentNode;
        //create a place marker
        var marker = document.createComment('');
        try {
            //mark the place where element is
            elementParent.insertBefore(marker, element);
            var restoreState;
            //restoring the state of input[checkbox/radio] is necessary only for IE6
            if (toLowerCase(element.nodeName) == 'input' && (element.type == 'checkbox' || element.type == 'radio')) {
                var check = element.checked;
                restoreState = function(e) {
                    e.checked = check;
                };
            } else {
                restoreState = noop;
            }
            //move element from its original place into the single submit form
            form.appendChild(element);
            //restore the state of element for IE6
            restoreState(element);

            event = event || null;
            var options = {execute: execute, render: render, onevent: requestCallback, 'ice.window': namespace.window, 'ice.view': viewID};
            var decoratedEvent = $event(event, element);

            cancelBubbling(decoratedEvent);
            cancelDefaultAction(decoratedEvent);

            serializeEventToOptions(decoratedEvent, options);
            serializeAdditionalParameters(additionalParameters, options);
            jsf.ajax.request(element, event, options);
        } finally {
            //move back the element from the single submit form to the previously marked place
            form.removeChild(element);
            elementParent.insertBefore(element, marker);
            //remove marker
            elementParent.removeChild(marker);
        }
    }

    singleSubmitExecuteThis = function(event, idorelement, additionalParameters) {
        var element = idOrElement(idorelement);
        if (standardFormSerialization(element)) {
            return fullSubmit('@this', '@all', event, element, function(p) {
                p('ice.submit.type', 'ice.se');
                p('ice.submit.serialization', 'form');
                if (additionalParameters) additionalParameters(p);
            });
        } else {
            return singleSubmit('@this', '@all', event, element, function(p) {
                p('ice.submit.type', 'ice.se');
                p('ice.submit.serialization', 'element');
                if (additionalParameters) additionalParameters(p);
            });
        }
    };

    singleSubmitExecuteThisRenderThis = function(event, idorelement, additionalParameters) {
        var element = idOrElement(idorelement);
        if (standardFormSerialization(element)) {
            return fullSubmit('@this', '@this', event, element, function(p) {
                p('ice.submit.type', 'ice.ser');
                p('ice.submit.serialization', 'form');
                if (additionalParameters) additionalParameters(p);
            });
        } else {
            return singleSubmit('@this', '@this', event, element, function(p) {
                p('ice.submit.type', 'ice.ser');
                p('ice.submit.serialization', 'element');
                if (additionalParameters) additionalParameters(p);
            });
        }
    };

    var addPrefix = 'patch+';
    var removePrefix = 'patch-';

    function fullSubmit(execute, render, event, element, additionalParameters) {
        event = event || null;

        var disabled = document.getElementById(element.id + ":ajaxDisabled");
        if (disabled) {
            var disabledArray = disabled.value.split(" ");
            var l = disabledArray.length;
            for (var i = 1; i < l; i++) {
                var name = disabledArray[i];
                var field = element[name];
                if ((field) && (field.value == name ) && (element.nativeSubmit)) {
                    element.nativeSubmit();
                    return;
                }
            }
        }

        var viewID = viewIDOf(element);
        var options = {execute: execute, render: render, onevent: requestCallback, 'ice.window': namespace.window, 'ice.view': viewID};
        var decoratedEvent = $event(event, element);

        cancelBubbling(decoratedEvent);
        cancelDefaultAction(decoratedEvent);

        serializeEventToOptions(decoratedEvent, options);
        serializeAdditionalParameters(additionalParameters, options);

        if (deltaSubmit(element)) {
            var form = formOf(element);
            var previousParameters = form.previousParameters || HashSet();
            var currentParameters = HashSet(jsf.getViewState(form).split('&'));
            var addedParameters = complement(currentParameters, previousParameters);
            var removedParameters = complement(previousParameters, currentParameters);
            form.previousParameters = currentParameters;
            function splitStringParameter(f) {
                return function(p) {
                    var parameter = split(p, '=');
                    f(decodeURIComponent(parameter[0]), decodeURIComponent(parameter[1]));
                };
            }

            var deltaSubmitForm = document.getElementById(viewID);
            var clonedElement = element.cloneNode(true);
            var appendedElements = [];

            function createHiddenInputInDeltaSubmitForm(name, value) {
                append(appendedElements, appendHiddenInputElement(deltaSubmitForm, name, value));
            }

            try {
                createHiddenInputInDeltaSubmitForm('ice.deltasubmit.form', form.id);
                createHiddenInputInDeltaSubmitForm(form.id, form.id);
                each(addedParameters, splitStringParameter(function(name, value) {
                    createHiddenInputInDeltaSubmitForm(addPrefix + name, value);
                }));
                each(removedParameters, splitStringParameter(function(name, value) {
                    createHiddenInputInDeltaSubmitForm(removePrefix + name, value);
                }));

                deltaSubmitForm.appendChild(clonedElement);
                jsf.ajax.request(clonedElement, event, options);
            } finally {
                each(appendedElements, function(element) {
                    deltaSubmitForm.removeChild(element);
                });
                deltaSubmitForm.removeChild(clonedElement);
            }
        } else {
            jsf.ajax.request(element, event, options);
        }
    }

    submit = function(event, element, additionalParameters) {
        return fullSubmit('@all', '@all', event, idOrElement(element), function(p) {
            p('ice.submit.type', 'ice.s');
            p('ice.submit.serialization', 'form');
            if (additionalParameters) additionalParameters(p);
        });
    };
})();
