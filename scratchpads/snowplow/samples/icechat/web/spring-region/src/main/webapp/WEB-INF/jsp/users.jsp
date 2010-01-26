<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div class="chatViewSubHeader">Who's Here?</div>
<table>
<c:forEach var="chatSession" items="${chat.currentChatSessionHolder.session.room.userChatSessions}">
    <tr><td>
    <c:out value="${chatSession.user.displayName}"/>
    </td></tr>
</c:forEach>
</table>
