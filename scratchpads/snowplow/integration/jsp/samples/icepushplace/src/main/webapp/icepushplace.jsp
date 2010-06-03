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
<%@taglib prefix="icep" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>

<jsp:useBean id="members" class="org.icepush.place.jsp.view.model.Members" scope="application">
</jsp:useBean>
<jsp:setProperty name="members" property="*"/>
<jsp:useBean id="person" class="org.icepush.place.jsp.view.model.Person" scope="session">
<% members.getIn().add(person); %>
</jsp:useBean>
<jsp:setProperty name="person" property="*"/>
<jsp:useBean id="service" class="org.icepush.place.jsp.services.impl.IcepushPlaceServiceImpl" scope="application">
</jsp:useBean>

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
        	var nickName = document.getElementById("settings").elements["nickname"].value;
                var mood = document.getElementById("settings").elements["mood"].value;
                var comment = document.getElementById("settings").elements["comment"].value;
                var region = document.getElementById("settings").elements["region"].value;
        	var xmlHttp = getXmlHttpRequest();
        	var params = "nickName=" + nickName + "&mood=" + mood + "&comment=" + comment + "&region=" + region;
                xmlHttp.open("POST", "./updateSettings.jsp", false);
        	xmlHttp.setRequestHeader("Content-type",
        			"application/x-www-form-urlencoded");
        	xmlHttp.send(params);
        }
        function click_messageOut(row,from){
        	var messageOut = document.getElementById("messageForm" + row).elements["messageOut" + row].value;
                var xmlHttp = getXmlHttpRequest();
        	var params = "messageOut=" + messageOut + "&row=" + row + "&from=" + from;
                xmlHttp.open("POST", "./messageOut.jsp", false);
        	xmlHttp.setRequestHeader("Content-type",
        			"application/x-www-form-urlencoded");
        	xmlHttp.send(params);
        }
        //]]></script>
</head>
<body>
<h2>ICEpush Place</h2>

<form id="settings">
    Nickname: <input type="text" 
                     value="${person.nickname}"
                     id="nickname"
                     name="nickname"
                     size="20" /><br/><br/>
    What mood are you in?: <select id="mood" name="mood" >
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
                          </select><br/><br/>
    What's on your mind?: <input type="text" 
                                 value="${person.comment}"
                                 id="comment"
                                 name="comment"
                                 size="20"/><br/><br/>
    Change your continent: <select id="region" name="region">
                               <option value="North America"
                               <% if (person.getRegion().compareTo("North America") == 0)  {%>
                               selected
                               <% } %>
                               >North America</option>
                               <option value="Europe"
                               <% if (person.getRegion().compareTo("Europe") == 0)  {%>
                               selected
                               <% } %>
                               >Europe</option>
                               <option value="South America"
                                <% if (person.getRegion().compareTo("South America") == 0)  {%>
                               selected
                               <% } %>
                               >South America</option>
                               <option value="Asia"
                               <% if (person.getRegion().compareTo("Asia") == 0)  {%>
                               selected
                               <% } %>
                               >Asia</option>
                               <option value="Africa"
                               <% if (person.getRegion().compareTo("Africa") == 0)  {%>
                               selected
                               <% } %>
                               >Africa</option>
                               <option value="Antarctica"
                               <% if (person.getRegion().compareTo("Antarctica") == 0)  {%>
                               selected
                               <% } %>
                               >Antarctica</option>
                           </select><br/><br/>
    <input type="submit" value="Update" onclick="click_updateSettings();return false;"/>
</form>

<table>
	<tr>
		<td><h4>ICEpush Place&nbsp</h4></td>
	</tr>
	<tr>
		<td><icep:region group="all" page="/members.jsp"/></td>
	</tr>
</table>
<icep:push group="all"/>
</body>
</html>
