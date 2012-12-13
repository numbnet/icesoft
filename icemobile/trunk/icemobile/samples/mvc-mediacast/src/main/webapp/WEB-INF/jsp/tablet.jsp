<%--
  ~ Copyright 2004-2012 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

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
                    <%@ include file="/WEB-INF/jsp/gallery-list.jsp" %>
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