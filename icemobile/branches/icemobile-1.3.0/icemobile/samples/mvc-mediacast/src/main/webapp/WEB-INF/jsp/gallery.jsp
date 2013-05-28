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
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" class="mobi-button"
				href='<c:url value="/media"/>'>Upload</a>
			<span>Photo Gallery</span>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			 <form:form id="filterForm" method="POST" modelAttribute="galleryModel">
		             
		         <mobi:fieldsetGroup inset="true">
		            <mobi:fieldsetRow>
		                <label>Enter some tags to filter the messages</label>
		            </mobi:fieldsetRow>
		            <mobi:fieldsetRow>
		                <mobi:inputText name="filters" 
	                         	styleClass="input"
	                         	type="text"
                                autoCorrect="off"
                                placeholder="Enter some tags.."
                                value="${galleryModel.filterString}"/>
		                <mobi:commandButton name="submit"
	                                style="margin: 0;padding: 6px 8px;"
	                                value="Search"/>
		            </mobi:fieldsetRow>
		        </mobi:fieldsetGroup>
		        
		        <mobi:fieldsetGroup inset="true">
		            <mobi:fieldsetRow>
		                <label>Or select a tag from the cloud to filter</label>
		            </mobi:fieldsetRow>
		            <mobi:fieldsetRow>
		                <ul class="tagCloud">
		                    <c:forEach var="tag" items="${galleryModel.tags}">
		                        <li>
		                            <a href='<c:url value="/media/gallery?filters=${tag}"/>' style="font-size:${galleryModel.tagsMap[tag]}pt">
		                            ${tag}</a>
		                        </li>
		                    </c:forEach>
		                </ul>
		            </mobi:fieldsetRow>
		        </mobi:fieldsetGroup>
		        
		        <mobi:fieldsetGroup inset="true">
		            <mobi:fieldsetRow>
		                <span id="msgCount">Found ${galleryModel.filteredMessagesCount} messages</span>
		            </mobi:fieldsetRow>
		        </mobi:fieldsetGroup>
		    </form:form>
		    
		    <form:form id="galleryFrm" method="POST" modelAttribute="galleryModel">
		        <mobi:outputList inset="false" id="galleryList">
		            <c:forEach var="media" items="${galleryModel.filteredMessages}">
		                <mobi:outputListItem>
		                    <a id="mediaLink" href='<c:url value="/media/${media.id}"/>'>
		                    	<img id="largePhoto" width="50px" 
									src='<c:url value="/resources/uploads/${media.largePhoto.name}"/>'
									class="imageViewer" style="padding-right:5px;border:none;" />
		                        <span>${media.title}</span>
							</a>
		                </mobi:outputListItem>
		            </c:forEach>
		        </mobi:outputList>
		            
		    </form:form>

		    
		    
			
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>