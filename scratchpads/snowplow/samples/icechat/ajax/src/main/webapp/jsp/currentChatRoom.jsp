<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>       
<div class="chatRoomView">
	<form id="chatRoomForm">
		<input type="hidden" id="roomName"/>
		<div class="chatRoomHeader">
			Chat Room '<span id="currentChatRoomName"></span>'
		</div>
		<div class="chatRoomContainer">
			<div class="chatRoomUsers">
				<div class="chatViewSubHeader">Who's Here?</div>
				<span id="currentChatRoomUsers"></span>
			</div>
			<div class="chatRoomMessages">
				<div class="chatViewSubHeader">Messages</div>
				<span id="currentChatRoomMessages"></span>
			</div>
		</div>
		<div class="clearer"></div>
		<div class="addNewMessage">
			New Message&nbsp;
			<input type="text" id="newChatRoomMessage" style="width:40%;" />
			<input type="button" onclick="sendNewChatRoomMessage();"
				value="Send" />
		</div>
	</form>
</div>
