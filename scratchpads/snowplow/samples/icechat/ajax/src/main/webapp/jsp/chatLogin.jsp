<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>       
<form id="chatSession">
	You are logged in as
	<span id="displayName"><%=session.getAttribute("user")%></span>  <input type="button" value="Logout" onclick="window.logout();return false;" />
</form>