<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div id="viewerPanel">
    <c:if test="${not empty media}">
        <div class="lightbox">
            <img id="largePhoto" src='<c:url value="/resources/uploads/${media.largePhoto.file.name}"/>'
                class="imageViewer" />
        </div>
        <div class="message-desc">
            <h3><c:out value="${media.description}"/></h3>
        </div>
        
        <mobi:fieldSetGroup style="margin-top:10px;">
            <mobi:fieldSetRow style="text-align:center;">
                <div>${media.numberOfVotes} Votes so far..</div>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow style="text-align:center;">
                <div>${msg}</div>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
    </c:if>
</div>