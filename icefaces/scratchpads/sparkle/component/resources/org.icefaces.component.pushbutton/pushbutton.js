ice.component.pushbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
    if (YAHOO.widget.Logger){
	 	  YAHOO.widget.Logger.enableBrowserConsole();
     }
        //get the button html element
        var buttonRoot = document.getElementById(clientId);
    
		var button = new YAHOO.widget.Button(clientId,
				{label: jsProps.label, 
			      type: jsProps.type});

		
		if(jsProps.label) {
			button.set('label', jsProps.label);
		}

		if(jsProps.type) {
			button.set('button', jsProps.type);
		} 
		
		if (jsfProps.singleSubmit){
			button.set('singleSubmit', jsfProps.singleSubmit);
			YAHOO.log("set singleSubmit to "+jsfProps.singleSubmit);
		}
		
//		var singleSubmit=false;
//		if (jsfProps.singleSubmit){
//			YAHOO.log("jsProps.singleSubmit="+jsfProps.singleSubmit);
//			button.set('singleSubmit',jsfProps.singleSubmit);
//			YAHOO.log("set singleSubmit to "+singleSubmit); 
//		}
		

		var onClick = function (e) {
			YAHOO.log("in onClick function for e="+e+" with clientId="+clientId);
			buttonNode = document.getElementById(clientId);
YAHOO.log(" buttonRoot="+buttonRoot+"  buttonNode="+buttonNode+" e.target="+e.target);
   

            //button does not submit when using it by Node or root
            //as they are both type of SPAN rather than BUTTON
	        if (jsfProps.singleSubmit) {
	        	YAHOO.log(" single submit is true for clientId="+clientId);
                ice.se(e, buttonRoot); 
            } else {
            	YAHOO.log("single Submit is false for clientId="+clientId);
                ice.s(e, buttonRoot);                    
            }             
		};

		// the following lines didn't work alone..had to put the fn in the constructoras well
		button.on("click", onClick);

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
