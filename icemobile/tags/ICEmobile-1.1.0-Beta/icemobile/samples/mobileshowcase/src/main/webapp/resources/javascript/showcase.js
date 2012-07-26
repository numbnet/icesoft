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
	
	var supportsOrientationChange = "onorientationchange" in window,
		orientationEvent = supportsOrientationChange ? "orientationchange" : "resize";
	
	var resizeHandler = function(updates) {
		resizeElementHeight(elementId);
	}
	
	ice.onAfterUpdate(resizeHandler);

	window.addEventListener(orientationEvent, resizeHandler);
	
}
