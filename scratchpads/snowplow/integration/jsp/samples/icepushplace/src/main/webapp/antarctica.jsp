<%--
 *
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>

<jsp:useBean id="regions" class="org.icepush.place.jsp.view.model.Regions" scope="application">
</jsp:useBean>
<jsp:useBean id="person" class="org.icepush.place.jsp.view.model.Person" scope="session">
</jsp:useBean>

<table border="1">
    <thead>
        <tr><td colspan="6"><span style="font-weight: bold; font-size: large;">Antarctica</span></td></tr>
    </thead>
    <tr>
        <th>Nickname</th>
        <th>Mood</th>
        <th>What's on your mind?</th>
        <th>Client Technology</th>
        <th>Latest Post</th>
        <th>Post Message</th>
    </tr>
    <c:choose>
    <c:when test="${empty regions.antarctica}">
    <tr>
        <td colspan="6">Empty</td>
    </tr>
    </c:when>
    <c:otherwise>
    <c:forEach var="elem" items="${regions.antarctica}" varStatus="row">
    <tr>
        <td><c:out value="${elem.nickname}"/>&nbsp</td>
        <td><c:out value="${elem.mood}"/>&nbsp</td>
        <td><c:out value="${elem.comment}"/>&nbsp</td>
        <td><c:out value="JSP"/>&nbsp</td>
        <td><c:out value="${elem.messageIn}"/>&nbsp</td>
        <td>
            <form id="msgForm${elem.region}${row.index}">
            <input id="msgOut${elem.region}${row.index}"
                   type="text"
                   name="messageOut"
                    size="20" />&nbsp
            <input type="submit"
                   value="Post"
                   onclick="click_messageOut('${elem.region}',${row.index},'${person.nickname}');return false;"/>
            </form>
        </td>
    </tr>
    </c:forEach>
    </c:otherwise>
    </c:choose>
</table>
