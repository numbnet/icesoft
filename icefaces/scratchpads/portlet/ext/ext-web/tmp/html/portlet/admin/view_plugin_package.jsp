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

<%
PluginPackage pluginPackage = PluginPackageUtil.getPluginPackageByModuleId(moduleId, repositoryURL);

// Breadcrumbs

breadcrumbs.append(" &raquo; <a href=\"" + currentURL + "\">" + pluginPackage.getName() + "</a>");

%>

<%= breadcrumbs.toString() %>

<br><br>

<liferay-ui:success key="pluginDownloaded" message="the-plugin-was-downloaded-successfully-and-is-now-being-installed" />

<liferay-ui:error key="errorConnectingToServer" message="an-unexpected-error-occurred-while-connecting-to-the-repository" />
<liferay-ui:error key="errorResponseFromServer" message="connecting-to-the-repository-returned-an-error" />
<liferay-ui:error key="invalidUrl" message="plugin-does-not-have-a-valid-url" />

<table border="0" cellpadding="3" cellspacing="0">
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "name") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<b><a href="<%= pluginPackage.getPageURL() %>"><%= pluginPackage.getName() %></a></b> (v<%= pluginPackage.getVersion() %>) <a href="<%= pluginPackage.getArtifactURL() %>">[<%= LanguageUtil.get(pageContext, "download") %>]</a>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "author") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<%= LanguageUtil.get(pageContext, pluginPackage.getAuthor()) %>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "types") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<%
		Iterator itr = pluginPackage.getTypes().iterator();

		while (itr.hasNext()) {
			String type = (String)itr.next();
		%>

			<%= type %><c:if test="<%= itr.hasNext() %>">, </c:if>

		<%
		}
		%>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "tags") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>

		<%
		itr = pluginPackage.getTags().iterator();

		while (itr.hasNext()) {
			String tag = (String)itr.next();
		%>

			<%= tag %><c:if test="<%= itr.hasNext() %>">, </c:if>

		<%
		}
		%>

	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "licenses") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>

		<%
		itr = pluginPackage.getLicenses().iterator();

		while (itr.hasNext()) {
			License license = (License)itr.next();
		%>

			<c:if test="<%= Validator.isNotNull(license.getUrl()) %>">
				<a href="<%= license.getUrl()%>">
			</c:if>

			<%= license.getName() %>

			<c:if test="<%= Validator.isNotNull(license.getUrl()) %>">
				</a>
			</c:if>

			<c:if test="<%= license.isOsiApproved() %>">
				(<%= LanguageUtil.get(pageContext, "open-source")%>)
			</c:if>

			<c:if test="<%= itr.hasNext() %>">, </c:if>

		<%
		}
		%>

	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "liferay-versions") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>

		<%
		Iterator itr3 = pluginPackage.getLiferayVersions().iterator();

		while (itr3.hasNext()) {
			String liferayVersion = (String)itr3.next();
		%>

			<%= liferayVersion %><c:if test="<%= itr3.hasNext() %>">, </c:if>

		<%
		}
		%>

	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "repository") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<a href="<%= pluginPackage.getRepositoryURL() %>"><%= pluginPackage.getRepositoryURL() %></a>
	</td>
</tr>
<tr>
	<td colspan="3">
		<br>
	</td>
</tr>
<tr>
	<td>
		<%= LanguageUtil.get(pageContext, "short-description") %>
	</td>
	<td style="padding-left: 10px;"></td>
	<td>
		<%= pluginPackage.getShortDescription() %>
	</td>
</tr>

<c:if test="<%= Validator.isNotNull(pluginPackage.getLongDescription()) %>">
	<tr>
		<td>
			<%= LanguageUtil.get(pageContext, "long-description") %>
		</td>
		<td style="padding-left: 10px;"></td>
		<td>
			<%= pluginPackage.getLongDescription() %>
		</td>
	</tr>
</c:if>

<c:if test="<%= Validator.isNotNull(pluginPackage.getChangeLog()) %>">
	<tr>
		<td>
			<%= LanguageUtil.get(pageContext, "change-log") %>
		</td>
		<td style="padding-left: 10px;"></td>
		<td>
			<%= pluginPackage.getChangeLog() %>
		</td>
	</tr>
</c:if>

<%
List screenshots = pluginPackage.getScreenshots();
%>

<c:if test="<%= (screenshots != null) && !screenshots.isEmpty() %>">
	<tr>
		<td colspan="3">
			<br>
		</td>
	</tr>
	<tr>
		<td>
			<%= LanguageUtil.get(pageContext, "screenshots") %>
		</td>
		<td style="padding-left: 10px;"></td>
		<td>

			<%
			itr = screenshots.iterator();

			while (itr.hasNext()) {
				Screenshot screenshot = (Screenshot)itr.next();
			%>

				<a href="<%= screenshot.getLargeImageURL() %>"><img align="left" src="<%= screenshot.getThumbnailURL() %>" width="120"></a>

			<%
			}
			%>

		</td>
	</tr>
</c:if>

</table>

<br>

<input type="hidden" name="<portlet:namespace/>url" value="<%= pluginPackage.getArtifactURL() %>">

<input type="button" value='<%=LanguageUtil.get(pageContext, "install")%>' onClick="<%= downloadProgressId%>.startProgress(); <portlet:namespace />installPluginPackage('remoteDeploy', '<%= downloadProgressId %>', '<%= currentURL %>');">

<liferay-ui:upload-progress
	id="<%= downloadProgressId %>"
	message="downloading"
	redirect="<%= currentURL %>"
/>