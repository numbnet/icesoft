<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div class="chatViewSubHeader">Messages</div>
<table width="100%" cellspacing="2" cellpadding="2">
<c:forEach var="msg" items="${chat.currentChatSessionHolder.session.room.messages}">
    <tr><td>
        [<fmt:formatDate value="${msg.created}" type="both" dateStyle="short" timeStyle="short"/>]
        <b><c:out value="${msg.userChatSession.user.name}: "/></b><c:out value="${msg.message}"/>
    </td></tr>
</c:forEach>
</table>
