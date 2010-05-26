ice.component.pushbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	//setup Yahoo logger for pushbutton class

	YAHOO.util.Event.onDOMReady(function() {
	    var calLogReader = new YAHOO.widget.LogReader(null, {newestOnTop:false});
	    calLogReader.setTitle("Button Logger");
	    calLogReader.hideSource("global");
	    calLogReader.hideSource("LogReader");
});
	var logger = new YAHOO.widget.LogWriter("pushId 2");
	
		var Dom = YAHOO.util.Dom;
		var button = new YAHOO.widget.Button(clientId,{label: jsProps.label }, {type: jsProps.type});


		logger.log("params singleSubmit"+jsfProps.singleSubmit);
		
		if(jsProps.label) {
			button.set('label', jsProps.label);
		}

		if(jsProps.type) {
			button.set('button', jsProps.type);
		} 
		
	  
		var onClick = function (e) {
			logger.log("in onClick function for e="+e);
			buttonNode = document.getElementById(clientId);

	        if (jsfProps.singleSubmit) {
	        	logger.log("not single submit");
                ice.se(e, e.target); 
            } else {
            	logger.log("single Submit is false");
                ice.s(e, e.target);                    
            }             
		};

		// the following lines didn't work alone..had to put the fn in the constructoras well
		button.on("click", onClick);
// 		button.addListener("onclick", onButtonClick);

		bindYUI(button);
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
