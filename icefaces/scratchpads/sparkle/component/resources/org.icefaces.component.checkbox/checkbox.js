Ice.component.checkbox = {
	initialize:function(clientId) {
		var Dom = YAHOO.util.Dom;
		var button = new YAHOO.widget.Button(clientId, {type: 'checkbox'});

		var label = Ice.component.getProperty(clientId, 'label');
		if(label) {
			button.set('label', label['new']);
		}

		var checked = Ice.component.getProperty(clientId, 'checked');
		if(checked) {
			button.set('checked', checked['new']);
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

		return button;
	},
	updateProperties:function(_id, props) {
		var button = Ice.component.updateProperties(_id, props, this);
	},
	execute: function(_id) {
		var ele = document.getElementById(_id+'call');
		eval(ele.innerHTML);
	}
};
