<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<table width="99%" cellspacing="2" cellpadding="2">
    <c:forEach var="room" items="${rooms}">
        <tr>
            <td>
            	<button onclick="joinRoom(${room.id});return false;"
            		style="width:100%;">${room.name}</button>
            </td>
        </tr>
    </c:forEach>
</table>