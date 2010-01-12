
/****************** SECURITY AND AUTHORIZATION ***********************************/

function login() {
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			window.location.reload();
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
	return  "userName=" + loginForm.elements['userName'].value + "&nickName="
			+ loginForm.elements['nickName'].value + "&password=" + loginForm.elements['password'].value;
}

function register() {
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			window.location.reload();
		}
	}
	var form = document.forms['login'];
	var params = "op=register&" + getLoginFormParams();
	xmlHttp.open("POST", "auth", true);
	xmlHttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlHttp.send(params);	
}

function logout() {
	var xmlHttp = getXmlHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			window.location.reload();
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
	loginToChatRoom(roomName);
	
	//register ICEpush listener on room name
	ice.push.addGroupMember(roomName, window.pushId);
	ice.push.register([window.pushId], window.refreshChatRoomUsersAndMessages);
	
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

