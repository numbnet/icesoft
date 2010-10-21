<%--
 *
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
--%>
<html>
<head>
  <title>ICEpush ICEchat (Grails)</title>
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
  <link rel='stylesheet' type='text/css' href='${resource(dir: "css", file: "style-common.css")}'/>
  <script type="text/javascript">
    function getXmlHttpRequest() {
      try {
        return new XMLHttpRequest(); // Firefox, Opera 8.0+, Safari
      } catch (e) {
        try {
          return new ActiveXObject("Msxml2.XMLHTTP"); // Internet Explorer
        } catch (e) {
          try {
            return new ActiveXObject("Microsoft.XMLHTTP");
          } catch (e) {
            alert("Your browser is too old for AJAX!");
            return null;
          }
        }
      }
    }
    function login() {
      var xmlHttp = getXmlHttpRequest();
      xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4) {
          if (xmlHttp.status == 200)
            window.location.href = '${createLink(controller:'chat')}';
          else if (xmlHttp.status == 401)
            document.getElementById("loginMessages").innerHTML = xmlHttp.responseText;
        }
      }
      var loginForm = document.forms['loginform'];
      var params = "op=login&" + getLoginFormParams();
      xmlHttp.open("POST", "${createLink(action:'auth')}", true);
      xmlHttp.setRequestHeader("Content-type",
              "application/x-www-form-urlencoded");
      xmlHttp.send(params);
    }

    function getLoginFormParams() {
      var loginForm = document.forms['loginform'];
      return  "userName=" + loginForm.elements['userName'].value;
    }

  </script>
</head>
<body>
<div id="headerContainer" class="header_container">
  <div class="header_content">
    <div style="font-size: 20pt; color: #0F65B8; margin-top: 15px;">
      <img src="${resource(dir: "img", file: "ICEpush-grails.png")}" title="ICEpush with Grails"/>
      <img src="${resource(dir: "images", file: "grails-logo.png")}" width="82px" style="float:right;margin-top:10px;margin-right:25px;"/>
    </div>
  </div>
</div>

<div class="body_container body_container_pos" id="content">

  <div id="loginPanel">
    <div class="login">
      <form id="loginform">
        <h2>ICEchat Login</h2>
        <div style="text-align:right;">
          <label for="userName">Name</label>
          <input type="text" id="userName" onkeypress="if (event.keyCode == 13) {
            window.login();return false;
          }"/>
        </div>
        <div style="text-align:right;margin-top:20px;">
          <input type="button" id="login" value="Login" onclick="window.login();"/>
        </div>
        <div id="loginMessagesPanel">
          <span id="loginMessages"></span>
        </div>

      </form>
    </div>
  </div>
</div>
</body>
</html>

