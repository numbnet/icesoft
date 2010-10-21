<%--
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
 --%>
 
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