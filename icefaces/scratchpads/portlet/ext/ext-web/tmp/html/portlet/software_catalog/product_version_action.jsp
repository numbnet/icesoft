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

<%@ include file="/html/portlet/software_catalog/init.jsp" %>

<%
ResultRow row =	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

SCProductVersion productVersion = (SCProductVersion) row.getObject();
%>
<% if (Validator.isNotNull(productVersion.getDirectDownloadURL())) { %>
	<a href="<%=productVersion.getDirectDownloadURL()%>">
		<img src="/html/themes/classic/images/common/download.png" onmousemove="ToolTip.show(event, this, '<%=LanguageUtil.get(pageContext, "direct-download")%>')" alt="<%=LanguageUtil.get(pageContext, "direct-download")%>" align="absmiddle" border="0">
	</a>
<% } %>
<% if (Validator.isNotNull(productVersion.getDownloadPageURL())) { %>
	<a href="<%=productVersion.getDownloadPageURL()%>">
		<img src="/html/themes/classic/images/common/download.png" onmousemove="ToolTip.show(event, this, '<%=LanguageUtil.get(pageContext, "download-page")%>')" alt="<%=LanguageUtil.get(pageContext, "download-page")%>" align="absmiddle" border="0">
	</a>
<% } %>


<c:if test="<%= SCProductEntryPermission.contains(permissionChecker, productVersion.getProductEntryId(), ActionKeys.UPDATE) %>">
	<portlet:renderURL windowState="<%= WindowState.MAXIMIZED.toString() %>" var="editURL">
		<portlet:param name="struts_action" value="/software_catalog/edit_product_version" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="productEntryId" value="<%= String.valueOf(productVersion.getProductEntryId()) %>" />
		<portlet:param name="productVersionId" value="<%= String.valueOf(productVersion.getProductVersionId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon image="edit" url="<%= editURL %>" />
</c:if>

<c:if test="<%= SCProductEntryPermission.contains(permissionChecker, productVersion.getProductEntryId(), ActionKeys.UPDATE) %>">
	<portlet:actionURL windowState="<%= WindowState.MAXIMIZED.toString() %>" var="deleteURL">
		<portlet:param name="struts_action" value="/software_catalog/edit_product_version" />
		<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="productEntryId" value="<%= String.valueOf(productVersion.getProductEntryId()) %>" />
		<portlet:param name="productVersionId" value="<%= String.valueOf(productVersion.getProductVersionId()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete url="<%= deleteURL %>" />
</c:if>