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

var loadingAnimation = new Image();

loadingAnimation.src =  "<%= themeDisplay.getPathThemeImages() %>/progress_bar/loading_animation.gif";

function addPortlet(plid, portletId, doAsUserId) {
	var refreshPortletList = ",";

	<%
	List portlets = PortletLocalServiceUtil.getPortlets(company.getCompanyId(), false, false);

	for (int i = 0; i < portlets.size(); i++) {
		Portlet portlet = (Portlet)portlets.get(i);

		if (!portlet.isAjaxable()) {
	%>

			refreshPortletList += "<%= portlet.getPortletId() %>,";

	<%
		}
	}
	%>

	if (refreshPortletList.match("," + portletId + ",")) {
		self.location = "<%= themeDisplay.getPathMain() %>/portal/update_layout?p_l_id=" + plid + "&p_p_id=" + portletId + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=<%= Constants.ADD %>&referer=" + encodeURIComponent("<%= themeDisplay.getPathMain() %>/portal/layout?p_l_id=" + plid + "&doAsUserId=" + doAsUserId) + "&refresh=1";
	}
	else {
		var loadingDiv = document.createElement("div");
		var container = document.getElementById("layout-column_column-1");

		if (container == null) {
			return;
		}

		loadingDiv.className = "loading-animation";

		container.appendChild(loadingDiv);

		var queryString = "<%= themeDisplay.getPathMain() %>/portal/update_layout?p_l_id=" + plid + "&p_p_id=" + portletId + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=<%= Constants.ADD %>";

		AjaxUtil.request(queryString, {
			onComplete: function(xmlHttpReq){
				var container = document.getElementById("layout-column_column-1");
				var portletId = addPortletHTML(xmlHttpReq.responseText, container, loadingDiv);

				if (window.location.hash) {
					window.location.hash = "p_" + portletId;
				}
			}
		});
	}
}

function addPortletHTML(html, container, placeHolder) {
	if (container == null) {
		return;
	}

	var addDiv = document.createElement("div");

	addDiv.style.display = "none";
	addDiv.innerHTML = html;

	var portletBound = _$J(".portlet-boundary:first", addDiv).get(0);

	portletBound.parentNode.removeChild(portletBound);

	var portletId = portletBound.id;

	portletId = portletId.replace(/^p_p_id_/,"");
	portletId = portletId.replace(/_$/,"");

	portletBound.portletId = portletId;

	Liferay.Portlet.flagAjax(portletId);

	container.replaceChild(portletBound, placeHolder);

	jQuery(addDiv).evalScripts();

	if (!jQuery.browser.firefox) {
		jQuery(portletBound).evalScripts();
	}

	//TODO: Check to see if user has permissions
	if (true) {
		if (!portletBound.isStatic) {
			Liferay.Draggables.addItem(portletBound);
		}
	}

	if (_$J.browser.firefox) {
		setTimeout("Liferay.Portlet.process(\"" + portletId + "\")", 0);
	}
	else {
		Liferay.Portlet.process(portletId);
	}

	Liferay.Util.addInputType(portletBound.id);

	return portletId;
}

function closePortlet(plid, portletId, doAsUserId, skipConfirm) {
	if (skipConfirm || confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-remove-this-component") %>')) {
		var curItem = document.getElementById("p_p_id_" + portletId + "_");
		var parent = curItem.parentNode;

		parent.removeChild(curItem);

		if (curItem = document.getElementById(portletId)) {
			parent = curItem.parentNode;

			parent.removeChild(curItem);
		}

		if (LayoutConfiguration) {
			LayoutConfiguration.initialized = false;
		}

		if (LayoutColumns.layoutMaximized) {
			self.location = "<%= themeDisplay.getPathMain() %>/portal/update_layout?p_l_id=" + plid + "&p_p_id=" + portletId + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=<%= Constants.DELETE %>&referer=" + encodeURIComponent("<%= themeDisplay.getPathMain() %>/portal/layout?p_l_id=" + plid + "&doAsUserId=" + doAsUserId) + "&refresh=1";
		}
		else {
			loadPage("<%= themeDisplay.getPathMain() %>/portal/update_layout", "p_l_id=" + plid + "&p_p_id=" + portletId + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=<%= Constants.DELETE %>");
		}

		Liferay.Portlet.remove(portletId);
	}
	else {
		self.focus();
	}
}

function minimizePortlet(plid, portletId, restore, doAsUserId) {
	if (LayoutColumns.layoutMaximized) {
		self.location = "<%= themeDisplay.getPathMain() %>/portal/update_layout?p_l_id=" + plid + "&p_p_id=" + portletId + "&p_p_restore=" + restore + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=minimize&referer=" + encodeURIComponent("<%= themeDisplay.getPathMain() %>/portal/layout?p_l_id=" + plid + "&doAsUserId=" + doAsUserId) + "&refresh=1";
	}
	else {
		var portlet = jQuery('#p_p_id_' + portletId + '_');
		var portletContentContainer = portlet.find('.portlet-content-container');

		var buttonsEl = jQuery("#p_p_body_" + portletId + "_min_buttons");

		var html = buttonsEl.html();

		if (restore) {
			portletContentContainer.slideDown('fast');

			html = html.replace(", true", ", false");
			html = html.replace("restore.png", "minimize.png");
			html = html.replace("<%= LanguageUtil.get(pageContext, "restore") %>", "<%= LanguageUtil.get(pageContext, "minimize") %>");

			loadPage("<%= themeDisplay.getPathMain() %>/portal/update_layout", "p_l_id=" + plid + "&p_p_id=" + portletId + "&p_p_restore=" + restore + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=minimize");
		}
		else {
			portletContentContainer.slideUp('fast');

			html = html.replace(", false", ", true");
			html = html.replace("minimize.png", "restore.png");
			html = html.replace("<%= LanguageUtil.get(pageContext, "minimize") %>", "<%= LanguageUtil.get(pageContext, "restore") %>");

			loadPage("<%= themeDisplay.getPathMain() %>/portal/update_layout", "p_l_id=" + plid + "&p_p_id=" + portletId + "&p_p_restore=" + restore + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=minimize");
		}

		buttonsEl.html(html);
	}
}

function movePortlet(plid, portletId, columnId, columnPos, doAsUserId) {
	loadPage("<%= themeDisplay.getPathMain() %>/portal/update_layout", "p_l_id=" + plid + "&p_p_id=" + portletId + "&p_p_col_id=" + columnId + "&p_p_col_pos=" + columnPos + "&doAsUserId=" + doAsUserId + "&<%= Constants.CMD %>=move");
}