var singleSubmit;
var submit;
(function() {
    function serializeEventToOptions(event, element, options) {
        var collectingQuery = object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        });
        serializeOn($event(event, element), collectingQuery);
    }

    //set these variables only once
    var singleSubmitForm;
    var viewState;
    namespace.onLoad(function() {
        singleSubmitForm = document.createElement('form');
        singleSubmitForm.action = window.location.pathname;
        viewState = document.getElementById('javax.faces.ViewState').value;
    });

    singleSubmit = function (event, element) {
        var clonedElement = element.cloneNode(true);
        singleSubmitForm.appendChild(clonedElement);

        try {
            event = event || null;
            var options = {execute: clonedElement.id, render: '@all', 'ice.window': namespace.window, 'javax.faces.ViewState': viewState};
            serializeEventToOptions(event, element, options);
            jsf.ajax.request(clonedElement, event, options);
        } finally {
            singleSubmitForm.removeChild(clonedElement);
        }
    };

    submit = function (event, element, additionalParameters) {
        event = event || null;
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
        serializeEventToOptions(event, element, options);
        if (additionalParameters) {
            additionalParameters(function(name, value) {
                options[name] = value;
            });
        }
        jsf.ajax.request(element, event, options);
    };
})();