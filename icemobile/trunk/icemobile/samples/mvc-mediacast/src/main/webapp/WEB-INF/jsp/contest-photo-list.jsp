<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<form:form id="galleryFrm" method="POST" modelAttribute="galleryModel">
    <mobi:outputList inset="false" id="galleryList">
        <c:forEach var="media" items="${mediaService.mediaSortedByVotes}">
            <mobi:outputListItem>
                <a class="mediaLink" href='<c:url value="/contest-uploads/${media.id}"/>'>
                    <img height="50px" 
                        src='<c:url value="/resources/uploads/${media.photo.fileName}"/>'
                        style="padding-right:5px;border:none;float:left;" />
                    <span class="desc"><c:out value="${media.description}"/></span>
                    <span class="vote" >${media.numberOfVotes} Votes</span>
                </a>
            </mobi:outputListItem>
        </c:forEach>
    </mobi:outputList>
</form:form>