<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/head.jsp"%>
<body>
</c:if>
    <div class="root">
    	<mobi:pagePanel>
    		<mobi:pagePanelHeader>
    			<a id="backBtn" class="mobi-button mobi-button-default" style="float:left;line-height:20px;"
    				href='<c:url value="/app?p=upload&l=${layout}"/>'>Upload</a>
    			<span>Mediacast</span>
                <img src="./resources/images/icemobile.png" style="position: absolute;top: 10px;right: 2em;"/>
    		</mobi:pagePanelHeader>
    		<mobi:pagePanelBody>
    		    <%@ include file="/WEB-INF/jsp/photo-list.jsp" %>
            </mobi:pagePanelBody>
    		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
    	</mobi:pagePanel>
    </div>
    <script type="text/javascript">
        enhanceGet("#backBtn");
    </script>
<c:if test="${!ajaxRequest}">
    </body>
</html>
</c:if>