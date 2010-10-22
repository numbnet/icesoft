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
