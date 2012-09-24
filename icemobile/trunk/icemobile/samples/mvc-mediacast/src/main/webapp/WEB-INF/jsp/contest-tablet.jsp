<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
    <%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
    <mobi:pagePanel>
        <mobi:pagePanelHeader>
            
            <img src="./resources/images/icemobile_thumb.png"
                style="vertical-align:text-bottom;"/>
            Mediacast Contest
        </mobi:pagePanelHeader>
        <mobi:pagePanelBody>
            
            <div id="left" style="width:30%;float:left;">
                <div id="photoListContainer">
                    <push:region group="photos" page="/contest-photo-list?l=${layout}"/>
                </div>
            </div>
            <div id="right" style="width:70%;float:left;border-left:1px solid darkgray;box-sizing:border-box;-moz-border-sizing:border-box;">
                <mobi:tabset id="tabs" selectedTab="0">
	                <mobi:headers>
	                    <mobi:header><span class="mobitab">Upload</span></mobi:header>
	                    <mobi:header><span class="mobitab">Viewer</span></mobi:header>
	                </mobi:headers>
	                <mobi:content>
	                   <mobi:contentPane>
                            <div id="carouselContainer">
                                <push:region group="carousel" page="/contest-carousel"/>
                             </div>
	                       <%@ include file="/WEB-INF/jsp/contest-upload-form.jsp" %>
	                   </mobi:contentPane>
	                   <mobi:contentPane>
	                       <%@ include file="/WEB-INF/jsp/contest-viewer-panel.jsp" %>
                       </mobi:contentPane>
                    </mobi:content>
                </mobi:tabset>
            </div>
         </mobi:pagePanelBody>
        <%@ include file="/WEB-INF/jsp/contest-footer.jsp"%>
    </mobi:pagePanel>
    <script type="text/javascript">
     addResizeAfterUpdatesListener('left');
     addResizeAfterUpdatesListener('right');
    </script>
</body>
</html>