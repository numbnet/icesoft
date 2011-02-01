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
<html>
<head>
  <title>ICEpush ICEchat (Grails)</title>
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
  <meta http-equiv="X-UA-Compatible" content="IE=100" > <!-- IE8 mode -->
  <link rel='stylesheet' type='text/css' href='${resource(dir: "css", file: "style-common.css")}'/>
  <icep:bridge />
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
          window.location.href = '${createLink(controller:'login')}';
        }
      }
      var params = "op=logout";
      xmlHttp.open("POST", "${createLink(controller:'login',action:'auth')}", true);
      xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      xmlHttp.send(params);
    }
    function click_createChatRoom() {
      var roomName = getNewChatRoomName();
      createChatRoom(roomName);
      document.getElementById("createNewChatRoom").elements["newChatRoomName"].value = '';
      loginToChatRoom(roomName);
      window.location.reload();
    }
    function getNewChatRoomName() {
      return document.getElementById("createNewChatRoom").elements["newChatRoomName"].value;
    }
    function createChatRoom(roomName) {
      var xmlHttp = getXmlHttpRequest();
      var params = "roomName=" + roomName;
      xmlHttp.open("POST", "${createLink(action:'createNewChatRoom')}", false);
      xmlHttp.setRequestHeader("Content-type",
              "application/x-www-form-urlencoded");
      xmlHttp.send(params);
    }
    function loginToChatRoom(roomName) {
      var xmlHttp = getXmlHttpRequest();
      var params = "roomName=" + roomName;
      xmlHttp.open("POST", "${createLink(action:'loginToChatRoom')}", false);
      xmlHttp.setRequestHeader("Content-type",
              "application/x-www-form-urlencoded");
      xmlHttp.send(params);
    }
    function logoutOfChatRoom(roomName) {
      if (roomName != null) {
        var xmlHttp = getXmlHttpRequest();
        var params = "roomName=" + roomName;
        xmlHttp.open("POST", "${createLink(action:'logoutOfChatRoom')}", false);
        xmlHttp.setRequestHeader("Content-type",
                "application/x-www-form-urlencoded");
        xmlHttp.send(params);
      }
    }
    function click_sendMessage() {
      sendMessage(getNewMessage());
      document.getElementById("chatRoomForm").elements["newChatRoomMessage"].value = '';
    }
    function sendMessage(msg) {
      var xmlHttp = getXmlHttpRequest();
      var params = "msg=" + msg;
      xmlHttp.open("POST", "${createLink(action:'createNewMessage')}", false);
      xmlHttp.setRequestHeader("Content-type",
              "application/x-www-form-urlencoded");
      xmlHttp.send(params);
    }
    function getNewMessage() {
      return document.getElementById("chatRoomForm").elements["newChatRoomMessage"].value;
    }
    function updateDraft() {
      var xmlHttp = getXmlHttpRequest();
      var params = "msg=" + getNewMessage();
      xmlHttp.open("POST", "${createLink(action:'updateDraft')}", false);
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
     <img src="${resource(dir: "img", file: "ICEpush-grails.png")}" title="ICEpush with Grails"/>
      <img src="${resource(dir: "images", file: "grails-logo.png")}" width="82px" style="float:right;margin-top:10px;margin-right:25px;"/>
    </div>
  </div>
</div>

<div class="body_container body_container_pos" id="content">
  <div id="chatPanel">

    <div class="chatSession chatSession_pos" id="chatSessionPanel">
      <form id="chatSession">
        You are logged in as
        <span id="displayName"><%=session["user"]%></span>  <input type="button" value="Logout" onclick="window.logout();
      return false;"/>
      </form>
    </div>

    <div class="clearer"></div>

    <div class="mainPanel_pos">

      <div id="chatRoomsPanel" class="chatRooms chatRooms_pos">
        <h3>Chat Rooms</h3>
        <icep:region group="rooms" action="chatRooms"/>
        <form id="createNewChatRoom">
          <div class="createNewChatRoom">
            Create New Chat Room
            <br/>
            <label for="newChatRoomName">Name</label>&nbsp;
            <input type="text" id="newChatRoomName" style="width: 90px;margin-top:5px;"
                    onkeypress="if (event.keyCode == 13) {
                      click_createChatRoom();
                      return false;
                    }"/>
            <input type="button" value="Create" style="margin-top:5px;"
                    onclick="click_createChatRoom();"/>
          </div>
        </form>
      </div>

      <div id="currentChatRoom" class="currentChatRoom_pos">
        <%
          if (session["currentChatRoom"]) {
        %>
        <div class="chatRoom" id="chatRoom">
          <form id="chatRoomForm">
            <div class="chatRoomHeader">
              Chat Room '${session['currentChatRoom'].name}'
            </div>
            <div class="chatRoomContainer">
              <div class="chatRoomUsers">
                <div class="chatViewSubHeader">Who's Here?</div>
                <icep:region group="${session['currentChatRoom'].name}_users" action="chatRoomUsers" />
              </div>
              <div class="chatRoomMessages">
                <div class="chatViewSubHeader">Messages</div>
                <div>
                  <icep:region group="${session['currentChatRoom'].name}_messages" action="chatRoomMessages" />
                </div>
              </div>
            </div>
            <div class="clearer"></div>
            <div class="addNewMessage">
              New Message&nbsp;
              <input type="text" id="newChatRoomMessage" style="width:40%;"
                      onkeyup="window.updateDraft();"
                      onkeypress="if (event.keyCode == 13) {
                        click_sendMessage();
                        return false;
                      }"/>
              <input type="button" onclick="click_sendMessage();
              return false;"
                      value="Send"/>
            </div>
          </form>
        </div>
        <%
          }
        %></div>
    </div>
  </div>
</div>
</body>
</html>

