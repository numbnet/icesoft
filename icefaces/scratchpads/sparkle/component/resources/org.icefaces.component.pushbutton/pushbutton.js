ice.component.pushbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
    if (YAHOO.widget.Logger){
	 	  YAHOO.widget.Logger.enableBrowserConsole();
     }
        //want the span id
        var spanId = clientId+"_span";
        YAHOO.log("clientId="+clientId+" spanId="+spanId);
        //get the button html element
        var buttonNode = document.getElementById(spanId);
    
		var button = new YAHOO.widget.Button(spanId,
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
		}
	
        if (jsfProps.disabled){
              button.set("disabled", true);
        } else{
        	button.set("disabled", false);
        }

		var onClick = function (e) {
           YAHOO.log(" in onClick and e.target="+e.target);
   		   YAHOO.log("  buttonRoot="+buttonRoot+"  buttonNode="+buttonNode);
            var divRoot= document.getElementById(clientId);
            //singleSubmit means button just submits itself and renders itself
            //single submit false means that it submits the form
	        if (jsfProps.singleSubmit) {
	        	YAHOO.log(" single submit is true for clientId="+spanId);
                ice.se(e, divRoot); 
            } else {
            	YAHOO.log("single Submit is false for clientId="+spanId);
                ice.s(e, divRoot);                    
            }             
		};
		
		buttonRoot = document.getElementById(spanId);
		if (jsfProps.aria) {
		    //add roles and attributes to the YUI slider widget
		    buttonRoot.firstChild.setAttribute("role", "button");
		    if (jsProps.label){
		         buttonRoot.firstChild.setAttribute("aria-describedby",jsProps.label);
		    } else {
		    	buttonRoot.firstChild.setAttribute("aria-describedby","button description unavailable");
		    }
		    if (jsfProps.disabled){
		    	buttonRoot.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
		    }   
		}

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
