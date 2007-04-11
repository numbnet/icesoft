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

<%@ include file="/html/taglib/init.jsp" %>

<%@ page import="com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil" %>

<%
String randomNamespace = PwdGenerator.getPassword(PwdGenerator.KEY3, 4) + StringPool.UNDERLINE;

String className = (String)request.getAttribute("liferay-ui:tags_selector:className");
String classPK = (String)request.getAttribute("liferay-ui:tags_selector:classPK");
String hiddenInput = (String)request.getAttribute("liferay-ui:tags_selector:hiddenInput");
String curTags = GetterUtil.getString((String)request.getAttribute("liferay-ui:tags_selector:curTags"));
boolean focus = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:tags_selector:focus"));

if (Validator.isNotNull(className) && Validator.isNotNull(classPK)) {
	List entries = TagsEntryLocalServiceUtil.getEntries(className, classPK);

	curTags = ListUtil.toString(entries, "name");
}

String curTagsParam = request.getParameter(hiddenInput);

if (curTagsParam != null) {
	curTags = curTagsParam;
}
%>

<input id="<%= namespace %><%= hiddenInput %>" type="hidden">

<table border="0" cellpadding="0" cellspacing="0">
<tr>
	<td>
		<input id="<%= randomNamespace %>tags" type="text">
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<span id="<%= randomNamespace %>tagsSummary"></span>
	</td>
</tr>
</table>

<script type="text/javascript">
	var <%= randomNamespace %> = null;

	jQuery(
		function() {
			<%= randomNamespace %> = new Liferay.TagsSelector(
				{
					instanceVar: "<%= randomNamespace %>",
					hiddenInput: "<%= namespace + hiddenInput %>",
					textInput: "<%= randomNamespace %>tags",
					summarySpan: "<%= randomNamespace %>tagsSummary",
					curTags: "<%= curTags %>",
					focus: <%= focus %>
				}
			);
		}
	);
</script>