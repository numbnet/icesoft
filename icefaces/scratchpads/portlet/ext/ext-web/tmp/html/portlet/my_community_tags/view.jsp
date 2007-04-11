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

<%@ include file="/html/portlet/my_community_tags/init.jsp" %>

<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">
		<%= LanguageUtil.get(pageContext, "community-tags-are-injected-to-all-pages-of-this-community") %>

		<br><br>

		<c:choose>
			<c:when test="<%= entries.length > 0 %>">
				<%= LanguageUtil.format(pageContext, "you-have-the-following-tags-configured-x", "<b>" + StringUtil.merge(entries, ", ") + "</b>") %>
			</c:when>
			<c:otherwise>
				<%= LanguageUtil.get(pageContext, "you-have-not-configured-any-community-tags") %>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<%= LanguageUtil.get(pageContext, "you-must-be-authenticated-to-use-this-portlet") %>
	</c:otherwise>
</c:choose>