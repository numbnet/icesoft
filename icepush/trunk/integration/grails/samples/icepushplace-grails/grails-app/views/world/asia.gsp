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
<table cellspacing="0" cellpadding="0" style="width: 100%;">
<tbody>
    <tr>
        <td>
            <div class="regionPanel" style="width: 100%;">
                <div class="regionHeader">Asia</div>
            </div>
        </td>
    </tr>
    <g:if test="${!asia}">
        <tr><td>
            <table>
            <tbody>
                <tr><td>
                    No users on this continent.
                </td></tr>
            </tbody>
            </table>
        </td></tr>
    </g:if>
    <g:if test="${asia}">
    <g:each var="elem" in="${asia}" status="row">
        <tr><td>
            <table>
            <tbody>
                <tr>
                    <td>
                        <img src="images/mood-${elem.mood}.png" style="width: 26px; height: 29px;">
                    </td>
                    <td>${elem.name}</td>
                    <g:if test="${elem.technology}">
                    <td>using ${elem.technology}</td>
                    </g:if>
                    <td>
                        thinks '${elem.comment}'
                    <td>
                    <g:if test="${elem.messageIn}">
                    <td>and<td>
                    <td>says '${elem.messageIn}'</td>
                    </g:if>
                </tr>
            </tbody>
            </table>
        </td></tr>
    </g:each>
    </g:if>
</tbody>
</table>