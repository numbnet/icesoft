<%
/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ include file="/html/portlet/invitation/init.jsp" %>

<%
String portletResource = ParamUtil.getString(request, "portletResource");

PortletPreferences prefs = PortletPreferencesFactory.getPortletSetup(request, portletResource, false, true);

String emailMessageSubject = ParamUtil.getString(request, "emailMessageSubject", InvitationUtil.getEmailMessageSubject(prefs));
String emailMessageBody = ParamUtil.getString(request, "emailMessageBody", InvitationUtil.getEmailMessageBody(prefs));
%>

<script type="text/javascript">

	<%
	String editorParam = "emailMessageBody";
	String editorContent = emailMessageBody;
	%>

	function initEditor() {
		return "<%= UnicodeFormatter.toString(editorContent) %>";
	}

	function <portlet:namespace />saveConfiguration() {
		document.<portlet:namespace />fm.<portlet:namespace /><%= editorParam %>.value = parent.<portlet:namespace />editor.getHTML();
		submitForm(document.<portlet:namespace />fm);
	}
</script>

<form action="<liferay-portlet:actionURL portletConfiguration="true" />" method="post" name="<portlet:namespace />fm" onSubmit="<portlet:namespace />saveConfiguration(); return false;">
<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>">

<liferay-ui:error key="emailMessageBody" message="please-enter-a-valid-body" />
<liferay-ui:error key="emailMessageSubject" message="please-enter-a-valid-subject" />

<table border="0" cellpadding="0" cellspacing="0">
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "subject") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<input name="<portlet:namespace />emailMessageSubject" style="width: <%= ModelHintsDefaults.TEXT_DISPLAY_WIDTH %>px;" type="text" value="<%= emailMessageSubject %>">
	</td>
</tr>
<tr>
	<td colspan="3">
		<br>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "body") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<liferay-ui:input-editor editorImpl="<%= EDITOR_WYSIWYG_IMPL_KEY %>" />

		<input name="<portlet:namespace /><%= editorParam %>" type="hidden" value="">
	</td>
</tr>
</table>

<br>

<b><%= LanguageUtil.get(pageContext, "definition-of-terms") %></b>

<br><br>

<table border="0" cellpadding="0" cellspacing="0">
<tr>
	<td>
		<b>[$FROM_ADDRESS$]</b>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		The address of the email sender
	</td>
</tr>
<tr>
	<td>
		<b>[$FROM_NAME$]</b>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		The name of the email sender
	</td>
</tr>
<tr>
	<td>
		<b>[$PAGE_URL$]</b>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<%= PortalUtil.getPortalURL(request) %><%= PortalUtil.getLayoutURL(layout, themeDisplay) %>
	</td>
</tr>
<tr>
	<td>
		<b>[$PORTAL_URL$]</b>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<%= company.getPortalURL() %>
	</td>
</tr>
</table>

<br>

<input type="submit" value='<%= LanguageUtil.get(pageContext, "save") %>'>

</form>

<%!
public static final String EDITOR_WYSIWYG_IMPL_KEY = "editor.wysiwyg.portal-web.docroot.html.portlet.invitation.edit_configuration.jsp";
%>