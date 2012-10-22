<%@ page import="org.icemobile.util.Utils" %>
<head>
	<title><fmt:message key="title" /></title>
	<mobi:deviceResource includePush="true"/>
	<link rel="stylesheet" href="<c:url value="/resources/css/contest.css"/>"/>
	<script type="text/javascript" src='<c:url value="/resources/javascript/jquery-1.8.1-min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/javascript/jquery-ui-1.8.23.custom.min.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/resources/javascript/mediacast.js"/>'></script>
    <script type="text/javascript">
        <%
            String cloudPushId = Utils.getCloudPushId(request);
            if (null != cloudPushId) {
                out.println("window.addEventListener('load', function() { ice.push.parkInactivePushIds('" + cloudPushId + "'); }, false);");
            }
        %>
    </script>
    <mobi:googleAnalytics account="test" domain="www.icemobile.org"/>
</head>
