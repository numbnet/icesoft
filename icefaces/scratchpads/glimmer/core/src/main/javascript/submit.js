var submitEvent;
var submitForm;

(function() {
    function addInputHidden(form, name, value) {
        var i = document.createElement('input');
        i.setAttribute('name', name);
        i.setAttribute('value', value);
        i.setAttribute('type', 'hidden');
        return form.appendChild(i);
    }

    submitEvent = function(event, element, form) {
        var cleanupElements = [];
        serializeOn($event(event, element), object(function(method) {
            method(addNameValue, function(self, name, value) {
                var i = addInputHidden(form, name, value);
                append(cleanupElements, function() {
                    form.removeChild(i);
                });
            });
        }));

        jsf.ajax.request(element, event, {execute: '@all', render: '@all'});
        broadcast(cleanupElements);
    };

    submitForm = function(event, form) {
        //make event null if undefined
        event = event ? event : null;
        var cleanupElements = [];
        serializeOn($event(event, form), object(function(method) {
            method(addNameValue, function(self, name, value) {
                var i = addInputHidden(form, name, value);
                append(cleanupElements, function() {
                    form.removeChild(i);
                });
            });
        }));
        jsf.ajax.request(form, event, {execute: '@all', render: '@all'});
        broadcast(cleanupElements);
    };
})();