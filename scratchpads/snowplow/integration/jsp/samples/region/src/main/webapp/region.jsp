<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<jsp:useBean id="uniqueGroupName" class="org.icepush.integration.common.group.UniqueName" scope="application">
</jsp:useBean>

<jsp:useBean id="windowNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="session">
	<jsp:setProperty name="windowNotifier" property="interval" value="3000"/>
</jsp:useBean>

<jsp:useBean id="sessionNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="session">
	<jsp:setProperty name="sessionNotifier" property="interval" value="5000"/>
</jsp:useBean>

<jsp:useBean id="applicationNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="application">
	<jsp:setProperty name="applicationNotifier" property="interval" value="7000"/>
</jsp:useBean>


<html>
<head>
	<title>Testing ICEpush in JSP</title>
	<script type="text/javascript" src="code.icepush"></script>
	<script type="text/javascript" src="icepushUtil.js"></script>

</head>
<body>
	<h1>Testing ICEpush in JSP.</h1>
	Window Region
	<icep:region id="window-region" notifier="windowNotifier" page="/window.jsp"/>
	<br/>
	Session Region
	<icep:region group="${pageContext.session.id}" notifier="sessionNotifier" page="/session.jsp"/>
	<br/>
	Application Region
	<icep:region group="application" notifier="applicationNotifier" page="/application.jsp"/>
</body>
</html>
