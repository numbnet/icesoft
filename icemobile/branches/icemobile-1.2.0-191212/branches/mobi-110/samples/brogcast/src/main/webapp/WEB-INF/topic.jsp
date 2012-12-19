<%@page session="false" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%--
  ~ Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions an
  ~ limitations under the License.
  --%>

<%
    String topic = (String) request.getAttribute("brogcast.topic");
    String topicKey = "brogcast.topic-" + topic;
    List topicItems = (List) application.getAttribute(topicKey);
    if (null == topicItems) {
        topicItems = new ArrayList();
    }
    pageContext.setAttribute("topicItems", topicItems);
    pageContext.setAttribute("topic", topic);
%>
<html>
<head>
    <script type="text/javascript" src="code.icepush"></script>
    <script type="text/javascript" src="mobi.icepush"></script>
    <style>
        body {
            font-family: sans-serif;
        }
    </style>
</head>
<body>
Welcome to Brogcast for ${topic}.


<h3 id="itemRegionTitle"><%out.print(topicItems.size());%> Items in this
    blog:</h3>

<div id="itemRegion">
    <c:forEach items="${topicItems}" var="item">
        ${item}
    </c:forEach>
</div>
<script>
    var itemCount = <%out.print(topicItems.size());%>;
    function update() {
        var req;
        if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            req = new ActiveXObject("Microsoft.XMLHTTP");
        }
        if (req) {
            req.open("GET", "?n=" + itemCount, false);
            req.send("");
            var xml = req.responseXML;
            var itemRegion = document.getElementById("itemRegion");
            var newItemCount = xml.getElementsByTagName("count")[0].childNodes[0].nodeValue;
            var update = req.responseXML.getElementsByTagName("update")[0].childNodes[0].nodeValue;
            var regionTitle = document.getElementById("itemRegionTitle");
            itemCount = newItemCount;
            regionTitle.childNodes[0].nodeValue = itemCount + " Items in this blog";
            var updateDiv = document.createElement("div");
            updateDiv.innerHTML = update;
            itemRegion.insertBefore(updateDiv, itemRegion.childNodes[0]);
        }
    }
</script>
<push:register group="${topic}" callback="update"/>
</body>
</html>
