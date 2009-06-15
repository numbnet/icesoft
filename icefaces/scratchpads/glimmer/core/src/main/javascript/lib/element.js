//element methods
var identifier = operator();
var tag = operator();
var property = operator();
var parents = operator();
var enclosingForm = operator();
var enclosingBridge = operator();

var $element;
var $elementWithID;

(function() {
    function DefaultElement(element) {
        return object(function(method) {
            method(identifier, function(self) {
                return element.id;
            });

            method(tag, function(self) {
                return toLowerCase(element.tagName);
            });

            method(property, function(self, name) {
                return element[name];
            });

            method(parents, function(self) {
                return Stream(function(cellConstructor) {
                    function parentStream(e) {
                        if (e == null || e == document) return null;
                        return function() {
                            return cellConstructor($element(e), parentStream(e.parentNode));
                        };
                    }

                    return parentStream(element.parentNode);
                });
            });

            method(enclosingForm, function(self) {
                return detect(parents(self), function(e) {
                    return tag(e) == 'form';
                }, function() {
                    throw 'cannot find enclosing form';
                });
            });

            method(enclosingBridge, function(self) {
                return property(detect(parents(self), function(e) {
                    return property(e, 'bridge') != null;
                }, function() {
                    throw 'cannot find enclosing bridge';
                }), 'bridge');
            });

            method(serializeOn, noop);

            method(asString, function(self) {
                return asString(element);
            });
        });
    }

    function InputElement(element) {
        return objectWithAncestors(function(method) {
            method(enclosingForm, function(self) {
                var f = element.form;
                if (f) {
                    return FormElement(f);
                } else {
                    throw 'cannot find enclosing form';
                }
            });

            method(serializeOn, function(self, query) {
                switch (toLowerCase(element.type)) {
                    case 'image':
                    case 'textarea':
                    case 'submit':
                    case 'hidden':
                    case 'password':
                    case 'text': addNameValue(query, element.name, element.value); break;
                    case 'checkbox':
                    case 'radio': if (element.checked) addNameValue(query, element.name, element.value || 'on'); break;
                }
            });
        }, DefaultElement(element));
    }

    function SelectElement(element) {
        return objectWithAncestors(function(method) {
            method(serializeOn, function(self, query) {
                each(select(element.options, function(option) {
                    return option.selected;
                }), function(selectedOption) {
                    var value = selectedOption.value || (selectedOption.value == '' ? '' : selectedOption.text);
                    addNameValue(query, element.name, value);
                });
            });
        }, InputElement(element));
    }

    function ButtonElement(element) {
        return objectWithAncestors(function(method) {
            method(serializeOn, function(self, query) {
                addNameValue(query, element.name, element.value);
            });
        }, InputElement(element));
    }

    function AnchorElement(element) {
        return objectWithAncestors(function(method) {
            method(serializeOn, function(self, query) {
                var name = element.name;
                if (name) addNameValue(query, name, name);
            });
        }, DefaultElement(element));
    }

    function FormElement(element) {
        return objectWithAncestors(function(method) {
            method(enclosingForm, function(self) {
                return self;
            });

            method(serializeOn, function(self, query) {
                each(collect(element.elements, $element), function(e) {
                    serializeOn(e, query);
                });
            });
        }, DefaultElement(element));
    }

    $element = function(e) {
        if (!e) return null;
        //no polymophism here...'switch' is the way then.
        switch (toLowerCase(e.tagName)) {
            case 'textarea':
            case 'input': return InputElement(e);
            case 'th':
            case 'td':
            case 'button': return ButtonElement(e);
            case 'select': return SelectElement(e);
            case 'form': return FormElement(e);
            case 'a': return AnchorElement(e);
            default : return DefaultElement(e);
        }
    };

    $elementWithID = function(id) {
        return $element(document.getElementById(id));
    };
})();