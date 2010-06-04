ice.component.checkbox = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	   var Dom = YAHOO.util.Dom;
      
       if (YAHOO.widget.Logger){
	 	  YAHOO.widget.Logger.enableBrowserConsole();
       }
       
	   var button = new YAHOO.widget.Button(clientId, {type: jsProps.type});
	   
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
			e.target = document.getElementById(clientId);	
			//get the current value of checked
 	        var submittedValue =  e.newValue;	

//YAHOO.log("in onCheckedChange and e.newValue="+e.newValue+" button checked="+checkedValue);

			var params = function(parameter) {
				parameter(clientId+'_value', submittedValue);
			};
	        if (singleSubmit) {
//	         	YAHOO.log("not single submit");
                ice.se(e, e.target, params); 
            } else {
//            	YAHOO.log("single Submit is false");
                ice.s(e, e.target, params);                    
            }           
		};

		button.on("checkedChange", onCheckedChange);

		bindYUI(button);
	},
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {
		YAHOO.log("updateProperties for clientId="+clientId+" checked="+jsProps.checked);
       ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }  
};
