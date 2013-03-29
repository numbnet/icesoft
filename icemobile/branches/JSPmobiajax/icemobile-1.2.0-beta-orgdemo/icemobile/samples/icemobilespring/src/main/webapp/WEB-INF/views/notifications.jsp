<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/httpheaders.jsp" %>
<jsp:include page="template.jsp">
    <jsp:param name="demoID"    value="notifications" />
    <jsp:param name="demoTitle" value="Notifications" />
    <jsp:param name="demoPath" value="./inc/notifications-content.jsp" />
</jsp:include>