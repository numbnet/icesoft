ice.component.commandlink = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	    YAHOO.widget.Logger.enableBrowserConsole();

	YAHOO.util.Event.onDOMReady(function() {
    });


        var spanId = clientId + "_span";
	    var oLinkButton = new YAHOO.widget.Button(spanId,{ label: jsProps.label }, {type: jsProps.type});

        root = document.getElementById(spanId);

		root.firstChild.setAttribute("role", "link");
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
        var singleSubmit = JSContext..getJSFProps().singleSubmit;
        var doAction = JSContext.getJSFProps().doAction;
        var divRoot = document.getElementById(clientId);

        YAHOO.log("--> Button.doAction = " + doAction);

        if (singleSubmit) {
            YAHOO.log("Single Submit on element: " + divRoot);
            ice.se(e, divRoot );
        } else {
            YAHOO.log("Full Submit on element: " + divRoot);
            ice.s(e, divRoot );
        }
        // If there are actionListeners, don't do default behaviour
        if (doAction) {
            return false;
        }
    }
};
