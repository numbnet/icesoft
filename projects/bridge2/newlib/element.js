var id = operator();
var tag = operator();
var property = operator();
var attribute = operator();
var parents = operator();
var enclosingForm = operator();
var enclosingBridge = operator();

function Element(element) {
    return object(function(method) {
        method(id, function(self) {
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
                    if (e == null || e == document) return;
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
            return detect(parents(self), function(e) {
                return property(e, 'bridge') != null;
            }, function() {
                throw 'cannot find enclosing bridge';
            });
        });

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
                return $element(f);
            } else {
                throw 'cannot find enclosing form';
            }
        });
    }, Element(element));
}

function $element(e) {
    //no polymophism here...'switch' is the way then.
    switch (e.tagName.toLowerCase()) {
        case 'textarea':
        case 'input': return InputElement(e);
        //            case 'th':
        //            case 'td':
        //            case 'tr': return TableCellElement(e);
        //            case 'button': return ButtonElement(e);
        //            case 'select': return SelectElement(e);
        //            case 'form': return FormElement(e);
        //            case 'body': return BodyElement(e);
        //            case 'script': return ScriptElement(e);
        //            case 'title': return TitleElement(e);
        //            case 'a': return AnchorElement(e);
        //            case 'iframe': return IFrameElement(e);
        default : return Element(e);
    }
}

function $elementWithID(id) {
    return $element(document.getElementById(id));
}