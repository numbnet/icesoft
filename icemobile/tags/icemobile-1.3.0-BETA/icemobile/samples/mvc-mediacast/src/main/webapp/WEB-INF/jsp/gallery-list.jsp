<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<mobi:outputList inset="false" id="galleryList">
    <c:forEach var="m" items="${mediaService.mediaListSortedByTime}">
        <mobi:outputListItem>
            <div id="${m.id}".0
             data-created="${m.created}">
                <mobi:smallView>
                    <a class="mediaLink" href='<c:url value="/app?page=viewer&id=${m.id}"/>'>
                        <c:if test="${not empty m.smallPhoto}">
                            <img src='resources/uploads/${m.smallPhoto.name}' class="p"/>
                        </c:if>
                        <c:if test="${empty m.smallPhoto && not empty m.video}">
                            <img src='resources/images/movieIcon.png' class="p"/>
                        </c:if>
                        <c:if test="${empty m.smallPhoto && empty m.video && not empty m.audio}">
                            <img src='resources/images/soundIcon.png' class="p"/>
                        </c:if>
                        <span class="desc"><c:out value="${m.description}"/></span>
                    </a>
                </mobi:smallView>
                <mobi:largeView>
                    <a class="mediaLink" href="#" onclick="updateViewerPanel('${m.id}');">
                        <c:if test="${not empty m.smallPhoto}">
                            <img src='resources/uploads/${m.smallPhoto.name}' class="p"/>
                        </c:if>
                        <c:if test="${empty m.smallPhoto && not empty m.video}">
                            <img src='resources/images/movieIcon.png' class="p"/>
                        </c:if>
                        <c:if test="${empty m.smallPhoto && empty m.video && not empty m.audio}">
                            <img src='resources/images/soundIcon.png' class="p"/>
                        </c:if>
                        <span class="desc"><c:out value="${m.description}"/></span>
                    </a>
                </mobi:largeView>
             </div>
        </mobi:outputListItem>
    </c:forEach>
</mobi:outputList>
<push:register group="photos" callback="function(){getGalleryUpdate();}"/> 
