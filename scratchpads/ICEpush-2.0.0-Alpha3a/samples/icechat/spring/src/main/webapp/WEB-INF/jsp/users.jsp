<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<table style="width:100%;">
<c:forEach var="chatSession" items="${chatSessions}">
    <tr><td>
    <div style="float:left"><c:out value="${chatSession.user.name}"/></div>
    <c:if test="${chatSession.user != currentUser}">
    	 <icep:region group="${chatSession.room.id}_${chatSession.user.id}_draft" 
    	page="/rooms/${chatSession.room.id}/users/${chatSession.user.id}/draft"/>
    </c:if>   
    </td></tr>
</c:forEach>
</table>
