(function($) {	
	$.push = {
		version : 0.1,
		groups : {},
		pushIds : {},
		listenToGroup : function(groupName, callback) {
			var pushId = ice.push.createPushId();
			if (this.groups[groupName] == null) {
				this.groups[groupName] = new Array();
			}
			this.groups[groupName].push( [ callback, pushId ]);
			this.pushIds[pushId] = [groupName,callback];
			ice.push.addGroupMember(groupName, pushId);
			ice.push.register( [ pushId ], callback);
		},		
		stopListeningToGroup : function(groupName, callback) {
			var registrations = this.groups[groupName];
			if (registrations) {
				//if callback passed, just remove registrations for this callback
				if( callback ){						
					for ( var i = 0; i < registrations.length; i++) {
						if (registrations[i][0] == callback) {
							ice.push.removeGroupMember(groupName,
									registrations[i][1]);
							$.push.pushIds[registrations[i][1]] = null;
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
						$.push.pushIds[registrations[i][1]] = null;
						registrations[i] = null;
					}
					this.groups[groupName] = null;
				}
			}
		}
	};
	$.fn.loadPushUpdates = function(groupName, getURL){
		var pushId = ice.push.createPushId();
    	if ($.push.groups[groupName] == null) {
    		$.push.groups[groupName] = new Array();
		}
    	$.push.groups[groupName].push( [ getURL, pushId ]);
    	$.push.pushIds[pushId] = [this,getURL];
    	ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(pushId) {    
			$.get($.push.pushIds[pushId][1],function (data){
				$($.push.pushIds[pushId][0]).each( function(idx,elem){
					$(elem).html(data);
				});
			});
		});
    	return this;
    };    
    $.fn.appendPushUpdates = function(groupName, getURL){
    	var pushId = ice.push.createPushId();
    	if ($.push.groups[groupName] == null) {
    		$.push.groups[groupName] = new Array();
		}
    	$.push.groups[groupName].push( [ getURL, pushId ]);
    	$.push.pushIds[pushId] = [this,getURL];
    	ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(pushId) {    
			$.get($.push.pushIds[pushId][1],function (data){
				$($.push.pushIds[pushId][0]).each( function(idx,elem){
					$(elem).append(data);
				});
			});
		});
    	return this;
    };    
	return this;
})(jQuery);
