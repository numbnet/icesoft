<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<%-- 
<jsp:useBean id="requestNotifier" class="org.icepush.sample.basic.IntervalGroupNotifier" scope="session">
	<jsp:setProperty name="requestNotifier" property="interval" value="3000"/>
</jsp:useBean>
--%>
<jsp:useBean id="sessionNotifier" class="org.icepush.sample.basic.IntervalGroupNotifier" scope="session">
	<jsp:setProperty name="sessionNotifier" property="interval" value="5000"/>
</jsp:useBean>

<jsp:useBean id="applicationNotifier" class="org.icepush.sample.basic.IntervalGroupNotifier" scope="application">
	<jsp:setProperty name="applicationNotifier" property="interval" value="10000"/>
</jsp:useBean>

<html>
<head>
	<title>Testing ICEpush in JSP</title>
	<script type="text/javascript" src="code.icepush"></script>
	<script type="text/javascript" src="icepushUtil.js"></script>

</head>
<body>
	<h1>Testing ICEpush in JSP.</h1>
<%--
	Request Region
	<icep:region id="request-region" group="${session.id}" notifier="requestNotifier">
&nbsp 
	</icep:region>
	<br/>
--%>
	Session Region
	<icep:region id="session-region" group="session" notifier="sessionNotifier">
&nbsp 
	</icep:region>
	<br/>
	Application Region
	<icep:region id="application-region" group="Application" notifier="applicationNotifier">
&nbsp 
	</icep:region>
</body>
</html>
