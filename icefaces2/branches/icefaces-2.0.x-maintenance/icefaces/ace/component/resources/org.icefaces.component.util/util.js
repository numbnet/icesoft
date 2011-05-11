/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

function formOf(element) {
    var parent = element.parentNode;
    while (parent) {
        if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
        parent = parent.parentNode;
    }

    throw 'Cannot find enclosing form.';
}

if (!window['ice']) {
    window.ice = {};
}
if (!window['ice']['component_util']) {
    window.ice.component_util = {};
}

ice.component_util.isEventSourceInputElement = function(event) {
    var elem = ice.component_util.eventTarget(event);
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea') {
        return true;
    } else {
        return false;
    }
}

ice.component_util.eventTarget = function(event) {
       event = event || window.event;           
       return(event.target || event.srcElement);
}

ice.component_util.printArguments = function() {
    logger.info('-= Printing arguments =-');
    for(var i=0; i<arguments.length; i++) 
       logger.info(arguments[i]);
}

if (!window['ice']) {
    window.ice = {};
}
