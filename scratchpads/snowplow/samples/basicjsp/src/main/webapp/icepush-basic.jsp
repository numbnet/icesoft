<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<jsp:useBean id="requestNotifier" class="org.icepush.sample.basic.IntervalGroupNotifier" scope="session">
	<jsp:setProperty name="requestNotifier" property="interval" value="5000"/>
</jsp:useBean>

<html>
<head>
	<title>Testing ICEpush in JSP</title>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
	Testing ICEpush in JSP.
	<icep:register group="test" notifier="requestNotifier" callback="function(pushIds) { ice.info(ice.logger, ice.push.getCurrentNotifications()); }" />
</body>
</html>
