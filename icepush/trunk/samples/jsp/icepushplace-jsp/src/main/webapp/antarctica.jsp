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

<jsp:useBean id="world" class="org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld" scope="application">
</jsp:useBean>
<jsp:useBean id="person" class="org.icepush.ws.samples.icepushplace.PersonType" scope="session">
</jsp:useBean>

<table cellspacing="0" cellpadding="0" style="width: 100%;">
<tbody>
    <tr>
        <td>
            <div class="regionPanel" style="width: 100%;">
                <div class="regionHeader">Antarctica</div>
            </div>
        </td>
    </tr>
    <c:choose>
    <c:when test="${empty world.antarctica}">
        <tr><td>
            <table>
            <tbody>
                <tr><td>
                    No users on this continent.
                </td></tr>
            </tbody>
            </table>
        </td></tr>
    </c:when>
    <c:otherwise>
    <c:forEach var="elem" items="${world.antarctica}" varStatus="row">    
        <tr><td>
            <table>
            <tbody>
                <tr>
                    <td>
                        <img src="images/mood-${elem.mood}.png" style="width: 26px; height: 29px;">
                    </td>
                    <td>
                        <c:out value="${elem.name}"/>
                    </td>
                    <c:if test="${!empty elem.technology}">
                    <td>
                        <c:out value="using ${elem.technology}"/>
                    </td>
                    </c:if>
                    <td>
                        <c:out value="thinks '${elem.comment}'"/>
                    <td>
                    <c:if test="${!empty elem.messageIn}">
                    <td>
                        and
                    <td>
                    <td>
                        <c:out value="says '${elem.messageIn}'"/>            
                    </td>
                    </c:if>
                </tr>
            </tbody>
            </table>
        </td></tr>
    </c:forEach>
    </c:otherwise>
    </c:choose>
</tbody>
</table>
