<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Grails ICEpush Place</title>
	<link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>
    <script type="text/javascript">//<![CDATA[
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
    function register(){
       var xmlHttp = getXmlHttpRequest();
      xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4) {
          if (xmlHttp.status == 200){
            window.location.href = '${createLink(controller:'world')}';
            }
          else if (xmlHttp.status == 401)
            document.getElementById("init").innerHTML = xmlHttp.responseText;
        }
      }
        var submittedNickname = document.getElementById("init").elements["nickname"].value;
        var mood = document.getElementById("init").elements["mood"].value;
        var comment = document.getElementById("init").elements["comment"].value;
        var region = document.getElementById("init").elements["region"].value;
        var params = "submittedNickname=" + submittedNickname  + "&comment=" + comment + "&region=" + region + "&mood=" + mood;
        xmlHttp.open("POST", "${createLink(action:'register')}", true);
        xmlHttp.setRequestHeader("Content-type",
                        "application/x-www-form-urlencoded");
        xmlHttp.send(params);
    }
    //]]>
    </script>
</head>
<body>
<center>
<h1>Grails - ICEpush Place Login</h1>

<form id="init">
    <table class="inputSet">
        <tbody>
        <tr>
            <td>
                <div class="nameLabel">Nickname:</div>
            </td>
            <td>
                <input type="text" name="nickname" maxlength="15" class="nameInput"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="moodLabel">Mood:</div>
            </td>
            <td>
                <select name="mood" class="moodInput">
                    <option>average</option>
                    <option>shocked</option>
                    <option>angry</option>
                    <option>happy</option>
                    <option>sad</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <div class="thoughtLabel">Thoughts:</div>
            </td>
            <td>
                <input type="text" name="comment" maxlength="150" class="thoughtInput"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="regionLabel">Region:</div>
            </td>
            <td>
                <select name="region" class="regionInput">
                    <option value="4">North America</option>
                    <option value="3">Europe</option>
                    <option value="5">South America</option>
                    <option value="2">Asia</option>
                    <option value="0">Africa</option>
                    <option value="1">Antarctica</option>
                </select>
            </td>
        </tr>
    </tbody>
    </table>
    
    <input type="button" value="Login" onclick="register();" />
</form>
</center>
</body>
</html>
