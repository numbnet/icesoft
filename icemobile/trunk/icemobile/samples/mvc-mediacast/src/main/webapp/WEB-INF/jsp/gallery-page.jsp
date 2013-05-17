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
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/head.jsp"%>
<body>
</c:if>
    <div class="root">
    	<mobi:pagePanel>
    		<mobi:pagePanelHeader>
    			<a id="backBtn" class="mobi-button ui-btn-up-c mobi-button-back"
    				href='<c:url value="/app?page=upload"/>'>Upload</a>
    			<h1>Mediacast</h1>
                <img src="<c:url value="/resources/images/icemobile.png"/>" style="position: absolute;top: 10px;right: 10px;"/>
    		</mobi:pagePanelHeader>
    		<mobi:pagePanelBody>
    		    <%@ include file="/WEB-INF/jsp/gallery-list.jsp" %>
            </mobi:pagePanelBody>
    		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
    	</mobi:pagePanel>
    </div>
    <script type="text/javascript">
        enhanceGet("#backBtn");
        enhanceGet("#galleryList a");
    </script>
<c:if test="${!ajaxRequest}">
    </body>
</html>
</c:if>