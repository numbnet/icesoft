ice.component.checkbox = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
		var Dom = YAHOO.util.Dom;
		var button = new YAHOO.widget.Button(clientId, {type: jsProps.type});

		if(jsProps.label) {
			button.set('label', jsProps.label);
		}

		if(jsProps.checked) {
			button.set('checked', jsProps.checked);
		}
	   
		var onCheckedChange = function (e) { 
			e.target = document.getElementById(clientId);

			var params = function(parameter) {
				parameter(clientId+'_value', e.newValue);
			};

			ice.submit(e, e.target, params);
			logger.info('checkbox event submitted');
		};

		button.addListener("checkedChange", onCheckedChange);

		logger.info('checkbox initialized');

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
