var attributeAsString = operator();
var attributeAsBoolean = operator();
var attributeAsNumber = operator();
var valueAsStrings = operator();
var valueAsBooleans = operator();
var valueAsNumbers = operator();
var attributeNames = operator();
var childConfiguration = operator();

function XMLDynamicConfiguration(lookupElement) {
    function asBoolean(s) {
        return 'true' == toLowerCase(s);
    }

    function lookupAttribute(name) {
        var a = lookupElement().getAttribute(name);
        if (a) {
            return a;
        } else {
            throw 'unknown attribute: ' + name;
        }
    }

    function lookupValues(name) {
        return collect(asArray(lookupElement().getElementsByTagName(name)), function(e) {
            var valueNode = e.firstChild;
            return valueNode ? valueNode.nodeValue : '';
        });
    }

    return object(function(method) {
        method(attributeAsString, function(self, name, defaultValue) {
            try {
                return lookupAttribute(name);
            } catch (e) {
                if (isString(defaultValue)) {
                    return defaultValue;
                } else {
                    throw e;
                }
            }
        });

        method(attributeAsNumber, function(self, name, defaultValue) {
            try {
                return Number(lookupAttribute(name));
            } catch (e) {
                if (isNumber(defaultValue)) {
                    return defaultValue;
                } else {
                    throw e;
                }
            }
        });

        method(attributeAsBoolean, function(self, name, defaultValue) {
            try {
                return asBoolean(lookupAttribute(name));
            } catch (e) {
                if (isBoolean(defaultValue)) {
                    return defaultValue;
                } else {
                    throw e;
                }
            }
        });

        method(childConfiguration, function(self, name) {
            var elements = lookupElement().getElementsByTagName(name);
            if (isEmpty(elements)) {
                throw 'unknown configuration: ' + name;
            } else {
                return XMLDynamicConfiguration(function() {
                    return lookupElement().getElementsByTagName(name)[0];
                });
            }
        });

        method(valueAsStrings, function(self, name, defaultValues) {
            var values = lookupValues(name);
            return isEmpty(values) && defaultValues ? defaultValues : values;
        });

        method(valueAsNumbers, function(self, name, defaultValues) {
            var values = lookupValues(name);
            return isEmpty(values) && defaultValues ? defaultValues : collect(values, Number);
        });

        method(valueAsBooleans, function(self, name, defaultValues) {
            var values = lookupValues(name);
            return isEmpty(values) && defaultValues ? defaultValues : collect(values, asBoolean);
        });
    });
}
