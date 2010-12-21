function formOf(element) {
    var parent = element.parentNode;
    while (parent) {
        if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
        parent = parent.parentNode;
    }

    throw 'Cannot find enclosing form.';
}
if (!window['Ice'])
var Ice = {};
Ice.isEventSourceInputElement = function(event) {
    var elem = Ice.eventTarget(event);
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea') {
        return true;
    } else {
        return false;
    }
}

Ice.eventTarget = function(event) {
       event = event || window.event;           
       return(event.target || event.srcElement);
}

Ice.printArguments = function() {
    logger.info('-= Printing arguments =-');
    for(var i=0; i<arguments.length; i++) 
       logger.info(arguments[i]);
}

if (!window['ice']) {
    window.ice = {};
}
