function replaceDiv(divID, content) {
	var container = document.getElementById(divID);
	container.innerHTML = content;
}

var windowCount=0;
function countWindow(divID) {
	windowCount++;
	replaceDiv(divID, windowCount);
}

var sessionCount=0;
function countSession(divID) {
	sessionCount++;
	replaceDiv(divID, sessionCount);
}

var applicationCount=0;
function countApplication(divID) {
	applicationCount++;
	replaceDiv(divID, applicationCount);
}

