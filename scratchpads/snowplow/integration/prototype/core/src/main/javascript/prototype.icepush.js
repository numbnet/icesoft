var Push = {
	version: 0.1,
	groups: {},
	pushIds: {}
};

Push.listenToGroup = function(groupName, callback) {
	var pushId = ice.push.createPushId();
	if (this.groups[groupName] == null) {
		this.groups[groupName] = new Array();
	}
	this.groups[groupName].push( [ callback, pushId ]);
	this.pushIds[pushId] = [groupName,callback];
	ice.push.addGroupMember(groupName, pushId);
	ice.push.register( [ pushId ], callback);
};	

var AjaxPush = {
	Updater: Class.create({	  
		initialize: function(container, pushGroup, url, options) {
			Push.listenToGroup(pushGroup, function(){
				new Ajax.Updater(container, url, options );
			});
		  }
	})
};


Push.stopListeningToGroup = function(groupName, callback) {
	var registrations = this.groups[groupName];
	if (registrations) {
		// if callback passed, just remove registrations for this callback
		if( callback ){						
			for ( var i = 0; i < registrations.length; i++) {
				if (registrations[i][0] == callback) {
					ice.push.removeGroupMember(groupName,
							registrations[i][1]);
					Push.pushIds[registrations[i][1]] = null;
					registrations[i] = null;
					if (registrations.length == 0)
						this.groups[groupName] = null;
					break;
				}
			}
		}
		else{
			for ( var i = 0; i < registrations.length; i++) {
				ice.push.removeGroupMember(groupName,
						registrations[i][1]);
				Push.pushIds[registrations[i][1]] = null;
				registrations[i] = null;
			}
			this.groups[groupName] = null;
		}
	}
};

Push.Methods = {
    loadPushUpdates: function(element, groupName, url, insertionPoint) {
        element = $(element);
        var pushId = ice.push.createPushId();
    	if (Push.groups[groupName] == null) {
    		Push.groups[groupName] = new Array();
		}
    	Push.groups[groupName].push( [ url, pushId ]);
    	Push.pushIds[pushId] = [element,url];
    	ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(pushId) { 
			new Ajax.Updater(Push.pushIds[pushId][0], Push.pushIds[pushId][1], {
				  method: 'get',
				  insertion: insertionPoint
			});
		});
        return element;
    }
};

Element.addMethods(Push.Methods);