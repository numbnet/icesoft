function submitEvent(event, element) {
    event = event || null;
    var options = {execute: '@all', render: '@all', 'ice.window': namespace.window};
    var collectingQuery = object(function(method) {
        method(addNameValue, function(self, name, value) {
            options[name] = value;
        });
    });
    serializeOn($event(event, element), collectingQuery);

    jsf.ajax.request(element, event, options);
}