ice.component.checkboxbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	  var Dom = YAHOO.util.Dom;
      var divNode = document.getElementById(clientId);

//      if (YAHOO.widget.Logger){
//	 	  YAHOO.widget.Logger.enableBrowserConsole();
//      }
      YAHOO.util.Event.onDOMReady(function() {    
       var spanId = clientId+"_span";
	   var button = new YAHOO.widget.Button(spanId, {type: jsProps.type});

		var hiddenField = Dom.get(clientId+"_hidden");

		//use input hidden field instead to update value for server push
  		if(jsProps.checked) {
   			button.set('checked', hiddenField.value);
 		}
    
		var onCheckedChange = function (e) {
           var context = ice.component.getJSContext(clientId);
           var singleSubmit= context.getJSFProps().singleSubmit;
           var postParameters = context.getJSFProps().postParameters;
           var params = function(parameter) {
               if (postParameters != null) {
                   var argCount = postParameters.length / 2;
                   for (var idx =0; idx < argCount; idx ++ ) {
                       parameter( postParameters[idx*2], postParameters[(idx*2)+1] );
                   }
               }
           };
           YAHOO.log(" in onCheckedChange singleSubmit="+singleSubmit);
           if (singleSubmit)
                YAHOO.log("in onCheckedChange singleSubmit="+singleSubmit);
            else YAHOO.log("  onCheckedChange no jsfProps.singleSubmit"+singleSubmit);
		    buttonNode = document.getElementById(spanId);
	        divNode = document.getElementById(clientId);
			//get the current value of checked
 	        var submittedValue =  e.newValue;

            YAHOO.log("         e.newValue="+e.newValue+" old="+e.prevValue);

			hiddenField.value=submittedValue;

	        YAHOO.log(" hidden Field="+clientId+"_hidden"+" has value="+hiddenField.value);

	        if (singleSubmit) {
 	         	YAHOO.log("single submit goes to server");
 			    //e.target is null so have to set the target to the root of this button for submit
 	         	e.target = buttonNode;
                ice.se(e, divNode, params);
            } else {
             	YAHOO.log("single Submit is false doesn't go to server");
                //no submit!! already updated the hidden field
            }
		};

		button.on("checkedChange", onCheckedChange);

	    if (jsfProps.aria) {
	         //add roles and attributes to the YUI slider widget
	            buttonNode = document.getElementById(spanId);
	            buttonNode.firstChild.setAttribute("role", "button");
	            buttonNode.firstChild.setAttribute("aria-describedby",jsProps.label);
	            if (jsfProps.disabled){
	            	buttonNode.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
	            }
	            //listen for keydown event, to provide short-cut key support.

	            button.on("keydown", function(event) {
	                //get the current value of the element and set the aria values
	            	//the space bar toggles the button
	                var isSpace = event.keyCode == 32;

	                if (isSpace) {
	                	// submit the thing
	                	YAHOO.log(" submit this thing for keycode="+event.keyCode);

	                	//update the root of this element to set the aria values
	        			buttonNode = document.getElementById(spanId);
	          			//get the current value of checked
	         	        var submittedValue =  event.newValue;
	         	        if (submittedValue){
	        	            buttonNode.firstChild.setAttribute("aria-checked",true);
	         	        }else {
	        	            buttonNode.firstChild.setAttribute("aria-checked",false);
	         	        }

	         	       //the space seems to fire the onClick event for both FF and Safari
	                }
	            }, divNode);
	    }
		bindYUI(button);
      });
	},
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {        		
        YAHOO.log("updateProperties...checking jsfProps.singleSubmit="+jsfProps.singleSubmit);

        var context = ice.component.getJSContext(clientId);
        if (context && context.isAttached()) {
            var prevJSFProps = context.getJSFProps();
            if (prevJSFProps.hashCode != jsfProps.hashCode) {
                context.getComponent().destroy();
                document.getElementById(clientId)['JSContext'] = null;
                JSContext[clientId] = null;
            }
        }
       ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
       //     var JSContext = ice.component.getJSContext(clientId);
       //     var singleSubmit = JSContext.getJSFProps().singleSubmit;

   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }


};
