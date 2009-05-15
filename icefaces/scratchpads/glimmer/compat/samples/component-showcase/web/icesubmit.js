//fckeditor needs Ice namespace
window.Ice = {};

function iceSubmitPartial(form, component, evt) {
    ice.submitEvent(evt, component, form || formOf(component));
    return false;
}

function iceSubmit(form, component, evt) {
    ice.submitForm(evt, form || formOf(component));
    return false;
}

function formOf(element) {
    var parent = element.parentNode;
    while (parent) {
        if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
        parent = parent.parentNode;
    }

    throw 'Cannot find enclosing form.';
}

function setFocus() {
}