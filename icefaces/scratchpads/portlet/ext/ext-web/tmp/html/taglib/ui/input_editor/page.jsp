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

<%
String name = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-editor:name"), namespace + "editor");

String editorImpl = (String)request.getAttribute("liferay-ui:input-editor:editorImpl");

if (Validator.isNull(editorImpl)) {
	editorImpl = PropsUtil.get("editor.wysiwyg.default");
}
else {
	editorImpl = PropsUtil.get(editorImpl);
}

String initMethod = (String)request.getAttribute("liferay-ui:input-editor:initMethod");
String onChangeMethod = (String)request.getAttribute("liferay-ui:input-editor:onChangeMethod");
String height = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-editor:height"), "400");
String width = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-editor:width"), "640");

StringMaker sm = new StringMaker();

sm.append(themeDisplay.getPathJavaScript());
sm.append("/editor/editor.jsp?p_l_id=");
sm.append(plid);
sm.append("&p_main_path=");
sm.append(themeDisplay.getPathMain());
sm.append("&doAsUserId=");
sm.append(themeDisplay.getDoAsUserId());
sm.append("&editorImpl=");
sm.append(editorImpl);

if (Validator.isNotNull(initMethod)) {
	sm.append("&initMethod=");
	sm.append(initMethod);
}

if (Validator.isNotNull(onChangeMethod)) {
	sm.append("&onChangeMethod=");
	sm.append(onChangeMethod);
}

String editorURL = sm.toString();
%>

<iframe frameborder="0" height="<%= height %>" id="<%= name %>" name="<%= name %>" scrolling="no" src="<%= editorURL %>" width="<%= width %>"></iframe>