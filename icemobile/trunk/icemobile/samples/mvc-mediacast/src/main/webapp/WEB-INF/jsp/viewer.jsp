<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/head.jsp"%>
<body>
    <div id="root">
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" class="mobi-button mobi-button-default"
				href='<c:url value="/app?p=gallery&l=${layout}"/>'>Gallery</a>
			<span>Mediacast</span>
            <img src="./resources/images/icemobile.png" style="position: absolute;top: 10px;right: 2em;"/>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<%@ include file="/WEB-INF/jsp/viewer-panel.jsp" %>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
    </div></body>
</html>