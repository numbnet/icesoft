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

    function isAjaxDisabled(element) {
        var elementID = element.id;
        var formID = formOf(element).id;
        //lookup element that contains the list of elements that have AJAX submission disabled
        var disablingMarker = document.getElementById(formID + ":ajaxDisabled");
        return disablingMarker && contains(split(trim(disablingMarker.value), ' '), elementID);
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

    function encodedURLOf(form) {
        return form['javax.faces.encodedURL'] ? form['javax.faces.encodedURL'].value : form.action;
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
        var clonedElements = [];
        try {
            var clonedElement = form.appendChild(element.cloneNode(true));
            append(clonedElements, clonedElement);

            var tagName = toLowerCase(element.nodeName);
            //copy state which IE won't copy during cloning
            if (tagName == 'input') {
                if (element.type == 'radio') {
                    clonedElement.checked = element.checked;
                }
                if (element.type == 'checkbox') {
                    clonedElement.checked = element.checked;
                    //copy the rest of checkboxes with the same name and their state
                    var name = element.name;
                    each(element.form.elements, function(checkbox) {
                        if (checkbox.name == name && checkbox != element) {
                            var checkboxClone = form.appendChild(checkbox.cloneNode(true));
                            append(clonedElements, checkboxClone);
                            checkboxClone.checked = checkbox.checked;
                        }
                    });
                }
            } else if (tagName == 'select') {
                var clonedOptions = clonedElement.options;
                each(element.options, function(option, i) {
                    clonedOptions[i].selected = option.selected;
                });
            }

            event = event || null;
            var options = {execute: execute, render: render, onevent: requestCallback, 'ice.window': namespace.window, 'ice.view': viewID, 'ice.focus': currentFocus};
            var decoratedEvent = $event(event, element);

            if (isKeyEvent(decoratedEvent) && isEnterKey(decoratedEvent)) {
                cancelBubbling(decoratedEvent);
                cancelDefaultAction(decoratedEvent);
            }

            serializeEventToOptions(decoratedEvent, options);
            serializeAdditionalParameters(additionalParameters, options);

            debug(logger, join([
                'partial submit to ' + encodedURLOf(form),
                'javax.faces.execute: ' + execute,
                'javax.faces.render: ' + render,
                'javax.faces.source: ' + element.id,
                'view ID: ' + viewID,
                'event type: ' + type(decoratedEvent)
            ], '\n'));
            jsf.ajax.request(clonedElement, event, options);
        } finally {
            each(clonedElements, function(c) {
                form.removeChild(c);
            });
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
        if (isAjaxDisabled(element)) {
            var f = formOf(element);
            //use native submit function saved by namespace.captureSubmit
            if (f && f.nativeSubmit) {
                var fakeClick = document.createElement("input");
                fakeClick.setAttribute("type", "hidden");
                fakeClick.setAttribute("name", element.name);
                fakeClick.setAttribute("value", element.value);
                f.appendChild(fakeClick);
                f.nativeSubmit();
                f.removeChild(fakeClick);
            }
        } else {
            event = event || null;

            var viewID = viewIDOf(element);
            var options = {execute: execute, render: render, onevent: requestCallback, 'ice.window': namespace.window, 'ice.view': viewID, 'ice.focus': currentFocus};
            var decoratedEvent = $event(event, element);

            if (isKeyEvent(decoratedEvent) && isEnterKey(decoratedEvent)) {
                cancelBubbling(decoratedEvent);
                cancelDefaultAction(decoratedEvent);
            }

            serializeEventToOptions(decoratedEvent, options);
            serializeAdditionalParameters(additionalParameters, options);

            var form = formOf(element);
            var isDeltaSubmit = deltaSubmit(element);

            debug(logger, join([
                (isDeltaSubmit ? 'delta ' : '') + 'full submit to ' + encodedURLOf(form),
                'javax.faces.execute: ' + execute,
                'javax.faces.render: ' + render,
                'javax.faces.source: ' + element.id,
                'view ID: ' + viewID,
                'event type: ' + type(decoratedEvent)
            ], '\n'));

            if (isDeltaSubmit) {
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

                    jsf.ajax.request(deltaSubmitForm, event, options);
                } finally {
                    each(appendedElements, function(element) {
                        deltaSubmitForm.removeChild(element);
                    });
                }
            } else {
                jsf.ajax.request(element, event, options);
            }
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
