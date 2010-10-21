<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%--
 *
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
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
  --%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="icep" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<jsp:useBean id="chatService" class="org.icepush.samples.icechat.jsp.beans.SynchronizedChatServiceBean" scope="application"/>
<html>
    <head>
        <title>ICEpush ICEchat (JSP)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Expires" content="-1">
        <link rel='stylesheet' type='text/css' href='./css/style-common.css'/>
        <script type="text/javascript" src="code.icepush"></script>
        <script type="text/javascript">
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
        function logout() {
        	var xmlHttp = getXmlHttpRequest();
        	xmlHttp.onreadystatechange = function() {
        		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
        			window.location.href = './login.jsp';
        		}
        	}
        	var params = "op=logout";
        	xmlHttp.open("POST", "auth", true);
        	xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        	xmlHttp.send(params);	
        }
        function click_createChatRoom(){
        	var roomName = getNewChatRoomName();
        	createChatRoom(roomName);
        	document.getElementById("createNewChatRoom").elements["newChatRoomName"].value = '';
        	loginToChatRoom(roomName);
        	window.location.reload();
        }
        function getNewChatRoomName(){
        	return document.getElementById("createNewChatRoom").elements["newChatRoomName"].value;
        }
        function createChatRoom(roomName) {
        	var xmlHttp = getXmlHttpRequest();
        	var params = "roomName=" + roomName;
        	xmlHttp.open("POST", "./inc/createNewChatRoom.jsp", false);
        	xmlHttp.setRequestHeader("Content-type",
        			"application/x-www-form-urlencoded");
        	xmlHttp.send(params); 
        }
        function loginToChatRoom(roomName){
        	var xmlHttp = getXmlHttpRequest();
        	var params = "roomName=" + roomName;
        	xmlHttp.open("POST", "./inc/loginToChatRoom.jsp", false);
        	xmlHttp.setRequestHeader("Content-type",
        			"application/x-www-form-urlencoded");
        	xmlHttp.send(params);	
        }
        function logoutOfChatRoom(roomName){
        	if( roomName != null ){
        		var xmlHttp = getXmlHttpRequest();
        		var params = "roomName=" + roomName;
        		xmlHttp.open("POST", "./inc/logoutOfChatRoom.jsp", false);
        		xmlHttp.setRequestHeader("Content-type",
        				"application/x-www-form-urlencoded");
        		xmlHttp.send(params);		
        	}	
        }
        function click_sendMessage(){
        	sendMessage(getNewMessage());
        	document.getElementById("chatRoomForm").elements["newChatRoomMessage"].value = '';
        }
        function sendMessage(msg) {
        	var xmlHttp = getXmlHttpRequest();
        	var params = "msg=" + msg;
        	xmlHttp.open("POST", "./inc/createNewMessage.jsp", false);
        	xmlHttp.setRequestHeader("Content-type",
        			"application/x-www-form-urlencoded");
        	xmlHttp.send(params);
        }
        function getNewMessage(){
        	return document.getElementById("chatRoomForm").elements["newChatRoomMessage"].value;
        }
        function updateDraft(){
        	var xmlHttp = getXmlHttpRequest();
        	var params = "msg=" + getNewMessage();
        	xmlHttp.open("POST", "./inc/updateDraft.jsp", false);
        	xmlHttp.setRequestHeader("Content-type",
        			"application/x-www-form-urlencoded");
        	xmlHttp.send(params);
        }
                                                                                          
        </script>
    </head>
    <body>
    	
    	<div id="headerContainer" class="header_container">
     		<div class="header_content">
				<div style="font-size: 20pt; color: #0F65B8; margin-top: 15px;">
					<img src="./img/ICEpush-JSP.png" title="ICEpush with JSP"/>
					<img src="./img/java-duke-logo.png" width="82px" style="float:right;margin-top:10px;margin-right:25px;"/>
				</div>
			</div> 		
    	</div>
        
        <div class="body_container body_container_pos" id="content"> 
            <div id="chatPanel">
			
				<div class="chatSession chatSession_pos" id="chatSessionPanel">
					<form id="chatSession">
						You are logged in as
						<span id="displayName"><%=session.getAttribute("user")%></span>  <input type="button" value="Logout" onclick="window.logout();return false;" />
					</form>
				</div>
				
				<div class="clearer"></div>
			
				<div class="mainPanel_pos">
				
					<div id="chatRoomsPanel" class="chatRooms chatRooms_pos">
						<h3>Chat Rooms</h3>
						<icep:region group="rooms" page="/inc/chatRooms.jsp"/>
						<form id="createNewChatRoom">
							<div class="createNewChatRoom">
								Create New Chat Room
								<br />
								<label for="newChatRoomName">Name</label>&nbsp;
								<input type="text" id="newChatRoomName" style="width: 90px;margin-top:5px;" 
									onkeypress="if(event.keyCode == 13){ click_createChatRoom();return false;}"/>
								<input type="button" value="Create" style="margin-top:5px;"
									onclick="click_createChatRoom();" />
							</div>
						</form>
					</div>
				
					<div id="currentChatRoom" class="currentChatRoom_pos">
<%
	if( session.getAttribute("currentChatRoom") != null ){
%>
						<div class="chatRoom" id="chatRoom">
							<form id="chatRoomForm">
								<div class="chatRoomHeader">
									Chat Room '${sessionScope['currentChatRoom'].name}'
								</div>
								<div class="chatRoomContainer">
									<div class="chatRoomUsers">
										<div class="chatViewSubHeader">Who's Here?</div>
										<icep:region group="${sessionScope['currentChatRoom'].name}_users" page="/inc/chatRoomUsers.jsp">
										</icep:region>
									</div>
									<div class="chatRoomMessages">
										<div class="chatViewSubHeader">Messages</div>
										<div>
										<icep:region group="${sessionScope['currentChatRoom'].name}_messages" page="/inc/chatRoomMessages.jsp"
											evalJS="false">
										</icep:region>
										</div>
									</div>
								</div>
								<div class="clearer"></div>
								<div class="addNewMessage">
									New Message&nbsp;
									<input type="text" id="newChatRoomMessage" style="width:40%;"
										onkeyup="window.updateDraft();"
										onkeypress="if(event.keyCode == 13){ click_sendMessage(); return false;}"/>
									<input type="button" onclick="click_sendMessage();return false;"
										value="Send" />
								</div>
							</form>
						</div>  			
<%
	}
%>					</div>
				</div>
			</div>
        </div>
    </body>
</html>

