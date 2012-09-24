<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<form:form id="galleryFrm" method="POST" modelAttribute="galleryModel">
    <input type="hidden" name="layout" value="${layout}"/>
    <input type="hidden" name="form" value="gallery"/>
    <c:if test="${layout eq 'm' }">
        <input type="hidden" name="p" value="gallery"/>
    </c:if>
    <mobi:outputList inset="false" id="galleryList">
        <c:forEach var="m" items="${mediaService.mediaSortedByVotes}">
            <mobi:outputListItem>
                    <c:if test="${layout eq 'm'}">
                        <a class="mediaLink" href='<c:url value="/contest?p=viewer&l=${layout}&id=${m.id}"/>'>
                            <img src='<c:url value="/resources/uploads/${m.smallPhoto.file.name}"/>' class="p"/>
                        </a>
                    </c:if>
                    <c:if test="${layout ne 'm'}">
                        <a class="mediaLink" href="#" onclick="updateViewerPanel('${m.id}');">
                            <img src='<c:url value="/resources/uploads/${m.smallPhoto.file.name}"/>' class="p"/>
                        </a>
                    </c:if>
                    <input type="image" class="vote" title="Vote for it!" 
                        src="<c:url value="/resources/css/css-images/like.png"/>"
                        name="photoId" value="${m.id}"></input>
                    
                    <span class="desc"><c:out value="${m.description}"/></span>
                    <span class="vote" >${m.numberOfVotes} Votes</span>
            </mobi:outputListItem>
        </c:forEach>
    </mobi:outputList>
    
</form:form>
<script type="text/javascript">
    enhanceForm("#galleryFrm","#galleryFrm");
</script>