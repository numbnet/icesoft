<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<head>
    <title>ICEmobile Spring</title>
    <mobi:deviceResource  includePush="true"/>
    <link href="resources/favicon.ico" rel="icon" type="image/x-icon">
    <link href="resources/favicon.ico" rel="shortcut icon" type="image/x-icon">
    <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript"
        src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/icemobilespring.js" />"></script> 
    <mobi:googleAnalytics/>
</head>