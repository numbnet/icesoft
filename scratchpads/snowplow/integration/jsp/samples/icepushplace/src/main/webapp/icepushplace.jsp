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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
--%>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.icepush.PushContext"%>
<%@taglib prefix="icep" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>

<jsp:useBean id="world" class="org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld" scope="application">
   <jsp:setProperty name="world" property="webServiceURL" value="http://localhost:8080/icePushPlaceService"/>
   <jsp:setProperty name="world" property="applicationURL" value="http://localhost:8080/myApp"/>
</jsp:useBean>
<jsp:useBean id="person" class="org.icepush.ws.samples.icepushplace.PersonType" scope="session">
    <jsp:setProperty name="person" property="*"/>
</jsp:useBean>

<% 
if (person.getKey() == 0) {
   person = world.loginPerson(person.getRegion(), person);
}
%>
<html>
<head>
    <title>ICEpush Place</title>
    <script type="text/javascript" src="code.icepush"></script>
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
        var name = document.getElementById("settings").elements["name"].value;
        var mood = document.getElementById("settings").elements["mood"].value;
        var comment = document.getElementById("settings").elements["comment"].value;
        var region = document.getElementById("settings").elements["region"].value;
        var xmlHttp = getXmlHttpRequest();
        var params = "name=" + name + "&mood=" + mood + "&comment=" + comment + "&region=" + region;
        xmlHttp.open("POST", "./updateSettings.jsp", false);
        xmlHttp.setRequestHeader("Content-type",
                        "application/x-www-form-urlencoded");
        xmlHttp.send(params);
    }
    function click_messageOut(region,row,from){
        var messageOut = document.getElementById("msgForm" + region + row).elements["msgOut" + region + row].value;
        var xmlHttp = getXmlHttpRequest();
        var params = "msgOut=" + messageOut + "&region=" + region + "&row=" + row + "&from=" + from;
        xmlHttp.open("POST", "./messageOut.jsp", false);
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
            <td>Name: </td>
            <td>
                <input type="text"
                       value="${person.name}"
                       id="name"
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
                    <% if (person.getMood().compareTo("average") == 0)  {%>
                    selected
                    <% } %>
                    >average</option>
                    <option value="shocked"
                    <% if (person.getMood().compareTo("shocked") == 0)  {%>
                    selected
                    <% } %>
                    >shocked</option>
                    <option value="angry"
                    <% if (person.getMood().compareTo("angry") == 0)  {%>
                    selected
                    <% } %>
                    >angry</option>
                    <option value="happy"
                    <% if (person.getMood().compareTo("happy") == 0)  {%>
                    selected
                    <% } %>
                    >happy</option>
                    <option value="sad"
                    <% if (person.getMood().compareTo("sad") == 0)  {%>
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
                       value="${person.comment}"
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
                    <option value="4"
                    <% if (person.getRegion() == 4)  {%>
                    selected
                    <% } %>
                    >North America</option>
                    <option value="3"
                    <% if (person.getRegion() == 3)  {%>
                    selected
                    <% } %>
                    >Europe</option>
                    <option value="5"
                    <% if (person.getRegion() == 5)  {%>
                    selected
                    <% } %>
                    >South America</option>
                    <option value="2"
                    <% if (person.getRegion() == 2)  {%>
                    selected
                    <% } %>
                    >Asia</option>
                    <option value="0"
                    <% if (person.getRegion() == 0)  {%>
                    selected
                    <% } %>
                    >Africa</option>
                    <option value="1"
                    <% if (person.getRegion() == 1)  {%>
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
                <input type="submit" value="Update" onclick="click_updateSettings();return false;"/>
            </td>
        </tr>
    </table>
</form>

<h2>Regions</h2>
<icep:region group="1" page="/northAmerica.jsp"/><br/><br/>
<icep:region group="2" page="/europe.jsp"/><br/><br/>
<icep:region group="3" page="/southAmerica.jsp"/><br/><br/>
<icep:region group="4" page="/asia.jsp"/><br/><br/>
<icep:region group="5" page="/africa.jsp"/><br/><br/>
<icep:region group="6" page="/antarctica.jsp"/><br/><br/>

</body>
</html>
