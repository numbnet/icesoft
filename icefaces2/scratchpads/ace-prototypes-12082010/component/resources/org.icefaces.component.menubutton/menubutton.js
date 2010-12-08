ice.component.menubutton = {
    initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
	   var Dom = YAHOO.util.Dom;
	   var Event = YAHOO.util.Event;
	   
       var divNode = document.getElementById(clientId);
       
       if (YAHOO.widget.Logger){
	 	  YAHOO.widget.Logger.enableBrowserConsole();
       }
       
       var spanId = clientId+"_span";
       var buttonId=clientId+"_button";
       var hiddenField = Dom.get(clientId+"_hidden");
       
       var menuName=buttonId+"select";
       if (yuiProps.menuName){
    	   menuName=yuiProps.menuName;
       }
       YAHOO.log("buttonId="+buttonId+"menuName="+menuName);
       
	   var button = new YAHOO.widget.Button(buttonId, {type: yuiProps.type, menu: menuName});
	   
       if(yuiProps.label) {
			button.set('label', yuiProps.label);
		}
		
		var singleSubmit=jsfProps.singleSubmit;	
		
  		
  	   if (jsfProps.disabled){
  			  button.set('disabled', true);
  	   }else{
 			  button.set('disabled', false);
 	   }
	   

  	   var onMenuClick = function (p_sType, p_aArgs){
  			var oEvent = p_aArgs[0],	//	DOM event

			oMenuItem = p_aArgs[1];	//	MenuItem instance that was the 
									//	target of the event

		    if (oMenuItem) {
			    YAHOO.log("[MenuItem Properties] text: " + 
						oMenuItem.cfg.getProperty("text") + ", value: " + 
						oMenuItem.value);
			    var submittedValue=oMenuItem.value;
			    //use params??
			    YAHOO.log("submitted value="+submittedValue+" for clientId="+clientId+"_value");
				var params = function(parameter) {
					parameter(clientId+'_value', submittedValue);
				};	    
			    // set the hidden field to new menu selected item's id??
//			    hiddenField.value=oMenuItem.value;
//			    YAHOO.LOG("hidden field = "+hiddenField);
			       if (singleSubmit) {
		 	         	YAHOO.log("single submit just submits itself target="+oEvent.target);
		 			    //e.target is null so have to set the target to the root of this button for submit
//		 	  //    	oEvent.target = divNode;
		                ice.se(oEvent, divNode, params); 
		            } else {
		             	YAHOO.log("submits form target="+oEvent.target);
		                ice.s(oEvent, divNode, params);                    
		            }  
		    }
		    
	

	   };
	   
	   var onSelectedMenuItemChange = function(event){
		   var menuItem = event.newValue;
		   YAHOO.log("new menuitem = "+menuItem);
		   this.set("label" ("<em class=\"yui-button-label\">" +
				       menuItem.cfg.getProperty("text") + "</em>"));
		   
	   };
	   
  	   button.on("selectedMenuItemChange", onSelectedMenuItemChange);
  	   
       button.getMenu().subscribe("click", onMenuClick);
//		   button.getMenu().clickEvent.subscribe(function(ev,args){
//			   var opt = args[1].srcElement;
//			   alert("clicked on "+opt.innerHTML + ' (' + opt.value + ')');
//		   });

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
