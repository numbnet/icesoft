<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
    <%@ include file="/WEB-INF/jsp/head.jsp"%>
<body>
    <mobi:pagePanel>
        <mobi:pagePanelHeader>
            
            <img src="./resources/images/icemobile_thumb.png"/>
            ICEmobile Mediacast
        </mobi:pagePanelHeader>
        <mobi:pagePanelBody>
            
            <mobi:splitPane id="splitPane" scrollable="true" columnDivider="30">
                <mobi:fragment name="left">
                    <%@ include file="/WEB-INF/jsp/photo-list.jsp" %>
                </mobi:fragment>
                <mobi:fragment name="right">
                    <mobi:tabSet id="tabs" selectedTab="0">
                        <mobi:headers>
                            <mobi:header><span class="mobitab">Upload</span></mobi:header>
                            <mobi:header><span class="mobitab">Viewer</span></mobi:header>
                        </mobi:headers>
                        <mobi:content>
                           <mobi:contentPane>
                                <div id="carouselContainer">
                                    <push:region group="photos" page="/carousel?l=t"/>
                                 </div>
                               <%@ include file="/WEB-INF/jsp/upload-form.jsp" %>
                           </mobi:contentPane>
                           <mobi:contentPane>
                               <%@ include file="/WEB-INF/jsp/viewer-panel.jsp" %>
                           </mobi:contentPane>
                        </mobi:content>
                    </mobi:tabSet>
                </mobi:fragment>
            </mobi:splitPane>
         </mobi:pagePanelBody>
        <%@ include file="/WEB-INF/jsp/footer.jsp"%>
    </mobi:pagePanel>
</body>
</html>