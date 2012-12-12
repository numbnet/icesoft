<%@ include file="/WEB-INF/jsp/include.jsp"%>
<mobi:outputList inset="false" id="galleryList">
    <c:forEach var="m" items="${mediaService.mediaListSortedByTime}">
        <mobi:outputListItem>
            <div id="${m.id}" data-created="${m.created}">
                <c:if test="${layout eq 'm'}">
                    <a class="mediaLink" href='<c:url value="/app?p=viewer&l=${layout}&photoId=${m.id}"/>'>
                        <img src='resources/uploads/${m.smallPhoto.name}' class="p"/>
                    </a>
                </c:if>
                <c:if test="${layout ne 'm'}">
                    <a class="mediaLink" href="#" onclick="updateViewerPanel('${m.id}');">
                        <img src='resources/uploads/${m.smallPhoto.name}' class="p"/>
                    </a>
                </c:if>
                
                <span class="desc"><c:out value="${m.description}"/></span>
             </div>
        </mobi:outputListItem>
    </c:forEach>
</mobi:outputList>
<push:register group="photos" callback="function(){getGalleryUpdate();}"/>
