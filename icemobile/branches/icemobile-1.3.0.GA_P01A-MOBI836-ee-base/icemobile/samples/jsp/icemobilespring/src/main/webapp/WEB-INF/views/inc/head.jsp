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

<%@ page import="org.icemobile.util.Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<head>
    <title>ICEmobile Spring</title>
    <mobi:deviceResource  includePush="true"/>
    <link href="<c:url value="/resources/favicon.ico"/>" rel="icon" type="image/x-icon">
    <link href="<c:url value="/resources/favicon.ico"/>" rel="shortcut icon" type="image/x-icon">
    <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript"
        src="<c:url value="/resources/jquery/jquery-1.9.1.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/icemobilespring.js" />"></script> 
    <mobi:googleAnalytics/>
</head>