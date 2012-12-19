<%@ page session="false"%>
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
			<%@ include file="/WEB-INF/jsp/contest-viewer-panel.jsp" %>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>