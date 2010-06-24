<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Enter ICEpush Place</title>
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
<h2>Enter ICEpush Place</h2>
<form id="init">
    Nickname: <input type="text" name="nickname" size="20"/><br/><br/>
    What mood are you in?: <select name="mood">
                               <option value="average">average</option>
                               <option value="shocked">shocked</option>
                               <option value="angry">angry</option>
                               <option value="happy">happy</option>
                               <option value="sad">sad</option>
                          </select><br/><br/>
    What's on your mind?: <input type="text" name="comment" size="20"/><br/><br/>
    Change your region: <select name="region">
                               <option value="1">North America</option>
                               <option value="2">Europe</option>
                               <option value="3">South America</option>
                               <option value="4">Asia</option>
                               <option value="5">Africa</option>
                               <option value="6">Antarctica</option>
                           </select><br/><br/>
    <input type="button" value="Enter" onclick="register();" />
</form>
</body>
</html>
