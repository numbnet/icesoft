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
<div id="users">
	<g:if test="${session['user'] && session['currentChatRoom']}">
		<g:each var="chatSession" in="${session['currentChatRoom'].userChatSessions}">
			<div id="${chatSession.user.name}">
				${chatSession.user.name}&nbsp; 
				<icep:region action="messageDraft" params="[user:chatSession.user.name]"
					group="${chatSession.room.name}_${chatSession.user.name}_draft" > </icep:region>
			</div>
	</g:each>
	</g:if>
</div>