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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
  --%>
<table border="1">
    <thead>
        <tr><td colspan="6"><span style="font-weight: bold; font-size: large;">North America</span></td></tr>
    </thead>
    <tr>
        <th>Nickname</th>
        <th>Mood</th>
        <th>What's on your mind?</th>
        <th>Client Technology</th>
        <th>Latest Post</th>
        <th>Post Message</th>
    </tr>
    <g:if test="${!northAmerica}">
    <tr>
        <td colspan="6">Empty</td>
    </tr>
    </g:if>
    <g:if test="${northAmerica}">
    <g:each var="elem" in="${northAmerica}" status="row">
    <tr>
        <td>${elem.name}</td>
        <td>${elem.mood}</td>
        <td>${elem.comment}</td>
        <td>Grails</td>
        <td>${elem.messageIn}</td>
        <td>
            <form id="msgForm${elem.region}${row}">
            <input id="msgOut${elem.region}${row}"
                   type="text"
                   name="messageOut"
                    size="20" />&nbsp
            <input type="submit"
                   value="Post"
                   onclick="click_messageOut('${elem.region}',${row},'<%=session["person"].name%>');return false;"/>
            </form>
        </td>
    </tr>
    </g:each>
    </g:if>
</table>