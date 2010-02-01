<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="icep" uri="/WEB-INF/icepush.tld"%>

<jsp:useBean id="members" class="org.icepush.integration.jsp.samples.push.Members" scope="application">
</jsp:useBean>
<jsp:setProperty name="members" property="*"/>

<html>
<head>
	<title>Testing ICEpush in JSP</title>
	<script type="text/javascript" src="code.icepush"></script>
	<script type="text/javascript" src="icepushUtil.js"></script>

</head>
<body>
<h2>Testing icep:push</h2><br/><br/>
<h3>Welcome ${members.nickname}.</h3><br/><br/>

<form method="post" action="out.jsp">
    <input type="hidden" name="nickname" value="${members.nickname}"/>
    <input type="submit" value="Get Me Out Of Here"/>
</form>

<table>
	<tr>
		<td><h4>IN&nbsp</h4></td>
		<td><h4>OUT</h4></td>
	</tr>
	<tr>
		<td><icep:region group="all" page="/whosIn.jsp"/></td>
		<td><icep:region group="all" page="/whosOut.jsp"/></td>
	</tr>
</table>
<icep:push group="all"/>
</body>
</html>
