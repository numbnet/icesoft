ice.component.linkButton = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	    YAHOO.widget.Logger.enableBrowserConsole();

	YAHOO.util.Event.onDOMReady(function() {
    });


        var spanId = clientId + "_span";
	    var oLinkButton = new YAHOO.widget.Button(spanId,{ label: jsProps.label }, {type: jsProps.type});

        root = document.getElementById(spanId);

		root.firstChild.setAttribute("role", "link");
	    root.firstChild.setAttribute("aria-labelledby",jsProps.label);
	    if (jsfProps.disabled){
	        root.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
	    }
	    // If there's no action listener, this is standard anchor behaviour
	    // otherwise it's got an actionListener/action attribute. Described by offers further description
	    if (!jsfProps.doAction ) {
	       root.firstChild.setAttribute("aria-describedby", "Standard HTML anchor");
	    } else {
           root.firstChild.setAttribute("aria-describedby", "JSF action event source");
        } 

		bindYUI(oLinkButton);
	},
	
    //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
    updateProperties:function(clientId, jsProps, jsfProps, events) {
        ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
    },
 
    //delegate call to ice.yui.getInstance(..) with the reference of this lib
    getInstance:function(clientId, callback) {
        ice.component.getInstance(clientId, callback, this);
    },

    // Click handler visible from Renderer code 
    clickHandler : function (e, clientId) {

        var JSContext = ice.component.getJSContext(clientId);
        var singleSubmit = JSContext.getJSFProps().singleSubmit;
        var postParameters = JSContext.getJSFProps().postParameters; 
        var doAction = JSContext.getJSFProps().doAction;
        var divRoot = document.getElementById(clientId);

        YAHOO.log("--> Button.doAction = " + doAction);

         var params = function(parameter) {
            var nameAndValue = postParameters.split(",");
            var argCount = nameAndValue.length / 2;
            for (var idx =0; idx < argCount; idx ++ ) {
                parameter( nameAndValue[idx*2], nameAndValue[(idx*2)+1] );
            }
        };

        if (singleSubmit) {

            YAHOO.log("Single Submit on element: " + divRoot);
            ice.se(e, divRoot, params );
        } else {
            YAHOO.log("Full Submit on element: " + divRoot);
            ice.s(e, divRoot, params );
        }
        // If there are actionListeners, don't do default behaviour
        if (doAction) {
            return false;
        }
    }
};
