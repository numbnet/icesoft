/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
ice.ace.DataExporters = {};

ice.ace.DataExporter = function(id, behaviors) {
	this.id = id;
	ice.ace.DataExporters[this.id] = this;
	if (ice.ace.DataExporter.shouldOpenPopUp()) {
		this.window = window.open('','','width=400,height=150');
		this.window.document.write('<html><head><title>Data export file loader</title></head><body id="body"></body></html>');
		this.body = this.window.document.getElementById('body');
		this.body.innerHTML = '<p>Please wait while the file you requested is generated...</p>';
		this.window.focus();
	}
	behaviors();
}
ice.ace.DataExporter.prototype.url = function(url) {
	if (ice.ace.DataExporter.shouldOpenPopUp()) {
		this.body.innerHTML = this.body.innerHTML + '<p>The file is ready. Click link below to download.</p>'
			+ '<a href="' + url + '">Download File</a>';
		this.window.focus();
	} else {
		var iframe = document.createElement('iframe');
		iframe.setAttribute('src', url);
		iframe.style.display = 'none';
		document.body.appendChild(iframe);
	}
	ice.ace.DataExporters[this.id] = null;
}
ice.ace.DataExporter.shouldOpenPopUp = function() {
	if (ice.ace.jq.browser.msie) 
		if (navigator.userAgent.indexOf("Trident/5") < 0) // detects IE9
			return true;
	return false;
}