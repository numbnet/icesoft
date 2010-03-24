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

Ice.simulateFocus = function(ele, anc) {
    if(!document.all) {
        anc.style.visibility='hidden';
    } 
    anc.style.borderStyle='none';
    anc.style.outlineStyle='none'; 
    anc.style.borderWidth='0px';
    anc.style.outlineWidth='0px'; 
    anc.style.margin='0px'; 
    if (ele == null) return; 
    ele['_borderStyle'] = ele.style.borderStyle;     
    ele.style.borderStyle='dotted';
    ele['_borderWidth'] = ele.style.borderWidth;   
    ele.style.borderWidth='1px 1px 1px 1px';
    ele['_borderColor'] = ele.style.borderColor;   
    ele.style.borderColor = 'black';    
}

Ice.simulateBlur = function(ele, anc) {
    if(!document.all) {    
        anc.style.visibility='visible';
    } 
    if (ele == null) return; 
    ele.style.borderStyle = ele['_borderStyle'];
    ele.style.borderWidth = ele['_borderWidth'];  
    ele.style.borderColor = ele['_borderColor'];   
}