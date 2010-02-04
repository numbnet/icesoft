<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>  
<%@page import="java.util.*,org.icepush.samples.icechat.model.*,org.icepush.samples.icechat.ajax.servlet.*" %>
<%
	List<ChatRoom> rooms = ((SynchronizedChatServiceBean)application.getAttribute("chatService")).getChatRooms();
	if( rooms != null && rooms.size() > 0 ){
		for( ChatRoom room : rooms ){
%>
	<div>
		<link href='chat/openchatroom?name=<%=room.getName()%>'><%=room.getName()%></link>
	</div>
<%
		}
	}
	else{
%>
	<div>No rooms available</div>
<%		
	}
%>    
