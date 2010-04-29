var singleSubmitExecuteThis;
var singleSubmitExecuteThisRenderThis;
var submit;
(function() {
    function formOf(element) {
        return element.form ? element.form : enclosingForm(element);
    }

    function viewIDOf(element) {
        return configurationOf(element).viewID;
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

    function singleSubmit(execute, render, event, element, additionalParameters) {
        var viewID = viewIDOf(element);
        var clonedElement = element.cloneNode(true);
        var form = document.getElementById(viewID);
        try {
            form.appendChild(clonedElement);

            event = event || null;
            var options = {execute: execute, render: render, 'ice.window': namespace.window, 'ice.view': viewID};
            serializeEventToOptions(event, element, options);
            serializeAdditionalParameters(additionalParameters, options);
            jsf.ajax.request(clonedElement, event, options);
        } finally {
            form.removeChild(clonedElement);
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
        var options = {execute: execute, render: render, 'ice.window': namespace.window, 'ice.view': viewID};
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
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = name;
                input.value = value;
                deltaSubmitForm.appendChild(input);
                append(appendedElements, input);
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
