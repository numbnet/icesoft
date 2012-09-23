<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" class="mobi-button mobi-button-default" style="float:left;line-height:20px;"
				href='<c:url value="/contest?p=upload&l=${layout}"/>'>Upload</a>
			<span>Contest Gallery</span>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
		    <push:region group="photos" page="/contest-photo-list"/>
		    
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>