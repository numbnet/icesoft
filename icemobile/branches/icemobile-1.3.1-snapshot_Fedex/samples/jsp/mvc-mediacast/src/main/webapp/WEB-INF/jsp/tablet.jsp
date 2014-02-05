<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
            
            <img src="<c:url value="/resources/images/icemobile_thumb.png"/>"/>
            ICEmobile Mediacast
        </mobi:pagePanelHeader>
        <mobi:pagePanelBody>
            
            <mobi:splitPane id="splitPane" scrollable="true" columnDivider="30">
                <mobi:fragment name="left">
                    <%@ include file="/WEB-INF/jsp/gallery-list.jsp" %>
                </mobi:fragment>
                <mobi:fragment name="right">
                    <mobi:tabSet id="tabs" selectedId="tab1" fixedPosition="false">
                       <mobi:tabPane id="tab1" title="Upload">
                            <script type="text/javascript">
                            function updateCarousel(){
                                ice.push.get('./carousel', function(parameter) { 
                                    parameter('group', 'photos');
                                } , 
                                function(statusCode, responseText) {
                                    var container = document.getElementById('carouselContainer');
                                    if( container ) container.innerHTML = responseText;
                                    if( container ) ice.push.searchAndEvaluateScripts(container);
                                });
                            }
                            </script>
                            <push:register group="photos" callback="updateCarousel"/>
                            <div id="carouselContainer"></div>
                           <%@ include file="/WEB-INF/jsp/upload-form.jsp" %>
                       </mobi:tabPane>
                       <mobi:tabPane id="tab2" title="Viewer">
                           <%@ include file="/WEB-INF/jsp/viewer-panel.jsp" %>
                       </mobi:tabPane>
                    </mobi:tabSet>
                </mobi:fragment>
            </mobi:splitPane>
         </mobi:pagePanelBody>
        <%@ include file="/WEB-INF/jsp/footer.jsp"%>
    </mobi:pagePanel>
</body>
</html>