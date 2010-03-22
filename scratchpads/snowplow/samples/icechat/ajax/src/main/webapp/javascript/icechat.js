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
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			if( xmlHttp.responseText )
				$("loginMessages").innerHTML = xmlHttp.responseText;
			else
				window.location.href = './index.html';
		}
	}
	var loginForm = document.forms['login'];
	var params = "op=login&" + getLoginFormParams();
	xmlHttp.open("POST", "auth", true);
	xmlHttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(params);	
}

function getLoginFormParams(){
	var loginForm = document.forms['login'];
	return  "userName=" + loginForm.elements['userName'].value;
}

function logout() {
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			window.location.href = './login.html';
		}
	}
	var params = "op=logout";
	xmlHttp.open("POST", "auth", true);
	xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlHttp.send(params);	
}


/****************** TEMPLATES ***********************************/
function loadHeader(parent){
	doGet("inc/header.html",parent);
}

function loadChatRoomsPanel(parent){
	doGet("inc/chatRoomsPanel.html",parent);
}

function loadChatLogin(parent){
	doGet("jsp/chatLogin.jsp",parent);
}

function loadChatPanel(parent){
	doGetAndEval("inc/chatPanel.html",parent);	
}

function loadChatRoomTemplate(parent){
	doGet("inc/chatRoom.html",parent);
}


/****************** RESOURCES ***********************************/

function createChatRoom(roomName) {
	var xmlHttp = getXmlHttpRequest();
	var params = "roomName=" + roomName;
	xmlHttp.open("POST", "createroom", false);
	xmlHttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(params);
}

function getChatRoomUsers(roomName) {
	var xmlHttp = getXmlHttpRequest();
	var params = "roomName=" + roomName;
	xmlHttp.open("GET", "chatroomusers?" + params, false);
	xmlHttp.send(params);	
	return xmlHttp.responseText;
}

function getChatRooms(){
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.open("GET", "chatrooms", false);
	xmlHttp.send(null);	
	return xmlHttp.responseText;
}

function getChatRoomMessages(roomName) {
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.open("GET", "messages?roomName="+roomName, false);
	xmlHttp.send(null);	
	return xmlHttp.responseText;
}

function logoutOfChatRoom(roomName){
	if( roomName != null ){
		var xmlHttp = getXmlHttpRequest();
		var params = "roomName=" + roomName;
		xmlHttp.open("POST", "logoutofroom", false);
		xmlHttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlHttp.send(params);		
	}	
}

function sendMessage(roomName,msg) {
	var xmlHttp = getXmlHttpRequest();
	var params = "roomName=" + roomName + "&msg=" + msg;
	xmlHttp.open("POST", "createmessage", false);
	xmlHttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(params);
}

function updateDraft(roomName,msg){
	var xmlHttp = getXmlHttpRequest();
	var params = "roomName=" + roomName + "&msg=" + msg;
	xmlHttp.open("POST", "updatedraft", false);
	xmlHttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(params);
}

function loginToChatRoom(roomName){
	var xmlHttp = getXmlHttpRequest();
	var params = "roomName=" + roomName;
	xmlHttp.open("POST", "logintoroom", false);
	xmlHttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(params);	
}



/****************** VIEW HANDLING ***********************************/

function click_createChatRoom(){
	var roomName = getNewChatRoomName();
	createChatRoom(roomName);
	refreshChatRoomsList();
	//document.forms['createNewChatRoom'].newChatRoomName.value = ""; ff
	document.getElementById("createNewChatRoom").elements["newChatRoomName"].value = '';
	openChatRoom(roomName);
}

function click_sendMessage(){
	sendMessage(getCurrentRoomName(),getNewMessage());
	//sendMessage(document.forms['chatRoomForm'].roomName.value, //ff
	//		document.forms['chatRoomForm'].newChatRoomMessage.value);
	//document.forms['chatRoomForm'].newChatRoomMessage.value = "";
	document.getElementById("chatRoomForm").elements["newChatRoomMessage"].value = '';
	refreshChatRoomMessages();
	refreshChatRoomUsers();
}

function kp_updateDraft(event){
	if( event.charCode == 32 ){ //space bar key
		//updateDraft(document.forms['chatRoomForm'].roomName.value,
		//		document.forms['chatRoomForm'].newChatRoomMessage.value);
		updateDraft(getCurrentRoomName(),getNewMessage());
		refreshChatRoomUsers();
	}
	return false;
}

function getCurrentRoomName(){
	return document.getElementById("chatRoomForm").elements["roomName"].value;
}

function getNewMessage(){
	return document.getElementById("chatRoomForm").elements["newChatRoomMessage"].value;
}

function getNewChatRoomName(){
	return document.getElementById("createNewChatRoom").elements["newChatRoomName"].value;
}

function refreshChatRoomsList(){
	document.getElementById("chatRooms").innerHTML = getChatRooms();
}

function refreshChatRoomUsers(){
	loading(document.getElementById("users"));
	document.getElementById("users").innerHTML = getChatRoomUsers(getCurrentRoomName());
}

function refreshChatRoomsPanel(){
	if( !document.getElementById("chatRooms"))
		loadChatRoomsPanel(document.getElementById("chatRoomsPanel"));
	refreshChatRoomsList();
}

function refreshChatRoomMessages(){
	document.getElementById("chatRoomMessages").innerHTML = 
		getChatRoomMessages(getCurrentRoomName());
}

function openChatRoom(roomName){
	
	if( getCurrentRoomName() != null ){
		logoutOfChatRoom(getCurrentRoomName());
	}
	
	loginToChatRoom(roomName);
	
	//register ICEpush listener on room name
	var usersPushId = ice.push.createPushId();
	ice.push.addGroupMember(roomName+"_users", usersPushId);
	ice.push.register([usersPushId], window.refreshChatRoomUsers);
	
	var messagesPushId = ice.push.createPushId();
	ice.push.addGroupMember(roomName+"_messages", messagesPushId);
	ice.push.register([messagesPushId], window.refreshChatRoomMessages);
	
	document.getElementById("currentChatRoom").innerHTML = document.getElementById("chatRoomTemplate").innerHTML;
	document.getElementById("chatRoomForm").elements["roomName"].value = roomName;
	document.getElementById("currentChatRoomName").innerHTML = roomName;
	refreshChatRoomUsers();
	refreshChatRoomMessages();
}

function refreshChatRoomUsersAndMessages(){
	refreshChatRoomUsers();
	refreshChatRoomMessages();
}

function refreshChatPanel(){
	if( ! document.getElementById("chatPanel"))
		loadChatPanel(document.getElementById("content"));
	refreshChatRoomsPanel();
}


/****************** UTIL ***********************************/
function getXmlHttpRequest() {
	try {
		return new XMLHttpRequest(); // Firefox, Opera 8.0+, Safari
	} catch (e) {
		try {
			return new ActiveXObject("Msxml2.XMLHTTP"); // Internet Explorer
		} catch (e) {
			try {
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				alert("Your browser is too old for AJAX!");
				return null;
			}
		}
	}
}

function loading(element){
	if( element ){
		var newChild = document.createElement("div");
		newChild.setAttribute("style","loading");
		element.innerHTML = "";
		element.appendChild(newChild);
	}	
}

function doGet(url,element){
	loading(element);
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			element.innerHTML = xmlHttp.responseText;
		}
	}
	xmlHttp.open("GET", url, true);
	xmlHttp.send(null);
}

function doGetAndEval(url,element){
	loading(element);
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			element.innerHTML = xmlHttp.responseText;
			evaluateScripts(element);
		}
	}
	xmlHttp.open("GET", url, true);
	xmlHttp.send(null);
}

function evaluateScripts(element) {
	var scripts = element.getElementsByTagName('script');
	for ( var i = 0 ; i < scripts.length; i++ ){
		eval(scripts[i].innerHTML);
	} 
}

