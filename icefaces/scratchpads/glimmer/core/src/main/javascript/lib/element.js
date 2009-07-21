function identifier(element) {
    return element.id;
}

function tag(element) {
    return toLowerCase(element.tagName);
}

function property(element, name) {
    return element[name];
}

function parents(element) {
    return Stream(function(cellConstructor) {
        function parentStream(e) {
            if (e == null || e == document) return null;
            return function() {
                return cellConstructor(e, parentStream(e.parentNode));
            };
        }

        return parentStream(element.parentNode);
    });
}

function enclosingForm(element) {
    return element.form || detect(parents(element), function(e) {
        return tag(e) == 'form';
    }, function() {
        throw 'cannot find enclosing form';
    });
}

function enclosingBridge(element) {
    return property(detect(parents(element), function(e) {
        return property(e, 'bridge') != null;
    }, function() {
        throw 'cannot find enclosing bridge';
    }), 'bridge');
}

function serializeElementOn(element, query) {
    if (tag(element) == 'a') {
        var name = element.name || element.id;
        if (name) addNameValue(query, name, name);
    }
}

function $elementWithID(id) {
    return document.getElementById(id);
}