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
                    <img src='<c:url value="/resources/uploads/${m.photo.fileName}"/>' class="p"/>
                    <span class="desc"><c:out value="${m.description}"/></span>
                    <span class="vote" >${m.numberOfVotes} Votes</span>
                </a>
                <c:if test="${edit}">
                    <a class="editLink" href='<c:url value="/contest?p=${view eq 'all' ? 'all' : 'gallery'}&l=${layout}&id=${m.id}&a=d"/>'>X</a>
                </c:if>
            </mobi:outputListItem>
        </c:forEach>
    </mobi:outputList>
    
</form:form>