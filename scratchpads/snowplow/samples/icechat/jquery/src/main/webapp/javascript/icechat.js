/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

/****************** SECURITY AND AUTHORIZATION ***********************************/

function login() {
	$.post("auth",{op:"login", userName:$("#userName").val()},
			function(data){ if(data)$("#loginMessages").html(data);else window.location.href = './index.html';});
}
function logout() {
	if( getCurrentRoomName() != null )
		logoutOfChatRoom(getCurrentRoomName());
	$.post("auth",{
		op:"logout"
	},function(){window.location.href = './login.html'; });
}


/****************** TEMPLATES ***********************************/
function loadHeader(parent){
	$(parent).load("inc/header.html");
}

function loadChatRoomsPanel(parent){
	$(parent).load("inc/chatRoomsPanel.html");	
}

function loadChatLogin(parent){
	$(parent).load("jsp/chatLogin.jsp");
}

function loadChatPanel(parent){
	$(parent).load("inc/chatPanel.html", function(){ evaluateScripts(parent)});	
}

function loadChatRoomTemplate(parent){
	$(parent).load("inc/chatRoom.html");
}


/****************** RESOURCES ***********************************/

function updateDraft(roomName,msg){
	$.post("updatedraft",{roomName:roomName,msg:msg});
}

function loginToChatRoom(roomName){
	$.post("logintoroom",{roomName: roomName});
}



/****************** VIEW HANDLING ***********************************/

function click_createChatRoom(){
	$.post('createroom',{roomName: getNewChatRoomName()}, 
			function(){
				refreshChatRoomsList();
				openChatRoom(getNewChatRoomName());
				$("#newChatRoomName").val('');				
	});
	
	
}

function click_sendMessage(){
	$.post("createmessage",{roomName: $('#roomName').val(), 
		msg: $("#newChatRoomMessage").val()});
	$("#newChatRoomMessage").val('');
	$("#chatRoomMessages").append("<div class='loading'></div>");
	
}

function kp_updateDraft(event){
	if( event.charCode == 32 ){ //space bar key
		updateDraft(getCurrentRoomName(),$("#newChatRoomMessage").val());
	}
	return false;
}

function getCurrentRoomName(){
	return $('#roomName').val();
}

function getNewChatRoomName(){
	return clean($("#newChatRoomName").val());
}

function refreshChatRoomsList(){
	$("#chatRooms").load("chatrooms");
}

function refreshChatRoomUsers(){
	loading(document.getElementById("users"));
	$("#users").load("chatroomusers?roomName=" + getCurrentRoomName(), function(){
		$("#users div[id]").each( 
				function (idx, elem){
					$.push.listenToGroup($(elem).children()[0].id, function(){ 
						window.refreshUserDraft(elem.id); 
					});		    		
				}
			);
	});
}

function refreshUserDraft(userName){
	$("#"+getCurrentRoomName()+"_"+userName+"_draft").load("messagedraft?roomName="+getCurrentRoomName()+"&userName="+userName,
			function(){
				$("#"+getCurrentRoomName()+"_"+userName+"_draft .typing").fadeTo(10000,0.001);
			}
	);
}

function refreshChatRoomsPanel(){
	if( $("#chatRooms").size()==0)
		loadChatRoomsPanel(document.getElementById("chatRoomsPanel"));
	refreshChatRoomsList();
}

function refreshChatRoomMessages(){
	$("#chatRoomMessages > div.loading").remove();
	var lastDiv = $("#chatRoomMessages > div:last");
	var lastMsgId = lastDiv.length > 0 ? lastDiv[0].id : 0;
	$.get("messages?roomName="+getCurrentRoomName()+"&idx="+lastMsgId, 
		function (data){
			$("#chatRoomMessages").append(data);
			//$("#chatRoomMessages > div:last").hide();
			$("#chatRoomMessages").ready( function(){
				//$("#chatRoomMessages > div:last").slideDown("slow");
				$("#chatRoomMessages").animate({ scrollTop: $("#chatRoomMessages").attr("scrollHeight") }, 3000);
			});		
	});
}

function logoutOfChatRoom(roomName){
	if( roomName != null ){
		$.post("logoutofroom",{roomName:roomName});
	}	
}

function openChatRoom(roomName){
	
	if( getCurrentRoomName() != roomName ){
		if( getCurrentRoomName() != null ){
			logoutOfChatRoom(getCurrentRoomName());
		}
		
		loginToChatRoom(roomName);
		
		$.push.listenToGroup(roomName+"_users",window.refreshChatRoomUsers);
		$.push.listenToGroup(roomName+"_messages",window.refreshChatRoomMessages);
		
		$("#currentChatRoom").html( $("#chatRoomTemplate").html() );
		$("#chatRoom").ready(function(){
			$("#chatRoom").fadeIn("slow");
		});
		$("#roomName").val(roomName);
		$("#currentChatRoomName").html(roomName);
		refreshChatRoomUsers();
		refreshChatRoomMessages();
		
		$("#newChatRoomMessage").bind("keyup", function(event){
			if(event.keyCode == 13){ 
				click_sendMessage();
				event.preventDefault();
			}
			window.updateDraft(getCurrentRoomName(),$("#newChatRoomMessage").val());
		});
	}	
}

function refreshChatRoomUsersAndMessages(){
	refreshChatRoomUsers();
	refreshChatRoomMessages();
}

function refreshChatPanel(){
	if( $("#chatPanel").size()==0)
		loadChatPanel(document.getElementById("content"));
	refreshChatRoomsPanel();
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
