ice.component.commandlink = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
	    YAHOO.widget.Logger.enableBrowserConsole();

	YAHOO.util.Event.onDOMReady(function() {
    });


    var spanId = clientId + "_span";
	var oLinkButton = new YAHOO.widget.Button(spanId,{label: jsProps.label }, {type: jsProps.type});

	// logger.log("params singleSubmit"+jsfProps.singleSubmit);
		
		if(jsProps.label) {
			oLinkButton.set('label', jsProps.label);
		}

		if(jsProps.type) {
			oLinkButton.set('button', jsProps.type);
		}
	    var isSingle = jsfProps.singleSubmit;
	    //alert("Single submit is: " + this.isSingle + ", origninal " +  jsfProps.singleSubmit);
	    YAHOO.log(" - local isSingle: " + this.isSingle + " original " + jsfProps.singleSubmit);
        root = document.getElementById(spanId);

        if (jsfProps.doAction) {
            root.onclick = this.actionClickHandler;
        }
        //oLinkButton.onclick = this.theInsideClick;

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


   // handle clickage
   actionClickHandler:function (e) {
       //alert("ice.s, return false click handler");
       //e.returnValue=false;

       var divRoot = document.getElementById(this.spanId);
       YAHOO.log(" ++=+++++ How about: " + this.isSingle + "?" );

       if (this.isSingle) {
           YAHOO.log(" ++=+++++ SINGLE SUBMIT onClick and e.target="+e.target + ", while divRoot = " + divRoot);
           ice.se(e, e.target);
       } else {
           YAHOO.log(" Full Submit onClick and e.target="+e.target + ", while divRoot = " + divRoot);
           ice.s(e, e.target);
       }
      // YAHOO.util.stopEvent(e);
       return false;
	},


};
