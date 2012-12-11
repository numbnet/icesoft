<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
    response.setHeader("Pragma", "no-cache");//HTTP 1.0
    response.setHeader("Expires", "0");//prevents proxy caching
%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>