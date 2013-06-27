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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
    <title>ICEmobile Spring</title>
    <mobi:deviceResource  includePush="true" name="android" />
    <link href="resources/favicon.ico" rel="icon" type="image/x-icon">
    <link href="resources/favicon.ico" rel="shortcut icon" type="image/x-icon">
    <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript"
        src="<c:url value="/resources/jquery/jquery-1.9.1.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/icemobilespring.js" />"></script> 
    <mobi:googleAnalytics/>
</head>
<body>
</c:if>

<div class="ajaxzone">
    <mobi:pagePanel>
        <mobi:pagePanelHeader>
            <a href="menu" id="menuLink" class="mobi-button ui-icon ui-icon-menu ui-icon-shadow"></a>
            ${param.demoTitle}
        </mobi:pagePanelHeader>
        <mobi:pagePanelBody>
            <jsp:include page="${param.demoPath}" />
        </mobi:pagePanelBody>
    </mobi:pagePanel>
    <script type="text/javascript">
    MvcUtil.enhanceLink(document.getElementById('menuLink'),"body");
    </script>
 </div>
         
<c:if test="${!ajaxRequest}">
    <script type="text/javascript">
        MvcUtil.enhanceAllLinks("#sp_left",".ajaxzone");
        $('#menu a[href=${param.demoID}]').addClass('active');
    </script>
</body>
</html>
</c:if>