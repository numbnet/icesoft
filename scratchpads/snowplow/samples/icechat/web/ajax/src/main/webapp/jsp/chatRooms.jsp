<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>       
<h3>Chat Rooms</h3>
<form id="chatRooms">
	<div class="loading"></div>
	<script type="text/javascript">getChatRoomList();</script>
</form>
<form id="createNewChatRoom">
	<div class="createNewChatRoom">
		Create New Chat Room
		<br />
		<label for="newChatRoomName">Name</label>&nbsp;
		<input type="text" id="newChatRoomName" style="width: 100px;margin-top:5px;" />
		<input type="button" value="Create" style="margin-top:5px;"
			onclick="createNewChatRoom();" />
	</div>
</form>