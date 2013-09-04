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
function resizeElementHeight(elementId) {
	var element = document.getElementById(elementId);
	var height = 0;
	var body = window.document.body;
	if (window.innerHeight) {
		height = window.innerHeight;
	} else if (body.parentElement.clientHeight) {
		height = body.parentElement.clientHeight;
	} else if (body) {
		if (body.clientHeight) {
			height = body.clientHeight;
		}
	}
	element.style.height = ((height - element.offsetTop) + "px");
}


function addResizeAfterUpdatesListener(elementId){

    // check caller to see if orientation changes are support and fall back
    // to window resize events otherwise
	var supportsOrientationChange = "onorientationchange" in window,
		orientationEvent = supportsOrientationChange ? "orientationchange" : "resize";
	
	var resizeHandler = function(updates) {
		resizeElementHeight(elementId);
	}

    // resize height on first load
    resizeElementHeight(elementId);

    // add DTD on after update listener, to resize after dom event has been applied
	ice.onAfterUpdate(resizeHandler);

    // apply resize on either orientation or window size change.
	window.addEventListener(orientationEvent, resizeHandler);
	
}
