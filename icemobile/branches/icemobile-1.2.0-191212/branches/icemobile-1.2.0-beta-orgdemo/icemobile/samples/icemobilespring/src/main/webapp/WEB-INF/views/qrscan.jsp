<%@ include file="/WEB-INF/views/httpheaders.jsp" %>
<jsp:include page="template.jsp">
    <jsp:param name="demoID"    value="qrscan" />
    <jsp:param name="demoTitle" value="QR Codes" />
    <jsp:param name="demoPath" value="./inc/qrscan-content.jsp" />
</jsp:include>