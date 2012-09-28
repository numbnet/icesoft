<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" class="mobi-button mobi-button-default" style="float:left;line-height:20px;"
				href='<c:url value="/contest?p=gallery&l=${layout}"/>'>Gallery</a>
			<span>ICEmobile Contest</span><img src="./resources/images/icemobile.png"
                style="position:absolute;right:40px;top:12px;"/>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<%@ include file="/WEB-INF/jsp/contest-viewer-panel.jsp" %>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/contest-footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>