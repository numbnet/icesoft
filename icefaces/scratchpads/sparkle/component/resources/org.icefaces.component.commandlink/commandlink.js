ice.component.commandlink = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {

	YAHOO.util.Event.onDOMReady(function() {
    });

	var Dom = YAHOO.util.Dom;
	jprops = jsfProps;
	var oLinkButton = new YAHOO.widget.Button(clientId,{label: jsProps.label }, {type: jsProps.type});

	// logger.log("params singleSubmit"+jsfProps.singleSubmit);
		
		if(jsProps.label) {
			oLinkButton.set('label', jsProps.label);
		}

		if(jsProps.type) {
			oLinkButton.set('button', jsProps.type);
		}
		this.jprops = jsfProps;
		
	  
	    var onClick = function (e) {
		    // logger.log(  " in onClick function for e = " + e );
		    buttonNode = document.getElementById(clientId);

            alert ("jsfProps.doAction = " + jsfProps.doAction);
            if (!jsfProps.doAction) {
                e.returnValue=false;
                YAHOO.util.stopEvent(e);
                }

		    alert ("First");

	        if (jsfProps.singleSubmit) {
	        	YAHOO.log("not single submit");
                ice.se(e, e.target);
            } else {
            	YAHOO.log("single Submit is false");
                ice.s(e, e.target);
            }
            alert("Second");
            alert("Third");
            return false;
		};

        root = document.getElementById(clientId);

        root.onclick = this.theInsideClick;


		//oLinkButton.on("click", onClick);
		// Set the aria role for the link. No further attributes supported.
		root.firstChild.setAttribute("role", "link");
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
   }, 



   theInsideClick:function (e) {

       e.returnValue=false;

	   //buttonNode = document.getElementById(clientId);
       ice.s(e, e.target);

       //YAHOO.util.stopEvent(e);
       // YUI 3? 
       //DOMEventFacade.preventDefault(false);

       //e.preventDefault();
       return false;
	}

};

 var theOutsideOnClick = function (e) {

		    //buttonNode = document.getElementById(clientId);
            ice.s(e, e.target);
            alert("My Function second");
            e.returnValue=false;
            // YAHOO.util.stopEvent(e);
            window.event.returnValue=false;
            return false;
		};