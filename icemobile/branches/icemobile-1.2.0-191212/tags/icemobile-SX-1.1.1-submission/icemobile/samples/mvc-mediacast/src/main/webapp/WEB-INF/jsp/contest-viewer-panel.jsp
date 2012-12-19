<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<img id="largePhoto" width="100%"
    src='<c:url value="/resources/uploads/${media.photo.fileName}"/>'
    class="imageViewer" />
<div class="message-desc">
    <h3><c:out value="${media.description}"/></h3>
</div>

<form:form id="voteForm" method="POST">
    <mobi:fieldSetGroup style="margin-top:10px;">
        <mobi:fieldSetRow style="text-align:center;">
            <div>${media.numberOfVotes} Votes so far..</div>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow style="text-align:center;">
            <div>${msg}</div>
            <mobi:commandButton buttonType="important"
                value="Vote for it!"
                style="width: 100px; margin-top: 5px; margin-bottom: 0;"/>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>
</form:form>