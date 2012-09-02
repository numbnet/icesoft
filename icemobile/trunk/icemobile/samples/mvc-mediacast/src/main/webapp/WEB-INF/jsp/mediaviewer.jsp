<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>Media Viewer</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<mobi:fieldSetGroup inset="true">
				<mobi:fieldSetRow id="titleRow" style="padding:5px;">
					<h3>${media.title}</h3>
				</mobi:fieldSetRow>
			</mobi:fieldSetGroup>

			<c:if test="${media.showPhoto}">
				<mobi:fieldSetGroup inset="true">
					<mobi:fieldSetRow>
						<label>Photo:</label>
					</mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
						<img id="largePhoto"
							src='<c:url value="/resources/uploads/${media.photo.fileName}"/>'
							class="imageViewer" />
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</c:if>

			<c:if test="${media.showVideo}">
				<mobi:fieldSetGroup inset="true">
					<mobi:fieldSetRow>
						<label>Video:</label>
					</mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
						<video id="videoPlayer"
							src='<c:url value="/resources/uploads/${media.video.fileName}"/>' class="imageViewer"
							controls="controls"></video>
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</c:if>

			<c:if test="${media.showAudio}">
				<mobi:fieldSetGroup inset="true">
					<mobi:fieldSetRow>
						<label>Audio:</label>
					</mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
						<audio id="audioPlayer"
							src='<c:url value="/resources/uploads/${media.audio.fileName}"/>' class="imageViewer"
							controls="controls"></audio>
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</c:if>

			<mobi:fieldSetGroup inset="true">
				<mobi:fieldSetRow>
					<label>Description: </label>
					<c:out value="${media.description}" />
				</mobi:fieldSetRow>
				<mobi:fieldSetRow>
					<label>Tags: </label>
					<c:forEach items="${media.tags}" var="tag">
						<img src='<c:url value="/resources/uploads/images/tag.gif"/>' />
						<span style="vertical-align:top;display:inline-block;margin-right:10px;">
							<c:out value="${tag}"/>&#160;
						</span>
					</c:forEach>
				</mobi:fieldSetRow>
			</mobi:fieldSetGroup>

			<mobi:fieldSetGroup inset="true">
				<mobi:fieldSetRow style="text-align:center;">
					<form:form id="delMediaFrm" method="DELETE">
						<mobi:commandButton name="Delete" buttonType='attention'
                                    value="Delete"/>
					</form:form>
				</mobi:fieldSetRow>
			</mobi:fieldSetGroup>

		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>