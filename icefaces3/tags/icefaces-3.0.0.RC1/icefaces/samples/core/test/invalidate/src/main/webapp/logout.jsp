<%--
  ~ Version: MPL 1.1
  ~
  ~ The contents of this file are subject to the Mozilla Public License
  ~ Version 1.1 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://www.mozilla.org/MPL/
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations under
  ~ the License.
  ~
  ~ The Original Code is ICEfaces 1.5 open source software code, released
  ~ November 5, 2006. The Initial Developer of the Original Code is ICEsoft
  ~ Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
  ~ 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
  ~
  ~ Contributor(s): _____________________.
  --%>

<html>
<head>JSP Logout Page</head>
<body>

<% session.invalidate(); %>

<p>This JSP page has invalidated the session. Using the back button and trying to interact with the page should
    give a ViewExpiredException and redirect to the error-page configured in the web-xml (viewExpired.xhtml).</p>

<a href="index.jsp">Go back to the start page.</a>

</body>
</html>