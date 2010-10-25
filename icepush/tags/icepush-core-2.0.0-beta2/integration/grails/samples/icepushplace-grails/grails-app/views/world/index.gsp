<html>
<head> 
  <title>Grails ICEpush Place</title>
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
  <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
  <icep:bridge />
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
    function click_updateSettings(){
        var submittedNickname = document.getElementById("settings").elements["nickname"].value;
        var mood = document.getElementById("settings").elements["mood"].value;
        var comment = document.getElementById("settings").elements["comment"].value;
        var region = document.getElementById("settings").elements["region"].value;
        var xmlHttp = getXmlHttpRequest();
        var params = "submittedNickname=" + submittedNickname  + "&comment=" + comment + "&region=" + region + "&mood=" + mood;
        xmlHttp.open("POST", "${createLink(controller:'world',action:'updateSettings')}", false);
        xmlHttp.setRequestHeader("Content-type",
                        "application/x-www-form-urlencoded");
        xmlHttp.send(params);
    }
    function click_messageOut(region,row,from){
        var messageOut = document.getElementById("msgForm" + region + row).elements["msgOut" + region + row].value;
        var xmlHttp = getXmlHttpRequest();
        var params = "msgOut=" + messageOut + "&region=" + region + "&row=" + row + "&from=" + from;
        xmlHttp.open("POST", "${createLink(controller:'world',action:'messageOut')}", false);
        xmlHttp.setRequestHeader("Content-type",
                        "application/x-www-form-urlencoded");
        xmlHttp.send(params);
    }
    //]]>
  </script>
</head>

<body>
<center>
<h1>Grails - ICEpush Place View</h1>

<form id="settings">
    <table class="inputSet">
        <tbody>
        <tr>
            <td>
                <div class="nameLabel">Nickname:</div>
            </td>
            <td>
                <input type="text" id="nickname" name="nickname" maxlength="15" class="nameInput" value="<%=session["person"].name%>"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="moodLabel">Mood:</div>
            </td>
            <td>
                <select id="mood" name="mood" class="moodInput">
                    <option value="average"
                    <% if(session["person"].mood == "average") {%>
                    selected
                    <% } %>
                    >average</option>
                    <option value="shocked"
                    <% if(session["person"].mood == "shocked") {%>
                    selected
                    <% } %>
                    >shocked</option>
                    <option value="angry"
                    <% if(session["person"].mood == "angry") {%>
                    selected
                    <% } %>
                    >angry</option>
                    <option value="happy"
                    <% if(session["person"].mood == "happy") {%>
                    selected
                    <% } %>
                    >happy</option>
                    <option value="sad"
                    <% if(session["person"].mood == "sad") {%>
                    selected
                    <% } %>
                    >sad</option>
                </select>            
            </td>
        </tr>
        <tr>
            <td>
                <div class="thoughtLabel">Thoughts:</div>
            </td>
            <td>
                <input type="text" id="comment" name="comment" maxlength="150" class="thoughtInput" value="<%=session["person"].comment%>"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="regionLabel">Region:</div>
            </td>
            <td>
                <select id="region" name="region" class="regionInput">
                    <option value="4"
                    <% if(session["person"].region == 4) {%>
                    selected
                    <% } %>
                    >North America</option>
                    <option value="3"
                    <% if(session["person"].region == 3) {%>
                    selected
                    <% } %>
                    >Europe</option>
                    <option value="5"
                    <% if(session["person"].region == 5) {%>
                    selected
                    <% } %>
                    >South America</option>
                    <option value="2"
                    <% if(session["person"].region == 2) {%>
                    selected
                    <% } %>
                    >Asia</option>
                    <option value="0"
                    <% if(session["person"].region == 0) {%>
                    selected
                    <% } %>
                    >Africa</option>
                    <option value="1"
                    <% if(session["person"].region == 1) {%>
                    selected
                    <% } %>
                    >Antarctica</option>
                </select>
            </td>
        </tr>
    </tbody>
    </table>
    
    <input type="submit" value="Update" onclick="click_updateSettings();"/>
</form>

<table class="worldPanel" cellspacing="0" cellpadding="0">
<tbody>
    <tr><td>
        <icep:region group="Africa" controller="world" action="africa"/>
    </td></tr>
    <tr><td>
        <icep:region group="Antarctica" controller="world" action="antarctica"/>
    </td></tr>
    <tr><td>
        <icep:region group="Asia" controller="world" action="asia"/>
    </td></tr>
    <tr><td>
        <icep:region group="Europe" controller="world" action="europe"/>
    </td></tr>
    <tr><td>
        <icep:region group="North America" controller="world" action="northAmerica"/>
    </td></tr>
    <tr><td>
        <icep:region group="South America" controller="world" action="southAmerica"/>
    </td></tr>
</tbody>
</table>

</center>
</body>
</html>