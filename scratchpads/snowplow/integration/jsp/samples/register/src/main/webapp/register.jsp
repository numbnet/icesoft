<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<jsp:useBean id="windowNotifier" class="org.icepush.integration.jsp.samples.register.IntervalTrigger" scope="session">
</jsp:useBean>

<jsp:useBean id="sessionNotifier" class="org.icepush.integration.jsp.samples.register.IntervalTrigger" scope="session">
</jsp:useBean>

<jsp:useBean id="applicationNotifier" class="org.icepush.integration.jsp.samples.register.IntervalTrigger" scope="application">
</jsp:useBean>

<%-- Set timerList property in beans so timers can be tracked and canceled --%>
<%@ page import="org.icepush.integration.jsp.samples.register.IntervalTrigger" %>
<%@ page import="org.icepush.integration.jsp.samples.register.TimerList" %>
<% 
TimerList timers = (TimerList)application.getAttribute("triggerTimers");
windowNotifier.setTimerList(timers);
sessionNotifier.setTimerList(timers);
applicationNotifier.setTimerList(timers);
%>

<%-- set properties outside <jsp:usebean> to ensure interval gets set, and notification timers start.  
This overcomes a problem on application restart where an old ajax GET to the dynamic content causes 
the bean to be instantiated but does not set the interval.  When the subsequent page load happens, 
the <jsp:usebean> does not intantiate the bean, so will not set the properties from the body tags of 
<jsp:usebean>.  That is why properties are set outside the <jsp:usebean> declarations. --%>

<jsp:setProperty name="windowNotifier" property="interval" value="3000"/>
<jsp:setProperty name="sessionNotifier" property="interval" value="5000"/>
<jsp:setProperty name="applicationNotifier" property="interval" value="7000"/>


<%--  In order to cancel timers, we need to add them to a list that can be cleaned up at application
undeployment time.  We use jspInit() to create the timer list, and jspDestroy() to cancel all the
timers in the list --%>

<%!
public void jspInit() {
	getServletContext().setAttribute("triggerTimers", new TimerList());
}
%>
<%!
public void jspDestroy() {
	TimerList timers = (TimerList) getServletContext().getAttribute("triggerTimers");
	timers.stopTimers();
}
%>

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
	<icep:register group="${pageContext.session.id}" notifier="sessionNotifier" callback="function(){countSession('session-div');}"/>
	Application Notifications
	<div id="application-div">0</div>
	<icep:register group="application" notifier="applicationNotifier" callback="function(){countApplication('application-div');}"/>
</body>
</html>
