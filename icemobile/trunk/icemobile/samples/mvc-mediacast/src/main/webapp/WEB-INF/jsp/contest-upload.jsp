<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
</c:if>
<div class="ajaxzone">
    <mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn"class="mobi-button mobi-button-default"
				href='<c:url value="/contest?p=gallery&l=${layout}"/>'>Gallery</a>
            <span>Contest Upload</span>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
		    <push:region group="carousel" page="/contest-carousel"/>
			<div style="clear:both;"></div>
			
			<%@ include file="/WEB-INF/jsp/contest-upload-form.jsp" %>
			
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/contest-footer.jsp"%>
	</mobi:pagePanel>
</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>