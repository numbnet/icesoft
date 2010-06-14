ice.component.checkbox = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	   var Dom = YAHOO.util.Dom;
       var buttonRoot = document.getElementById(clientId);
       
       if (YAHOO.widget.Logger){
	 	  YAHOO.widget.Logger.enableBrowserConsole();
       }
       
	   var button = new YAHOO.widget.Button(clientId, {type: jsProps.type});
YAHOO.log ("ClientId is="+clientId);	   
	   if(jsProps.label) {
			button.set('label', jsProps.label);
	    }
		
		if (jsfProps.value) {
			YAHOO.log("jsfProps.value="+jsfProps.value);
		}
		var singleSubmit=jsfProps.singleSubmit;	
		YAHOO.log ("jsProps.checked=-"+jsProps.checked);	
  		if(jsProps.checked) {	
   			button.set('checked', jsProps.checked);
 		}
	   
		var onCheckedChange = function (e) { 			
		    buttonNode = document.getElementById(clientId);
		    YAHOO.log(" buttonRoot="+buttonRoot+"  buttonNode="+buttonNode+" e.target="+e.target);
		    //e.target is null so have to set the target to the root of this button for submit

			//get the current value of checked
 	        var submittedValue =  e.newValue;	

YAHOO.log("in onCheckedChange and e.newValue="+e.newValue+" old="+e.prevValue);

			var params = function(parameter) {
				parameter(clientId+'_value', submittedValue);
			};
    
			var hiddenField = Dom.get(clientId+"_hidden");
			hiddenField.value=submittedValue;

	YAHOO.log(" hidden Field="+clientId+"_hidden"+" has value="+hiddenField.value);
			
	        if (singleSubmit) {
 	         	YAHOO.log("single submit goes to server");
 	         	e.target = buttonNode;	
                ice.se(e, buttonRoot, params); 
            } else {
             	YAHOO.log("single Submit is false doesn't go to server");
                ice.s(e, buttonRoot, params);                    
            }           
		};

		button.on("checkedChange", onCheckedChange);

		bindYUI(button);
	},
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {
		YAHOO.log("updateProperties for clientId="+clientId);
       ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }  
};
