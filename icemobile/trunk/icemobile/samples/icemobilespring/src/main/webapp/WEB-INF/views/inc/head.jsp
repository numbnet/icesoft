<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<head>
    <title>ICEmobile Spring</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.all.css" />"
          rel="stylesheet" type="text/css"/>
    <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
          type="text/css"/>
    <mobi:deviceResource  view="small"/>
    <script type="text/javascript" src="code.icepush"></script>
    <script type="text/javascript">document.documentElement.className = document.documentElement.className+' js';</script>
    <mobi:googleAnalytics/>
</head>