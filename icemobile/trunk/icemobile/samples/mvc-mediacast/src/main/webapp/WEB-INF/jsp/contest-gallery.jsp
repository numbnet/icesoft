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
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>