ice.component.radiobutton = {
    initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
		var Dom = YAHOO.util.Dom;
		var Event = YAHOO.util.Event; 
		var divNode = document.getElementById(clientId);
	    var groupId = clientId+"_div";
		
	    if (YAHOO.widget.Logger){
	 	 	  YAHOO.widget.Logger.enableBrowserConsole();
	    }
	    YAHOO.log("INIT clientId="+clientId+" divNode="+divNode+" groupId="+groupId);
		    
	    function onButtonGroupReady(){
		   var obuttonGroup = new YAHOO.widget.ButtonGroup(groupId);
		   
		   var singleSubmit=jsfProps.singleSubmit;	
		   if (jsf.selectedItemId){
			   YAHOO.log("parameter selectedItemId="+jsf.SelectedItemId);
		   }
		   //hidden field for new/current value
		   var hiddenField= Dom.get(clientId+"_hidden");
		   if (Dom.get(clientId+"_hidden")){
			//   var hiddenField = Dom.get(clientId+"_hidden");
			   var hiddenFieldValue = hiddenField.value;
			   YAHOO.log("hiddenField="+hiddenField+" value = "+hiddenFieldValue);
			   if (hiddenField.value) {
				   YAHOO.log("    value of hiddenField="+hiddenField.value);
 			    for (var i = 0; i < obuttonGroup.getCount(); i++){
 				   YAHOO.log("for loop i="+i);
 				   YAHOO.log(" button id="+obuttonGroup.getButton(i).get("id"));
 				   if (obuttonGroup.getButton(i).get("id") == hiddenField.value ){
 					   obuttonGroup.check(i);
 					   YAHOO.log("set button i=" +i+" to checkd");
 				   }else{
 					   obuttonGroup.getButton(i).set("checked",false);
 					   YAHOO.log("set button i="+i+" to unchecked");
 				   }
			    }
			   }
		   }else YAHOO.log("hiddenField not set!!! still null");
		   
		   //set default to first radiobutton
//		   obuttonGroup.check(0);
//		   obuttonGroup.getButton(0);
		   YAHOO.log("currently have "+obuttonGroup.getCount()+" in buttonGroup");	   
			logger.info('radiobutton group initialized');
		//only fired when the checked value has changed so will always 
		// fire valueChangeListener??
			
		var onChecked2 = function(event){
		    groupNode = document.getElementById(groupId);
		    YAHOO.log("groupNode="+groupNode+" button group="+obuttonGroup+" divNode="+divNode);
	        divNode = document.getElementById(clientId);
			YAHOO.log("subscribe has event="+event);
			if (event.prevValue){
				YAHOO.log("prevValue="+event.prevValue);
			}
			if (event.newValue){
				YAHOO.log("newValue="+event.newValue);
			}
			
			//could possibly use obuttonGroup.checkedButton??
	
			var oCheckedButton = this.get("checkedButton");
			var submittedValue = oCheckedButton.get("id");
			YAHOO.log("submittedValue="+submittedValue+" checkedButton="+oCheckedButton+" cId="+oCheckedButton.get("id"));

			//set these values to hiddenfields for prev and new values or just new value?
			if (hiddenField){
				hiddenField.value=oCheckedButton.get("id");
				YAHOO.log("set hidden fields value to submittedValue="+submittedValue);
			}
			//submit
	        if (singleSubmit) {
 	         	YAHOO.log("single submit goes to server");
// 			    //e.target is null so have to set the target to the root of this button for submit
 	         	event.target = groupNode;	
                ice.se(event, divNode); 
            } else {
             	YAHOO.log("single Submit is false update hidden fieldo on server");
                                   
            } 
		  };
        obuttonGroup.subscribe('checkedButtonChange',onChecked2);
		bindYUI(obuttonGroup);
      }
	 YAHOO.util.Event.onContentReady(clientId, onButtonGroupReady);
   },
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, yuiProps, jsfProps, events) {
	   YAHOO.log("updateProperties selectedItemId="+jsfProps.selectedItemId);
		var Dom = YAHOO.util.Dom;
		var isDone = false;
		//for now use the prop value, but later get it from the hiddenfield
		   var context = ice.component.getJSContext(clientId);
		    if (context && context.isAttached()) {
		    	isDone=true;
		    	var buttonGroup = context.getComponent();
		    	YAHOO.log("Did this get a buttonGroup??"+ buttonGroup.toString());
		    	
			   var hiddenField= Dom.get(clientId+"_hidden");
			   if (Dom.get(clientId+"_hidden")){
	
				   var hiddenFieldValue = hiddenField.value;
				   YAHOO.log("in UP:- hiddenField="+hiddenField+" value = "+hiddenFieldValue);
				   if (hiddenField.value) {
					   YAHOO.log("    value of hiddenField="+hiddenField.value);
	 			    for (var i = 0; i < buttonGroup.getCount(); i++){
	 				   YAHOO.log("   for loop i="+i);
	 				   YAHOO.log("   button id="+buttonGroup.getButton(i).get("id"));
	 				   if (buttonGroup.getButton(i).get("id") == hiddenField.value ){
	 					   buttonGroup.check(i);
	 					   YAHOO.log("    set button i=" +i+" to checkd");
	 				   }else{
	 					   buttonGroup.getButton(i).set("checked",false);
	 					   YAHOO.log("   set button i="+i+" to unchecked");
	 				   }
				    }
				   }
			   }else YAHOO.log("hiddenField not set!!! still null");
		    }
		if(!isDone){
	       ice.component.updateProperties(clientId, yuiProps, jsfProps, events, this);
		}
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
	   YAHOO.log("getInstance selectedItemId="+jsfProps.selectedItemId);
       ice.component.getInstance(clientId, callback, this);
   }  
};
