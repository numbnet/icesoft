<%@ include file="/WEB-INF/views/httpheaders.jsp" %>
<jsp:include page="template.jsp">
    <jsp:param name="demoID"    value="contact" />
    <jsp:param name="demoTitle" value="Contact List" />
    <jsp:param name="demoPath" value="./inc/contact-content.jsp" />
</jsp:include>