<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/httpheaders.jsp" %>
<jsp:include page="template.jsp">
    <jsp:param name="demoID"    value="cloudpush" />
    <jsp:param name="demoTitle" value="Cloud Push" />
    <jsp:param name="demoPath" value="./inc/cloudpush-content.jsp" />
</jsp:include>