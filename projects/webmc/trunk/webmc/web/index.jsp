<!--
  ~ Version: MPL 1.1/GPL 2.0/LGPL 2.1
  ~
  ~ "The contents of this file are subject to the Mozilla Public License
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
  ~ 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
  ~
  ~ Contributor(s): _____________________.
  ~
  ~ Alternatively, the contents of this file may be used under the terms of
  ~ the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
  ~ License), in which case the provisions of the LGPL License are
  ~ applicable instead of those above. If you wish to allow use of your
  ~ version of this file only under the terms of the LGPL License and not to
  ~ allow others to use your version of this file under the MPL, indicate
  ~ your decision by deleting the provisions above and replace them with
  ~ the notice and other provisions required by the LGPL License. If you do
  ~ not delete the provisions above, a recipient may use your version of
  ~ this file under either the MPL or the LGPL License."
  ~
-->
<html>
<head>
    <title>WebMC - Welcome</title>
    <script>
        function showLarge( sUrl ) {
           window.open( sUrl,'', 'width=1024, height=850, scrollbars=yes, resizable=yes, status=no, location=no, menubar=no');
        }
    </script>
</head>
<body>
<!-- This page sniffs out whether or not someone is using a mobile browser so a 
     new browser window without a menubar can be launched when someone is using 
     webmc from a desktop browser -->
<%
String agent = request.getHeader("USER-AGENT").toLowerCase();
if((agent.indexOf("safari") != -1 && agent.indexOf("mobile") != -1) || 
   (agent.indexOf("opera") != -1 && agent.indexOf("240x320") != -1)){
    session.setAttribute("mobile", "true");
    String redirectURL = "./webmc.iface";
    response.sendRedirect(redirectURL);
}else{
session.setAttribute("mobile", "false");
%>
  <br/><br/><br/>
  <div style="text-align:center;">
    <a href="javascript: showLarge('webmc.iface')">
      <img border="0" alt="Start WebMC" src="resources/images/webpresentation_temp.gif" />
    </a>
  </div>
<%
} 
%>
</body>
</html>
