<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/head.jsp"%>
    <body>
</c:if>
        <div id="root">
            <mobi:pagePanel>
                <mobi:pagePanelHeader>
                    <a id="backBtn"class="mobi-button mobi-button-default"
                        href='<c:url value="/app?p=gallery&l=${layout}"/>'>Gallery</a>
                    <span>Mediacast</span>
                    <img src="./resources/images/icemobile.png" style="position: absolute;top: 10px;right: 2em;"/>
                </mobi:pagePanelHeader>
                <mobi:pagePanelBody>
                    <div id="carouselContainer">
                      <push:region group="photos" page="/carousel?l=m"/>
                     </div>
                    <div style="clear:both;"></div>
                    
                    <%@ include file="/WEB-INF/jsp/upload-form.jsp" %>
                    
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