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
    function formOf(element) {
        return element.form ? element.form : enclosingForm(element);
    }

    function standardFormSerialization(element) {
        return configurationOf(element).standardFormSerialization;
    }

    function serializeEventToOptions(event, element, options) {
        var collectingQuery = object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        });
        serializeOn($event(event, element), collectingQuery);
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
        var toRemove;
        try {
            var clonedElement;
            if (/MSIE/.test(navigator.userAgent)) {
                toRemove = form.appendChild(document.createElement('div'));
                toRemove.innerHTML = element.outerHTML;
                clonedElement = toRemove.firstChild;
            } else {
                toRemove = form.appendChild(element.cloneNode(true));
                clonedElement = toRemove;
            }

            event = event || null;
            var options = {execute: execute, render: render, onevent: requestCallback, 'ice.window': namespace.window, 'ice.view': viewID};
            serializeEventToOptions(event, element, options);
            serializeAdditionalParameters(additionalParameters, options);
            jsf.ajax.request(clonedElement, event, options);
        } finally {
            form.removeChild(toRemove);
        }
    }

    singleSubmitExecuteThis = function(event, element, additionalParameters) {
        if (standardFormSerialization(element)) {
            return fullSubmit('@this', '@all', event, element, additionalParameters);
        } else {
            return singleSubmit('@this', '@all', event, element, additionalParameters);
        }
    };

    singleSubmitExecuteThisRenderThis = function(event, element, additionalParameters) {
        if (standardFormSerialization(element)) {
            return fullSubmit('@this', '@this', event, element, additionalParameters);
        } else {
            return singleSubmit('@this', '@this', event, element, additionalParameters);
        }
    };

    var addPrefix = 'patch+';
    var removePrefix = 'patch-';

    function fullSubmit(execute, render, event, element, additionalParameters) {
        event = event || null;

        var viewID = viewIDOf(element);
        var options = {execute: execute, render: render, onevent: requestCallback, 'ice.window': namespace.window, 'ice.view': viewID};
        serializeEventToOptions(event, element, options);
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
        return fullSubmit('@all', '@all', event, element, additionalParameters);
    };
})();
