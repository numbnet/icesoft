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
<%@page import="org.icepush.PushContext,org.icepush.samples.icechat.model.*"%>
<%@taglib prefix="icep" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<jsp:useBean id="chatService" class="org.icepush.samples.icechat.jsp.beans.SynchronizedChatServiceBean" scope="application"/>
<jsp:useBean id="user" class="org.icepush.samples.icechat.model.User" scope="session"/>
<%
if (user != null) {
	if( session.getAttribute("currentChatRoom") != null )
		chatService.logoutOfChatRoom(((ChatRoom)session.getAttribute("currentChatRoom")).getName(),user);
	ChatRoom chatRoom = chatService.getChatRoom(request.getParameter("roomName"));
	if( chatRoom != null ){
		chatService.loginToChatRoom(chatRoom.getName(), user);
		session.setAttribute("currentChatRoom",chatRoom);
		PushContext pushContext = PushContext.getInstance(getServletContext());
		pushContext.push(chatRoom.getName()+"_users");
	}	
}
%>