<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<jsp:useBean id="windowNotifier" class="org.icepush.integration.common.notify.GroupNotifier" scope="session">
</jsp:useBean>

<html>
<head>
	<title>Testing ICEpush JSP register tag</title>
	<script type="text/javascript" src="code.icepush"></script>
	<script type="text/javascript" src="register.js"></script>

</head>
<body>
	<h1>Testing ICEpush in JSP.</h1>
	Window Notifications
	<div id="window-div">0</div>
	<icep:register notifier="windowNotifier" callback="function(){countWindow('window-div');}"/>
	Session Notifications
	<div id="session-div">0</div>
	<icep:register group="${pageContext.session.id}" callback="function(){countSession('session-div');}"/>
	Application Notifications
	<div id="application-div">0</div>
	<icep:register group="application" callback="function(){countApplication('application-div');}"/>

	<icep:pushPeriodic group="${windowNotifier.group}" interval="3000"/>
	<icep:pushPeriodic group="${pageContext.session.id}" interval="5000"/>
	<icep:pushPeriodic group="application" interval="7000"/>
</body>
</html>
