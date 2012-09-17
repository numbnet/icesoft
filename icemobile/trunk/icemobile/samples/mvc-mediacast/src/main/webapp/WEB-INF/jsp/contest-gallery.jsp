<%@ page session="true"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" class="mobi-button mobi-button-back"
				href='<c:url value="/contest-upload"/>'>Upload</a>
			<span>Contest Gallery</span>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			 <form:form id="filterForm" method="POST" modelAttribute="galleryModel">
		             
		         <mobi:fieldSetGroup inset="true">
		            <mobi:fieldSetRow>
		                <label>Enter some tags to filter the messages</label>
		            </mobi:fieldSetRow>
		            <mobi:fieldSetRow>
		                <mobi:inputtext name="filters" 
	                         	styleClass="input"
	                         	type="text"
                                autoCorrect="off"
                                placeholder="Enter some tags.."
                                value="${galleryModel.filterString}"/>
		                <mobi:commandButton name="submit"
	                                style="margin: 0;padding: 6px 8px;"
	                                buttonType="default"
	                                value="Search"/>
		            </mobi:fieldSetRow>
		        </mobi:fieldSetGroup>
		        
		        <mobi:fieldSetGroup inset="true">
		            <mobi:fieldSetRow>
		                <label>Or select a tag from the cloud to filter</label>
		            </mobi:fieldSetRow>
		            <mobi:fieldSetRow>
		                <ul class="tagCloud">
		                    <c:forEach var="tag" items="${galleryModel.tags}">
		                        <li>
		                            <a href='<c:url value="/media/gallery?filters=${tag}"/>' style="font-size:${galleryModel.tagsMap[tag]}pt">
		                            ${tag}</a>
		                        </li>
		                    </c:forEach>
		                </ul>
		            </mobi:fieldSetRow>
		        </mobi:fieldSetGroup>
		        
		        <mobi:fieldSetGroup inset="true">
		            <mobi:fieldSetRow>
		                <span id="msgCount">Found ${galleryModel.filteredMessagesCount} messages</span>
		            </mobi:fieldSetRow>
		        </mobi:fieldSetGroup>
		    </form:form>
		    
		    <form:form id="galleryFrm" method="POST" modelAttribute="galleryModel">
		        <mobi:outputList inset="false" id="galleryList">
		            <c:forEach var="media" items="${galleryModel.filteredMessages}">
		                <mobi:outputListItem>
		                    <a id="mediaLink" href='<c:url value="/media/${media.id}"/>'>
		                    	<img id="largePhoto" width="50px" 
									src='<c:url value="/resources/uploads/${media.photo.fileName}"/>'
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