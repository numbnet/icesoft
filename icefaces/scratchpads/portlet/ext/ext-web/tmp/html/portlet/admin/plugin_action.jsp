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

<%@ include file="/html/portlet/admin/init.jsp" %>

<%
String referer = request.getParameter(WebKeys.REFERER);
String tabs1 = ParamUtil.getString(request, "tabs1");
String tabs2 = ParamUtil.getString(request, "tabs2");
if (Validator.isNull(tabs2)) {
	tabs2 = "portlet";
}

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Document doc = (Document)row.getObject();

String moduleId = doc.get("moduleId");
String repositoryURL = doc.get("repositoryURL");

PortletURL rowURL = renderResponse.createRenderURL();

rowURL.setWindowState(WindowState.MAXIMIZED);

rowURL.setParameter("struts_action", "/admin/plugin_installer");
rowURL.setParameter("referer", referer);
rowURL.setParameter("redirect", currentURL);
rowURL.setParameter("tabs1", tabs1);
rowURL.setParameter("tabs2", tabs2);
rowURL.setParameter("moduleId", moduleId);
rowURL.setParameter("repositoryURL", repositoryURL);

%>

<liferay-ui:icon image="view" url="<%= rowURL.toString() %>" />