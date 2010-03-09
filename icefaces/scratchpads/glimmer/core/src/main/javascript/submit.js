var singleSubmit;
var submit;
(function() {
    function formOf(element) {
        if (element.form) {
            return element.form;
        } else {
            var parent = element.parentNode;
            while (parent) {
                if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
                parent = parent.parentNode;
            }

            throw 'Cannot find enclosing form.';
        }
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
    var viewState;
    onLoad(window, function() {
        singleSubmitForm = document.createElement('form');
        singleSubmitForm.action = window.location.pathname;
        singleSubmitForm.id = 'void';
        viewState = document.getElementById('javax.faces.ViewState').value;
    });

    singleSubmit = function (event, element, additionalParameters) {
        var clonedElement = element.cloneNode(true);
        singleSubmitForm.appendChild(clonedElement);

        try {
            event = event || null;
            var options = {execute: clonedElement.id, render: '@all', 'ice.window': namespace.window, 'javax.faces.ViewState': viewState};
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
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
        serializeEventToOptions(event, element, options);
        serializeAdditionalParameters(additionalParameters, options);

        if (namespace.configuration.deltaSubmit) {
            var form = formOf(element);
            var previousParameters = form.previousParameters || HashSet();
            var currentParameters = HashSet(jsf.getViewState(form).split('&'));
            var addedParameters = complement(currentParameters, previousParameters);
            var removedParameters = complement(previousParameters, currentParameters);
            form.previousParameters = currentParameters;

            each(addedParameters, function(p) {
                var parameter = p.split('=');
                var input = singleSubmitForm.appendChild(document.createElement('input'));
                input.type = 'hidden';
                input.name = addPrefix + decodeURIComponent(parameter[0]);
                input.value = parameter[1];
            });
            each(removedParameters, function(p) {
                var parameter = p.split('=');
                var input = singleSubmitForm.appendChild(document.createElement('input'));
                input.type = 'hidden';
                input.name = removePrefix + decodeURIComponent(parameter[0]);
                input.value = parameter[1];
            });

            options[form.id] = form.id;
            options['javax.faces.ViewState'] = viewState;
            options['ice.deltasubmit.form'] = form.id;

            var clonedElement = element.cloneNode(true);
            singleSubmitForm.appendChild(clonedElement);
            jsf.ajax.request(clonedElement, event, options);
            singleSubmitForm.innerHTML = '';
        } else {
            jsf.ajax.request(element, event, options);
        }
    };
})();
