jQuery.fn.appendPushUpdate = function(groupName, getURL) {
	return this.each(function() {
		var pushId = ice.push.createPushId();
		ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(){ 
			this.append($.get(getURL));
		});
	});
};

jQuery.fn.loadPushUpdate = function(groupName, getURL) {
	return this.each(function() {
		var pushId = ice.push.createPushId();
		var elem = this;
		ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(){ 
			$(elem).load($.get(getURL));
		});
	});
};

jQuery.push = {
	
	groups : {},	
	
	listenToGroup : function(groupName, callback) {
		var pushId = ice.push.createPushId();
		if( this.groups[groupName] == null ){
			this.groups[groupName] = new Array();			
		}
		this.groups[groupName].push([callback,pushId]);
		ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], callback);
	},
	
	stopListeningToGroup: function(groupName, callback){
		var registrations = this.groups[groupName];
		if( registrations ){
			for( var i=0; i < registrations.length ; i++ ){
				if( registrations[i][0] == callback ){
					ice.push.removeGroupMember(groupName,registrations[i][1]);
					registrations[i] = null;
					if( registrations.length == 0 )
						this.groups[groupName] = null;
					break;
				}
			}
		}
	},
	
	appendUpdates: function(selector, groupName, getURL){
		var pushId = ice.push.createPushId();
		if( this.groups[groupName] == null ){
			this.groups[groupName] = new Array();			
		}
		this.groups[groupName].push([getURL,pushId]);
		ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(){
			$.get(getURL,function(data){
				$(selector).each( function(idx,elem){
					$(elem).append(data);
				});
			});			
		});
	},
	loadUpdates: function(selector, groupName, getURL){
		var pushId = ice.push.createPushId();
		if( this.groups[groupName] == null ){
			this.groups[groupName] = new Array();			
		}
		this.groups[groupName].push([getURL,pushId]);
		ice.push.addGroupMember(groupName, pushId);
		ice.push.register( [ pushId ], function(){
			$.get(getURL,function(data){
				$(selector).each( function(idx,elem){
					$(elem).html(data);
				});
			});			
		});
	}
}
