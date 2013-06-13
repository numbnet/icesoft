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
<head>
	<title>ICEmobile Mediacast</title>
	<mobi:deviceResource includePush="true"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
    <c:set var="dojo" value="${param.js eq 'dojo'}"/>
    <c:choose>
        <c:when test="${dojo}">
            <script>
                dojoConfig= {
                    has: {
                        "dojo-firebug": true
                    },
                    parseOnLoad: false,
                    async: true
                };
            </script>
            <script type="text/javascript" src='<c:url value="/resources/javascript/dojo-release-1.8.3/dojo/dojo.js"/>'></script>
            <script type="text/javascript" src='<c:url value="/resources/javascript/mediacast-dojo.js"/>'></script>
        </c:when>
        <c:otherwise>
            <script type="text/javascript" src='<c:url value="/resources/javascript/jquery-1.9.1.min.js"/>'></script>
            <script type="text/javascript" src='<c:url value="/resources/javascript/jquery-ui-1.8.23.custom.min.js"/>'></script>
            <script type="text/javascript" src='<c:url value="/resources/javascript/mediacast-jquery.js"/>'></script>
        </c:otherwise>
    </c:choose>
    <mobi:googleAnalytics/>
</head>
