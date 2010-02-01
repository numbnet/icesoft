<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="members" class="org.icepush.integration.jsp.samples.push.Members" scope="application">
</jsp:useBean>
<table>
   <c:forEach var="elem" items="${members.out}">
	<tr>
		<td><c:out value="${elem}"/>&nbsp</td>
	</tr>
   </c:forEach>
</table>
