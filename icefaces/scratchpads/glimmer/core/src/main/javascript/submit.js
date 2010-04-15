var singleSubmit;
var submit;
(function() {
    function formOf(element) {
        return element.form ? element.form : enclosingForm(element);
    }

    function viewIDOf(element) {
        return detect(parents(element), function(e) {
            return e.viewID;
        }).viewID;
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

    singleSubmit = function (event, element, additionalParameters) {
        var viewID = viewIDOf(element);
        var clonedElement = element.cloneNode(true);
        var form = document.getElementById(viewID).cloneNode(true);
        form.appendChild(clonedElement);

        event = event || null;
        var options = {execute: clonedElement.id, render: '@all', 'ice.window': namespace.window, 'ice.view': viewID};
        serializeEventToOptions(event, element, options);
        serializeAdditionalParameters(additionalParameters, options);
        jsf.ajax.request(clonedElement, event, options);
    };

    var addPrefix = 'patch+';
    var removePrefix = 'patch-';
    submit = function (event, element, additionalParameters) {
        event = event || null;

        var viewID = viewIDOf(element);
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window, 'ice.view': viewID};
        serializeEventToOptions(event, element, options);
        serializeAdditionalParameters(additionalParameters, options);

        if (namespace.configuration.deltaSubmit) {
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

            var deltaSubmitForm = document.getElementById(viewID).cloneNode(true);

            function createHiddenInputInDeltaSubmitForm(name, value) {
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = name;
                input.value = value;
                deltaSubmitForm.appendChild(input);
            }

            createHiddenInputInDeltaSubmitForm('ice.deltasubmit.form', form.id);
            createHiddenInputInDeltaSubmitForm(form.id, form.id);
            each(addedParameters, splitStringParameter(function(name, value) {
                createHiddenInputInDeltaSubmitForm(addPrefix + name, value);
            }));
            each(removedParameters, splitStringParameter(function(name, value) {
                createHiddenInputInDeltaSubmitForm(removePrefix + name, value);
            }));

            var clonedElement = element.cloneNode(true);
            deltaSubmitForm.appendChild(clonedElement);
            jsf.ajax.request(clonedElement, event, options);
        } else {
            jsf.ajax.request(element, event, options);
        }
    };
})();
