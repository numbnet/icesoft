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
if (!window['ice']['ace']) {
    window.ice.ace = {};
}
if (!window['ice']['ace']['util']) {
    window.ice.ace.util = {};
}

ice.ace.util.isEventSourceInputElement = function(event) {
    var elem = ice.ace.util.eventTarget(event);
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea' || tag == 'button') {
        return true;
    } else {
        return false;
    }
};

ice.ace.util.isMouseOver = function(elem, jqEvent) {
    elem = ice.ace.jq(elem);

    var offset = elem.offset(),
        xMin = offset.left,
        xMax = xMin + elem.outerWidth(),
        yMin = offset.top,
        yMax = yMin + elem.outerHeight(),
        mouseY = jqEvent.pageY,
        mouseX = jqEvent.pageX;

    return (mouseY < yMax && mouseY > yMin && mouseX < xMax && mouseX > xMin);
}

ice.ace.util.eventTarget = function(event) {
       event = event || window.event;           
       return(event.target || event.srcElement);
};


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

ice.ace.util.createHiddenField = function(parent, id) {
    var inp = document.createElement("input");
    inp.setAttribute('type', 'hidden');
    inp.setAttribute('id', id);
    inp.setAttribute('name', id);
    parent.appendChild(inp);
    return inp;
};

ice.ace.util.createElement = function(parent, name) {
    var element = document.createElement(name);
    parent.appendChild(element);
    return element;
};

ice.ace.util.removeElement = function(element) {
    element.parentNode.removeChild(element);
};

ice.ace.onContentReady = function(id, fn) {
    YAHOO.util.Event.onContentReady(id, fn, window, true);
};
