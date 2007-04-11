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

<%@ include file="/html/portlet/enterprise_admin/init.jsp" %>

<%
boolean editable = ParamUtil.getBoolean(request, "editable");

String redirect = ParamUtil.getString(request, "redirect");

String className = ParamUtil.getString(request, "className");
String classPK = ParamUtil.getString(request, "classPK");

SearchContainer searchContainer = new SearchContainer();

List headerNames = new ArrayList();

headerNames.add("url");
headerNames.add("type");
headerNames.add("primary");

if (editable) {
	headerNames.add(StringPool.BLANK);
}

searchContainer.setHeaderNames(headerNames);

List results = WebsiteServiceUtil.getWebsites(className, classPK);
List resultRows = searchContainer.getResultRows();

for (int i = 0; i < results.size(); i++) {
	Website website = (Website)results.get(i);

	ResultRow row = new ResultRow(website, website.getPrimaryKey(), i);

	row.addText(website.getUrl());
	row.addText(website.getType().getName());
	row.addText(LanguageUtil.get(pageContext, website.isPrimary() ? "yes" : "no"));

	if (editable) {
		if (className.equals(website.getClassName())) {
			row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/enterprise_admin/website_action.jsp");
		}
		else {
			row.addText(StringPool.BLANK);
		}
	}

	resultRows.add(row);
}
%>

<c:if test="<%= editable %>">
	<input type="button" value='<%= LanguageUtil.get(pageContext, "add") %>' onClick="self.location = '<portlet:renderURL windowState="<%= WindowState.MAXIMIZED.toString() %>"><portlet:param name="struts_action" value="/enterprise_admin/edit_website" /><portlet:param name="redirect" value="<%= redirect %>" /><portlet:param name="className" value="<%= className %>" /><portlet:param name="classPK" value="<%= classPK %>" /></portlet:renderURL>';"><br>

	<c:if test="<%= results.size() > 0 %>">
		<br>
	</c:if>
</c:if>

<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />

<c:if test="<%= editable || (results.size() > 0) %>">
	<br>
</c:if>