ice.component.pushbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
      if (YAHOO.widget.Logger) {
            YAHOO.widget.Logger.enableBrowserConsole();
      }
      YAHOO.util.Event.onDOMReady(function() {
        //want the span id
        var spanId = clientId + "_span";
        YAHOO.log("clientId=" + clientId + " spanId=" + spanId);
        //get the button html element
        var buttonNode = document.getElementById(spanId);

        var button = new YAHOO.widget.Button(spanId,
            {label: jsProps.label,
            type: jsProps.type});


        if (jsProps.label) {
            button.set('label', jsProps.label);
        }

        if (jsProps.type) {
            button.set('button', jsProps.type);
        }
        if (!jsProps.tabindex) {
            button.set('tabindex', jsProps.tabindex);
        }

        if (jsfProps.disabled) {
            button.set("disabled", true);
        } else {
            button.set("disabled", false);
        }

        var postParameters = jsfProps.postParameters;
        var params = function(parameter) {
            if (postParameters != null) {
                 var argCount = postParameters.length / 2;
                 for (var idx =0; idx < argCount; idx ++ ) {
                     parameter( postParameters[idx*2], postParameters[(idx*2)+1] );
                 }
            }
        };


        var onClick = function (e) {
            YAHOO.log(" in onClick and e.target=" + e.target);
            YAHOO.log("  buttonRoot=" + buttonRoot + "  buttonNode=" + buttonNode);
            var divRoot = document.getElementById(clientId);
            //singleSubmit means button just submits itself and renders itself
            //single submit false means that it submits the form
            var context = ice.component.getJSContext(clientId);
            var singleSubmit = context.getJSFProps().singleSubmit;

            if (singleSubmit) {
                YAHOO.log(" single submit is true for clientId=" + spanId);
                ice.se(e, divRoot, params);
            } else {
                YAHOO.log("single Submit is false for clientId=" + spanId);
                ice.s(e, divRoot, params);
            }
        };

        buttonRoot = document.getElementById(spanId);
        if (jsfProps.aria) {
            //add roles and attributes to the YUI slider widget
            buttonRoot.firstChild.setAttribute("role", "button");
            if (jsfProps.ariaLabel) {
                buttonRoot.firstChild.setAttribute("aria-describedby", jsfProps.ariaLabel);
            } else {
                buttonRoot.firstChild.setAttribute("aria-describedby", "button description unavailable");
            }
            if (jsfProps.disabled) {
                buttonRoot.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
            }
        }

        button.on("click", onClick);


        bindYUI(button);
      });
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
