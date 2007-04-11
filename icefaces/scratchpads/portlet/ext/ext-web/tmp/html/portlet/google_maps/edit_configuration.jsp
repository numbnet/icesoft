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

<%@ include file="/html/portlet/google_maps/init.jsp" %>

<form action="<liferay-portlet:actionURL portletConfiguration="true" />" method="post" name="<portlet:namespace />fm">
<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>">

<table border="0" cellpadding="0" cellspacing="0">
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "map-address") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<input name="<portlet:namespace />mapAddress" style="width: <%= ModelHintsDefaults.TEXT_DISPLAY_WIDTH %>px;" type="text" value="<%= mapAddress %>">
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "map-input-enabled") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<liferay-ui:input-checkbox param="mapInputEnabled" defaultValue="<%= mapInputEnabled %>" />
	</td>
</tr>
<tr>
	<td colspan="3">
		<br>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "directions-address") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<input name="<portlet:namespace />directionsAddress" style="width: <%= ModelHintsDefaults.TEXT_DISPLAY_WIDTH %>px;" type="text" value="<%= directionsAddress %>">
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "directions-input-enabled") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<liferay-ui:input-checkbox param="directionsInputEnabled" defaultValue="<%= directionsInputEnabled %>" />
	</td>
</tr>
<tr>
	<td colspan="3">
		<br>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "height") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<input name="<portlet:namespace />height" size="4" type="text" value="<%= height %>"> px
	</td>
</tr>
</table>

<br>

<input type="button" value="<bean:message key="save" />" onClick="submitForm(document.<portlet:namespace />fm);">

</form>

<script type="text/javascript">
	document.<portlet:namespace />fm.<portlet:namespace />mapAddress.focus();
</script>