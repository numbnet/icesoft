ice.component.commandlink = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	//setup Yahoo logger for commandlink class

	YAHOO.util.Event.onDOMReady(function() {
});

	var Dom = YAHOO.util.Dom;
	var oLinkButton = new YAHOO.widget.Button(clientId,{label: jsProps.label }, {type: jsProps.type});

//		button = new YAHOO.widget.Button(clientId,{label: jsProps.label }, {type: jsProps.type});

	// logger.log("params singleSubmit"+jsfProps.singleSubmit);
		
		if(jsProps.label) {
			oLinkButton.set('label', jsProps.label);
		}

		if(jsProps.type) {
			oLinkButton.set('button', jsProps.type);
		}
		
	  
	var onClick = function (e) {
		// logger.log("in onClick function for e="+e);
		buttonNode = document.getElementById(clientId);

	        if (jsfProps.singleSubmit) {
	        	YAHOO.log("not single submit");
                ice.se(e, e.target); 
            } else {
            	YAHOO.log("single Submit is false");
                ice.s(e, e.target);                    
            }             
		};

		// the following lines didn't work alone..had to put the fn in the constructoras well
		oLinkButton.on("click", onClick);
// 		oLinkButton.addListener("onclick", onButtonClick);

		bindYUI(oLinkButton);
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
