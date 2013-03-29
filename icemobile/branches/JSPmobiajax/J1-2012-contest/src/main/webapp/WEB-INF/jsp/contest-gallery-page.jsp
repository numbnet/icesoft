<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
</c:if>
    <div class="root">
    	<mobi:pagePanel>
    		<mobi:pagePanelHeader>
    			<a id="backBtn" class="mobi-button mobi-button-default" style="float:left;line-height:20px;"
    				href='<c:url value="/contest?p=upload&l=${layout}"/>'>Upload</a>
    			<span>ICEmobile Contest</span>
                <img src="./resources/images/icemobile.png" />
    		</mobi:pagePanelHeader>
    		<mobi:pagePanelBody>
    		    <%@ include file="/WEB-INF/jsp/contest-photo-list.jsp" %>
            </mobi:pagePanelBody>
    		<%@ include file="/WEB-INF/jsp/contest-footer.jsp"%>
    	</mobi:pagePanel>
    </div>
    <script type="text/javascript">
        enhanceGet("#backBtn");
    </script>
<c:if test="${!ajaxRequest}">
    </body>
</html>
</c:if>