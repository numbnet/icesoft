<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<head>
    <title>ICEmobile Spring</title>
    <mobi:deviceResource  view="small"/>
    <link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.all.css" />"
          rel="stylesheet" type="text/css"/>
    <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
          type="text/css"/>
    <mobi:googleAnalytics/>
</head>