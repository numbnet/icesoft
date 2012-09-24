<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<form:form id="galleryFrm" method="POST" modelAttribute="galleryModel">
    <mobi:outputList inset="false" id="galleryList">
        <c:forEach var="m" items="${mediaService.mediaSortedByVotes}">
            <mobi:outputListItem>
            <a class="mediaLink" 
            <c:if test="${layout eq 'm'}">
                href='<c:url value="/contest?p=viewer&l=${layout}&id=${m.id}"/>'>
            </c:if>
            <c:if test="${layout ne 'm'}">
                href="#" onclick="updateViewerPanel('${m.id}');">
            </c:if>
                    <img src='<c:url value="/resources/uploads/${m.smallPhoto.file.name}"/>' class="p"/>
                    <span class="desc"><c:out value="${m.description}"/></span>
                    <span class="vote" >${m.numberOfVotes} Votes</span>
                </a>
            </mobi:outputListItem>
        </c:forEach>
    </mobi:outputList>
    
</form:form>