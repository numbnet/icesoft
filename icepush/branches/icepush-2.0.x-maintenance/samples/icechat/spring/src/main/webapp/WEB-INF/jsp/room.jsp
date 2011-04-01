<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<div id="currentChatRoom" class="currentChatRoom_pos">
	<input id="roomId" type="hidden" value="${chatSession.room.id}"/>
	<div class="chatRoom" id="chatRoom">
		<div class="chatRoomHeader">
			<c:out value="Chat Room '${chatSession.room.name}'"/>
		</div>
		<div class="chatRoomContainer">
			<div class="chatRoomUsers">
				<div class="chatViewSubHeader">Who's Here?</div>				
				<icep:region group="${chatSession.room.name}_users" page="/rooms/${chatSession.room.id}/users"/>
			</div>
			<div class="chatRoomMessages">
				<div id="chatRoomMessages">
					<div class="chatViewSubHeader">Messages</div>
					<icep:region group="${chatSession.room.name}_messages" page="/rooms/${chatSession.room.id}/messages"
						evalJS="false"/>
				</div>
			</div>
		</div>
		<div class="clearer"></div>
		<div class="addNewMessage">
			  <fmt:message key="newMessage"/>
                         
             <input id="message" onkeyup="window.updateDraft();" onkeypress="if(event.keyCode == 13){ window.sendMessage(); return false;}"/>
             <button title="Send New Message" onclick="window.sendMessage();return false;" id="sendMessage">Send</button>
                    
		</div>
	</div>        
</div>	