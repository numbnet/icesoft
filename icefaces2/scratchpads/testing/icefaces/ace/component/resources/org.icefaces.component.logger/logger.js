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

ice.component.logger = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	//setup Yahoo logger for either global/page or particular component

	YAHOO.util.Event.onDOMReady(function() {
	    var calLogReader = new YAHOO.widget.LogReader(null, {newestOnTop:false});
	    calLogReader.setTitle("Yui Logger");
	    calLogReader.hideSource("global");
	    calLogReader.hideSource("LogReader");
});
	if (jsProps.debugElement){
		var logger = new YAHOO.widget.LogReader("jsfProps.debugElement");
	}
	else{
	  var logger = new YAHOO.widget.LogWriter("Page Console");
	}
	YAHOO.widget.Logger.enableBrowserConsole();

//		logger.log("params id"+clientId);

		
//		if(jsProps.label) {
//			button.set('label', jsProps.label);
//		}
//
//		if(jsProps.type) {
//			button.set('button', jsProps.type);
//		} 
//		
//		bindYUI(button);
	},
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {
       ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }
   
   
};
