<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
    <body>
        <div id="contestUploadContent">
            <mobi:pagePanel>
                <mobi:pagePanelHeader>
                    <a id="backBtn"class="mobi-button mobi-button-default"
                        href='<c:url value="/contest?p=gallery&l=${layout}"/>'>Gallery</a>
                    <span>ICEmobile Contest</span>
                    <img src="./resources/images/icemobile.png"/>
                </mobi:pagePanelHeader>
                <mobi:pagePanelBody>
                    <div id="carouselContainer">
                      <push:region group="photos" page="/contest-carousel?l=m"/>
                     </div>
                    <div style="clear:both;"></div>
                    
                    <%@ include file="/WEB-INF/jsp/contest-upload-form.jsp" %>
                    
                </mobi:pagePanelBody>
                <%@ include file="/WEB-INF/jsp/contest-footer.jsp"%>
            </mobi:pagePanel>
        </div>
    </body>
</html>
</c:if>
<c:if test="${ajaxRequest}">
    <%@ include file="/WEB-INF/jsp/contest-upload-form.jsp" %>
</c:if>