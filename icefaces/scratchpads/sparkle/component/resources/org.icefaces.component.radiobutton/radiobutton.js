ice.component.radiobutton = {
    initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
		var Dom = YAHOO.util.Dom;
		
		//if we had the form id, then we could do the following
		// YAHOO.util.Event.onContentReady(formId, function(){
		//....
		//}); for constructor and adding the listener to the button
		var obuttonGroup = new YAHOO.widget.ButtonGroup(clientId);

		if(yuiProps.label) {
			button.set('label', yuiProps.label);
		}

		if(yuiProps.type) {
			alert("button type is radio");
			button.set('radio', yuiProps.radio);
		}
	   
		var onSelectedChange = function (e) { 
			//set  target of event to the buttongroup component
			e.target = document.getElementById(clientId);
            YAHOO.log("in onSelectedChange for event="+event);
            if (e.prevValue){
               YAHOO.log("   preValue "+e.prevValue)
            }
	       //write hidden value

		//submit
	        if (singleSubmit) {
 	         	YAHOO.log("single submit goes to server");
 			    //e.target is null so have to set the target to the root of this button for submit
 	         	e.target = buttonNode;	
                ice.se(e, divNode, params); 
            } else {
             	YAHOO.log("single Submit is false update hidden fieldo on server");
                ice.s(e, divNode, params);                    
            } 
		};

		button.on("checkedButtonChange", onSelectedChange);

		logger.info('radiobutton group initialized');

		bindYUI(button);
	},
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, yuiProps, jsfProps, events) {
       ice.component.updateProperties(clientId, yuiProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }  
};
