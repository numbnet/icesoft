<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div>
<form:form id="galleryFrm" method="POST" modelAttribute="galleryModel">
    <input type="hidden" name="layout" value="${layout}"/>
    <input type="hidden" name="form" value="gallery"/>
    <input id="updated" type="hidden" name="updated" value="${updated}"/>
    <c:if test="${layout eq 'm' }">
        <input type="hidden" name="p" value="gallery"/>
    </c:if>
    <mobi:outputList inset="false" id="galleryList">
        <c:forEach var="m" items="${mediaService.mediaSortedByVotes}">
            <mobi:outputListItem>
                <div id="${m.id}" data-lastvote="${m.lastVote}" data-created="${m.created}"
                    data-votes="${m.numberOfVotes}">
                    <c:if test="${layout eq 'm'}">
                        <a class="mediaLink" href='<c:url value="/contest?p=viewer&l=${layout}&id=${m.id}"/>'>
                            <img src='/uploads/${m.smallPhoto.file.name}' class="p"/>
                        </a>
                    </c:if>
                    <c:if test="${layout ne 'm'}">
                        <a class="mediaLink" href="#" onclick="updateViewerPanel('${m.id}');">
                            <img src='/uploads/${m.smallPhoto.file.name}' class="p"/>
                        </a>
                    </c:if>
                    <c:if test="${!fn:contains(m.votesAsString, voterId)}">
                        <input type="image" class="vote" title="Vote for it!" 
                            src="<c:url value="/resources/css/css-images/like.png"/>"
                            name="photoId" value="${m.id}"/>
                    </c:if>
                    <span class="desc"><c:out value="${m.description}"/></span>
                    <span class="vote" >${m.numberOfVotes} Votes</span>
                 </div>
            </mobi:outputListItem>
        </c:forEach>
    </mobi:outputList>
    
</form:form>
<script type="text/javascript">
    enhanceForm("#galleryFrm","#galleryFrm");
</script>
<push:register group="photos" callback="function(){getGalleryUpdate();}"/>
</div>