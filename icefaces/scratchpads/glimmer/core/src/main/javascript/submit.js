var singleSubmit;
var submit;
(function() {
    function formOf(element) {
        return element.form ? element.form : enclosingForm(element);
    }

    function viewStateOf(element) {
        return detect(parents(element), function(e) {
            return e.javax_faces_ViewState;
        }).javax_faces_ViewState;
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

    //set these variables only once
    var singleSubmitForm;
    onLoad(window, function() {
        singleSubmitForm = document.createElement('form');
        singleSubmitForm.action = window.location.pathname;
        singleSubmitForm.id = 'void';
    });

    singleSubmit = function (event, element, additionalParameters) {
        var clonedElement = element.cloneNode(true);
        singleSubmitForm.appendChild(clonedElement);

        try {
            event = event || null;
            var options = {execute: clonedElement.id, render: '@all', 'ice.window': namespace.window, 'javax.faces.ViewState': viewStateOf(element)};
            serializeEventToOptions(event, element, options);
            serializeAdditionalParameters(additionalParameters, options);
            jsf.ajax.request(clonedElement, event, options);
        } finally {
            singleSubmitForm.removeChild(clonedElement);
        }
    };

    var addPrefix = 'patch+';
    var removePrefix = 'patch-';
    submit = function (event, element, additionalParameters) {
        event = event || null;

        //do not use view state included in the form
        var form = formOf(element);
        var viewStateHiddenInput = form['javax.faces.ViewState'];
        if (viewStateHiddenInput) {
            viewStateHiddenInput.parentNode.removeChild(viewStateHiddenInput);
        }

        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window, 'javax.faces.ViewState': viewStateOf(element)};
        serializeEventToOptions(event, element, options);
        serializeAdditionalParameters(additionalParameters, options);

        if (namespace.configuration.deltaSubmit) {
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

            function createHiddenInputInSingleSubmitForm(name, value) {
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = name;
                input.value = value;
                singleSubmitForm.appendChild(input);
            }

            createHiddenInputInSingleSubmitForm('ice.deltasubmit.form', form.id);
            createHiddenInputInSingleSubmitForm(form.id, form.id);
            each(addedParameters, splitStringParameter(function(name, value) {
                createHiddenInputInSingleSubmitForm(addPrefix + name, value);
            }));
            each(removedParameters, splitStringParameter(function(name, value) {
                createHiddenInputInSingleSubmitForm(removePrefix + name, value);
            }));
            each(form.elements, function(e) {
                if (e.name && e.name == 'javax.faces.ViewState') {
                    createHiddenInputInSingleSubmitForm(e.name, e.value);
                }
            });

            var clonedElement = element.cloneNode(true);
            singleSubmitForm.appendChild(clonedElement);
            jsf.ajax.request(clonedElement, event, options);
            singleSubmitForm.innerHTML = '';
        } else {
            jsf.ajax.request(element, event, options);
        }
    };
})();
