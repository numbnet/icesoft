var submitEvent;
var submitForm;

(function() {
    submitEvent = function(event, element) {
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
        var collectingQuery = object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        });
        each([$event(event, element), $element(element)], function(e) {
            serializeOn(e, collectingQuery);
        });

        jsf.ajax.request(element, event, options);
    };

    submitForm = function(event, form) {
        //make event null if undefined
        event = event ? event : null;
        var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
        var collectingQuery = object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        });
        serializeOn($event(event, form), collectingQuery);

        jsf.ajax.request(form, event, options);
    };
})();