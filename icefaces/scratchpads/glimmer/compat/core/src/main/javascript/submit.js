var iceSubmitPartial;
var iceSubmit;
var formOf;

(function() {
    function resetHiddenFieldsFor(aForm) {
        each(aForm.elements, function(formElement) {
            if (formElement.type == 'hidden' && formElement.id == '' &&
                formElement.name != 'javax.faces.ViewState' &&
                formElement.name != 'ice.window') {
                formElement.value = '';
            }
        });
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
    };
})();
