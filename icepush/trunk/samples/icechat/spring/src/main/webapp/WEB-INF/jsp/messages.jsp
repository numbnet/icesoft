<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>

<table width="100%" cellspacing="2" cellpadding="2">
<c:forEach var="msg" items="${messages}" >
    <tr><td>
        [<fmt:formatDate value="${msg.created}" type="both" dateStyle="short" timeStyle="short"/>]
        <b><c:out value="${msg.userChatSession.user.name}: "/></b><c:out value="${msg.message}"/>
    </td></tr>
</c:forEach>
</table>
<script type="text/javascript">
$('#chatRoomMessages td:first').css('font-style','italic').fadeIn(1000);
</script>
