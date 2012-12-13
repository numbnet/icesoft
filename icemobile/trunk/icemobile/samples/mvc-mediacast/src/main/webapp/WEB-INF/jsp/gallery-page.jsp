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
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/head.jsp"%>
<body>
</c:if>
    <div class="root">
    	<mobi:pagePanel>
    		<mobi:pagePanelHeader>
    			<a id="backBtn" class="mobi-button mobi-button-default"
    				href='<c:url value="/app?p=upload&l=${layout}"/>'>Upload</a>
    			<span>Mediacast</span>
                <img src="./resources/images/icemobile.png" style="position: absolute;top: 10px;right: 2em;"/>
    		</mobi:pagePanelHeader>
    		<mobi:pagePanelBody>
    		    <%@ include file="/WEB-INF/jsp/gallery-list.jsp" %>
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