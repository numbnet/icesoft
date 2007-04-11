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

<%@ include file="/html/portlet/rss/init.jsp" %>

<%
String url = ParamUtil.getString(request, "url");
String title = StringPool.BLANK;
boolean hide = false;

WindowState windowState = renderRequest.getWindowState();
%>

<c:choose>
	<c:when test="<%= windowState.equals(WindowState.MAXIMIZED) && Validator.isNotNull(url) %>">

		<%
		int i = 0;
		entriesPerFeed = 20;
		%>

		<%@ include file="/html/portlet/rss/feed.jsp" %>
	</c:when>
	<c:otherwise>

		<%
		for (int i = 0; i < urls.length; i++) {
			url = urls[i];

			if (i < titles.length) {
				title = titles[i];
			}
			else {
				title = StringPool.BLANK;
			}

			if (i == 0) {
				hide = false;
			}
			else {
				hide = true;
			}
		%>

			<%@ include file="/html/portlet/rss/feed.jsp" %>

		<%
		}
		%>

		<script type="text/javascript">
			_$J("#p_p_id<portlet:namespace />").Accordion({
				headerSelector: '.portlet-rss-header',
				panelSelector: '.portlet-rss-content',
				panelHeight: _$J("#p_p_id<portlet:namespace /> .portlet-rss-content:first").height(),
				speed: 300
			});
		</script>
	</c:otherwise>
</c:choose>