<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<head>
    <title>ICEmobile Spring</title>
    <mobi:deviceResource  view="small" includePush="true"/>
    <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript"
        src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/icemobilespring.js" />"></script> 
    <mobi:googleAnalytics/>
</head>