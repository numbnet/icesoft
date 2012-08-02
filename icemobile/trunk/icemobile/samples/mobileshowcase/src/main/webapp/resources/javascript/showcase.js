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
