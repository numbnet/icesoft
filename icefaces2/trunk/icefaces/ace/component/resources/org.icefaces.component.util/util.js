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
};

ice.component_util.eventTarget = function(event) {
       event = event || window.event;           
       return(event.target || event.srcElement);
};

ice.component_util.printArguments = function() {
    logger.info('-= Printing arguments =-');
    for(var i=0; i<arguments.length; i++) 
       logger.info(arguments[i]);
};

ice.component_util.insertElementAtIndex = function(parentElem, insertElem, index) {
	if (!parentElem.hasChildNodes()) {
		parentElem.appendChild(insertElem);
	}
	else {
		var afterElem = parentElem.childNodes[index];
		parentElem.insertBefore(insertElem, afterElem);
	}
};

ice.component_util.arrayIndexOf = function(arr, elem, fromIndex) {
	if (arr.indexOf) {
		return arr.indexOf(elem, fromIndex);
	}
	var len = arr.length;
	if (fromIndex == null) {
		fromIndex = 0;
	} else if (fromIndex < 0) {
		fromIndex = Math.max(0, len + fromIndex);
	}
	for (var i = fromIndex; i < len; i++) {
		if (arr[i] === elem) {
			return i;
		}
	}
	return -1;
};

// One level deep comparison, not deep recursive
ice.component_util.arraysEqual = function(arr1, arr2) {
    if (!arr1 && !arr2) {
        return true;
    }
    else if (!arr1 || !arr2) {
        return false;
    }
    var len1 = arr1.length;
    var len2 = arr2.length;
    if (len1 != len2) {
        return false;
    }
    for (var i = 0; i < len1; i++) {
        if (arr1[i] !== arr2[i]) {
            return false;
        }
    }
    return true;
};

if (!window['ice']) {
    window.ice = {};
}
