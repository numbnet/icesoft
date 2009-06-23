var submitEvent;
var submitForm;

(function() {
    submitEvent = function(event, element, form) {
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
        serializeOn($event(event, element), object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        }));

        jsf.ajax.request(element, event, options);
    };

    submitForm = function(event, form) {
        //make event null if undefined
        event = event ? event : null;
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
        serializeOn($event(event, form), object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        }));
        jsf.ajax.request(form, event, options);
    };
})();