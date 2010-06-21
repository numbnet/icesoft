<html>
<head>
	<title>Enter ICEpush Place</title>
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
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
<h2>ICEpush Place</h2>

<form id="settings">
    <table border="1">
        <th colspan="2"><h4>ICEpush Place Console&nbsp</h4></th>
        <tr>
            <td>Nickname: </td>
            <td>
                <input type="text"
                       value="<%=session["person"].nickname%>"
                       id="nickname"
                       name="nickname"
                       size="20" />
            </td>
        </tr>
        <tr>
            <td>
                What mood are you in?:
            </td>
            <td>
                <select id="mood" name="mood" >
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
                What's on your mind?:
            </td>
            <td>
                <input type="text"
                       value="<%=session["person"].comment%>"
                       id="comment"
                       name="comment"
                       size="20"/>
            </td>
        </tr>
        <tr>
            <td>
                Change your region:
            </td>
            <td>
                <select id="region" name="region">
                    <option value="1"
                    <% if(session["person"].region == "1") {%>
                    selected
                    <% } %>
                    >North America</option>
                    <option value="2"
                    <% if(session["person"].region == "2") {%>
                    selected
                    <% } %>
                    >Europe</option>
                    <option value="3"
                    <% if(session["person"].region == "3") {%>
                    selected
                    <% } %>
                    >South America</option>
                    <option value="4"
                    <% if(session["person"].region == "4") {%>
                    selected
                    <% } %>
                    >Asia</option>
                    <option value="5"
                    <% if(session["person"].region == "5") {%>
                    selected
                    <% } %>
                    >Africa</option>
                    <option value="6"
                    <% if(session["person"].region == "6") {%>
                    selected
                    <% } %>
                    >Antarctica</option>
                    </select>
            </td>
        </tr>
        <tr>
            <td>

            </td>
            <td>
                <input type="submit" 
                       value="Update"
                       onclick="click_updateSettings();"/>
            </td>
        </tr>
    </table>
</form>

<h2>Regions</h2>

<icep:region group="1" controller="world" action="northAmerica"/><br/><br/>
<icep:region group="2" controller="world" action="europe"/><br/><br/>
<icep:region group="3" controller="world" action="southAmerica"/><br/><br/>
<icep:region group="4" controller="world" action="asia"/><br/><br/>
<icep:region group="5" controller="world" action="africa"/><br/><br/>
<icep:region group="6" controller="world" action="antarctica"/><br/><br/>

</body>
</html>