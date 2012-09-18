<%@ page session="true"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" class="mobi-button mobi-button-back"
				href='<c:url value="/contest-gallery"/>'>Gallery</a>
			<span>Contest Viewer</span>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<img id="largePhoto" width="100%"
				src='<c:url value="/resources/uploads/${media.photo.fileName}"/>'
				class="imageViewer" />
			<div class="message-desc">
				<h3><c:out value="${media.description}"/></h3>
			</div>

			<form:form id="voteForm" method="POST">
				<mobi:fieldSetGroup style="margin-top:10px;">
				    <mobi:fieldSetRow style="text-align:center;">
                        <div>${media.numberOfVotes} Votes so far..</div>
                    </mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
					    <div>${msg}</div>
						<mobi:commandButton buttonType="important"
                            value="Vote for it!"
                            style="width: 100px; margin-top: 5px; margin-bottom: 0;"/>
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</form:form>



		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>