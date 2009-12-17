//fckeditor needs Ice namespace
window.Ice = {};

var iceSubmitPartial;
var iceSubmit;
var formOf;
var setFocus;

(function() {
    function resetHiddenFieldsFor(aForm) {
        var elements = aForm.elements;
        var length = elements.length;
        for (var i = 0; i < length; i++) {
            var formElement = elements[i];
            if (formElement.type == 'hidden' && formElement.id == '') formElement.value = '';
        }
    }

    iceSubmitPartial = function(form, component, evt) {
        ice.submit(evt, component || form, function(parameter) {
            if (Ice.Menu != null && Ice.Menu.menuContext != null) {
                parameter('ice.menuContext', Ice.Menu.menuContext);
            }

            parameter('ice.submit.partial', true);
        });

        resetHiddenFieldsFor(form);
        return false;
    };

    iceSubmit = function(form, component, evt) {
        var code;
        if (evt.keyCode) code = evt.keyCode;
        else if (evt.which) code = evt.which;
        if (code > 3) {
            if (code != 13) {
                return false;
            }
        }
        ice.submit(evt, component || form, function(parameter) {
            if (Ice.Menu != null && Ice.Menu.menuContext != null) {
                parameter('ice.menuContext', Ice.Menu.menuContext);
            }

        });
        resetHiddenFieldsFor(form);

        return false;
    };

    formOf = function(element) {
        var parent = element.parentNode;
        while (parent) {
            if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
            parent = parent.parentNode;
        }

        throw 'Cannot find enclosing form.';
    }

    setFocus = noop;

    window.onLoad = ice.onLoad;
    window.onUnload = ice.onUnload;
    var noop = function() {
    };
    window.logger = {debug: noop, info: noop, warn: noop, error: noop, child: function() {
        return window.logger
    }};
})();
