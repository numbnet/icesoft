/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

/*******************************************************************************
 * HubTest-BasicConformance.js:
 *		JavaScript for test case HubTest-BasicConformance.html.
 *
 *		This JavaScript MUST NOT BE CHANGED.
 *
 * Copyright 2007 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

var checkPrefix = HubTest_BasicConformance_MyPrefix;
var publishSubscribeWorking = false;
var markupScannerWorking = false;

function TestWasSuccessful(idstring) {
	var elem = document.getElementById(idstring);
	elem.innerHTML = '<span style="color:green">TEST SUCCEEDED!!!</span>';
}

function subscribeTestCB(prefix, name, subscriberData, publisherData) {
	publishSubscribeWorking = true;
}

/* This function updates the HTML DOM based on whether the various test succeeded.
	It is invoked when the document 'load' event is raised. */
function MyLoadHandler() {
	var elem = document.getElementById("LibraryName");
	elem.innerHTML = '<span style="color:green">For library: '+checkPrefix+'</span>';
	if (OpenAjax.queryLibrary(checkPrefix)) {
		TestWasSuccessful("registerLibraryResult");
	}
	if (OpenAjax.getGlobals(checkPrefix)) {
		TestWasSuccessful("registerGlobalsResult");
	}
	if (HubTest_BasicConformance_Verify_Load()) {
		TestWasSuccessful("addOnLoadResult");
	}
	OpenAjax.subscribe("foo","bar",subscribeTestCB);
	OpenAjax.publish("foo","bar");
	if (publishSubscribeWorking) {
		TestWasSuccessful("PublishSubscribeResult");
	}
	if (markupScannerWorking) {
		TestWasSuccessful("MarkupScannerResult");
	}
}
OpenAjax.addOnLoad("MyLoadHandler");

/* This logic verifies that the markup scanner is working */
function markupScannerCB(element) {
	markupScannerWorking = true;
}
OpenAjax.registerAttrScanCB("foo", "class", "match", "HubTestResult", markupScannerCB);


