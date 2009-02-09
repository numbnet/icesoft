function operationNotSupported() {
    throw 'operation not supported';
}

function operator(defaultOperation) {
    return function() {
        var args = arguments;
        var instance = arguments[0];
        if (instance.instanceTag && instance.instanceTag == instance) {
            var method = instance(arguments.callee);
            if (method) {
                return method.apply(method, args);
            } else {
                operationNotSupported();
            }
        } else {
            return defaultOperation ? defaultOperation.apply(defaultOperation, args) : operationNotSupported();
        }
    }
}

function object(definition) {
    var operators = [];
    var methods = [];
    var unknown = null;
    definition(function(operator, method) {
        operators.push(operator);
        methods.push(method);
    }, function(method) {
        unknown = method;
    });
    //create the message dispatcher of the instance
    function self(operator) {
        var size = operators.length;
        for (var i = 0; i < size; i++) {
            if (operators[i] == operator) {
                return methods[i];
            }
        }

        return unknown;
    }
    //tag function with itself to differentiate from normal functions that don't do message dispatching
    return self.instanceTag = self;
}

function objectWithAncestors() {
    var definition = arguments[0];
    var args = arguments;
    var o = object(definition);
    function self(operator) {
        var method = o(operator);
        if (method) {
            return method;
        } else {
            var size = args.length;
            for (var i = 1; i < size; i++) {
                var ancestor = args[i];
                var overriddenMethod = ancestor(operator);
                if (overriddenMethod) {
                    return overriddenMethod;
                }
            }

            return null;
        }
    }
    //tag function with itself to differentiate from normal functions that don't do message dispatching
    return self.instanceTag = self;
}