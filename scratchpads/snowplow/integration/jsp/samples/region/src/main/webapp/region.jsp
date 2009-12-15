<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<jsp:useBean id="windowNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="session">
</jsp:useBean>

<jsp:useBean id="sessionNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="session">
</jsp:useBean>

<jsp:useBean id="applicationNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="application">
</jsp:useBean>

<%-- set properties outside <jsp:usebean> to ensure interval gets set, and notification timers start.  
This overcomes a problem on application restart where an old ajax GET to the dynamic content causes 
the bean to be instantiated but does not set the interval.  When the subsequent page load happens, 
the <jsp:usebean> does not intantiate the bean, so will not set the properties from the body tags of 
<jsp:usebean>.  That is why properties are set outside the <jsp:usebean> declarations. --%>

<jsp:setProperty name="windowNotifier" property="interval" value="3000"/>
<jsp:setProperty name="sessionNotifier" property="interval" value="5000"/>
<jsp:setProperty name="applicationNotifier" property="interval" value="7000"/>

<%
public void jspDestroy() {
	windowNotifier.stopTimer();
	sessionNotifier.stopTimer();
	applicationNotifier.stopTimer();
}
%>

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
