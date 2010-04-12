

/****************** SECURITY AND AUTHORIZATION ***********************************/

function login() {
	new Ajax.Request('auth', { 
		method:'post',
		parameters: {op: 'login', userName: $('userName').getValue(), 
			nickName: $('nickName').getValue(), password: $('password').getValue()},
		onSuccess: function(transport){
			window.location.reload();
		}
	});
}
function register() {
	new Ajax.Request('auth', { 
		method:'post',
		parameters: {op: 'register', userName: $('userName').getValue(), 
			nickName: $('nickName').getValue(), password: $('password').getValue()},
		onSuccess: function(transport){
			window.location.reload();
		}
	});
}

function logout() {
	if( getCurrentRoomName() != null )
		logoutOfChatRoom(getCurrentRoomName());
	new Ajax.Request('auth', { 
		method:'post',
		parameters: {op:'logout'},
		onSuccess: function(transport){
			window.location.reload();
		}
	});
}


/****************** TEMPLATES ***********************************/
function loadChatLogin(parent){
	new Ajax.Updater(parent, "./jsp/chatLogin.jsp", {
		  method: 'get'
	});
}


/****************** RESOURCES ***********************************/

function updateDraft(roomName,msg){
	new Ajax.Request('updatedraft', { 
		method:'post',
		parameters: {roomName:roomName,msg:msg}
	});
}

function loginToChatRoom(roomName){
	new Ajax.Request('logintoroom', { 
		method:'post',
		parameters: {roomName: roomName}
	});
}



/****************** VIEW HANDLING ***********************************/

function click_createChatRoom(){
	new Ajax.Request('createroom', { 
		method:'post',
		parameters: {roomName: getNewChatRoomName()},
		onSuccess: function(transport){
			refreshChatRoomsList();
			openChatRoom(getNewChatRoomName());
			$("newChatRoomName").setValue('');	
		}
	});
	
}

function click_sendMessage(){
	new Ajax.Request('createmessage', { 
		method:'post',
		parameters: {roomName: $('roomName').getValue(), msg: $("newChatRoomMessage").getValue()}
	});
	$("newChatRoomMessage").setValue('');
	$("chatRoomMessages").insert({bottom: "<div class='loading'></div>"});
	
}

function kp_updateDraft(event){
	if( event.charCode == 32 ){ //space bar key
		updateDraft(getCurrentRoomName(),$("newChatRoomMessage").getValue());
	}
	return false;
}

function getCurrentRoomName(){
	return $('roomName').getValue();
}

function getNewChatRoomName(){
	return clean($("newChatRoomName").getValue());
}

function refreshChatRoomsList(){
	new Ajax.Updater('chatRooms', './chatrooms', {
		  method: 'get'
	});
}

function refreshChatRoomUsers(){
	loading($("users"));
	new Ajax.Request('./chatroomusers', { 
		method:'get',
		parameters: {roomName: getCurrentRoomName()},
		onSuccess: function(req){
			$("users").insert(req.responseText);
			$$("#users div[id]").each( 
				function (elem){
					Push.listenToGroup($(elem).childElements()[0].id, function(){ 
						window.refreshUserDraft(elem.id); 
					});		    		
				}
			);
		}
	});
}

function refreshUserDraft(userName){
	new Ajax.Request('./messagedraft', { 
		method:'get',
		parameters: {roomName: getCurrentRoomName(), userName: userName},
		onSuccess: function(req){
			$(getCurrentRoomName()+"_"+userName+"_draft").innerHTML = req.responseText;
			$$('#' + getCurrentRoomName()+"_"+userName+"_draft .typing")[0].fade({ duration: 10.0, to: 0.01 });

		}
	});
}

function refreshChatRoomMessages(){
	$$("#chatRoomMessages > div.loading").each( function(elem){elem.remove();});
	var lastDiv = $("chatRoomMessages").childElements().last();
	var lastMsgId = lastDiv ? lastDiv.id : 0;
	new Ajax.Request('./messages', { 
		method:'get',
		parameters: {roomName: getCurrentRoomName(), idx: lastMsgId},
		onSuccess: function(req){
			var msgsDiv = $("chatRoomMessages");
			msgsDiv.insert({bottom: req.responseText});
			msgsDiv.scrollTop = msgsDiv.scrollHeight;
			var lastDiv = $("chatRoomMessages").childElements().last();
			var lastMsgId = lastDiv ? lastDiv.id : 0;
			if( msgsDiv.childElements().length > lastMsgId ){
				Sound.play('./media/blip.wav');
				msgsDiv.childElements().last().highlight();
			}
		}
	});
}

function logoutOfChatRoom(roomName){
	if( roomName != null ){
		new Ajax.Request('./logoutofroom', { 
			method:'post',
			parameters: {roomName: roomName}
		});
	}	
}

function openChatRoom(roomName){
	
	if( getCurrentRoomName() != null ){
		logoutOfChatRoom(getCurrentRoomName());
	}
	
	loginToChatRoom(roomName);
	
	Push.listenToGroup(roomName+"_users",window.refreshChatRoomUsers);
	Push.listenToGroup(roomName+"_messages",window.refreshChatRoomMessages);
	
	$("currentChatRoom").innerHTML =  $("chatRoomTemplate").innerHTML;
	$("chatRoom").appear();
	$("roomName").setValue(roomName);
	$("currentChatRoomName").innerHTML = roomName;
	refreshChatRoomUsers();
	refreshChatRoomMessages();
	
	$("newChatRoomMessage").observe("keyup", function(event){
		if(event.keyCode == 13){ 
			click_sendMessage();
			event.preventDefault();
		}
		window.updateDraft(getCurrentRoomName(),$("newChatRoomMessage").getValue());
	});
	
}

function refreshChatRoomUsersAndMessages(){
	refreshChatRoomUsers();
	refreshChatRoomMessages();
}

function loading(element){
	if( element ){
		var newChild = document.createElement("div");
		newChild.setAttribute("style","loading");
		element.innerHTML = "";
		element.appendChild(newChild);
	}	
}

function clean(str){
	return str.replace(' ','_');
}
