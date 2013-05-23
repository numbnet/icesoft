/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

if (!window['ice']) {
    window.ice = {};
}
if (!window['ice']['ace']) {
    window.ice.ace = {};
}
if (!window['ice']['ace']['util']) {
    window.ice.ace.util = {};
}

/* Element Selection Utilities ************************************************/
ice.ace.util.formOf = function(element) {
    var parent = element.parentNode;
    while (parent) {
        if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
        parent = parent.parentNode;
    }

    throw 'Cannot find enclosing form.';
}
/******************************************************************************/



/* Element Insertion Utilities ************************************************/
ice.ace.util.insertElementAtIndex = function(parentElem, insertElem, index) {
    if (!parentElem.hasChildNodes()) {
        parentElem.appendChild(insertElem);
    } else {
        var afterElem = parentElem.childNodes[index];
        if (afterElem) {
            parentElem.insertBefore(insertElem, afterElem);
        } else {
            parentElem.appendChild(insertElem);
        }
    }
};
/******************************************************************************/



/* Event Utilities ************************************************************/
ice.ace.util.isEventSourceInputElement = function(event) {
    var elem = ice.ace.util.eventTarget(event);
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea' || tag == 'button') {
        return true;
    } else {
        return false;
    }
};

ice.ace.util.eventTarget = function(event) {
       event = event || window.event;           
       return(event.target || event.srcElement);
};
/******************************************************************************/



/* Array Utilities ************************************************************/
ice.ace.util.arrayIndexOf = function(arr, elem, fromIndex) {
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
ice.ace.util.arraysEqual = function(arr1, arr2) {
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
/******************************************************************************/



/* Dynamic Style Sheet Utilities **********************************************/
ice.ace.util.getStyleSheet = function (sheetId) {
    return Array.prototype.filter.call(document.styleSheets, function(s) {
        return s.title == sheetId;
    })[0];
};

ice.ace.util.addStyleSheet = function (sheetId, parentSelector) {
    var s = document.createElement('style');
    s.type = 'text/css';
    s.rel = 'stylesheet';
    s.title = sheetId;
    document.querySelectorAll(parentSelector || "head")[0].appendChild(s);
    return ice.mobi.getStyleSheet(sheetId);
};
/******************************************************************************/