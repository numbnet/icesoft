<%@page session="false" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@page import="org.icepush.PushContext" %>
<%@page import="javax.servlet.http.Part" %>
<%@page import="java.io.File" %>
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
    //topic decoded by Dispatch Filter
    String topic = (String) request.getAttribute("brogcast.topic");
    pageContext.setAttribute("topic", topic);
    String clientURI = (String) request.getAttribute(
            "javax.servlet.forward.request_uri");
    String contextPath = application.getContextPath();

    String securityKeyName = "brogcast.topictoken-" + topic;
    String topicToken = (String) application.getAttribute(securityKeyName);
    String keyField = "";
    String userKey = request.getParameter("key");
    //reject request if token does not match
    if (null != topicToken) {
        if (!topicToken.equals(userKey)) {
            response.sendRedirect("..");
            return;
        }
    }

    boolean newSecurity = false;
    String secureRequested = request.getParameter("secure");
    if ("secure".equals(secureRequested)) {
        topicToken = Integer.toString((int) (Math.random() * 100000000), 36);
        application.setAttribute(securityKeyName, topicToken);
        newSecurity = true;
    }
    if (null != topicToken) {
        keyField = "<input type='hidden' name='key' value='" + topicToken + "'>";
        clientURI += "?key=" + topicToken;
    }


    String topicKey = "brogcast.topic-" + topic;
    Integer talkIndex = (Integer) application.getAttribute(topicKey);
    if (null == talkIndex) {
        talkIndex = new Integer(0);
        application.setAttribute(topicKey, talkIndex);
    }

    String fileName = "clip" + talkIndex + ".mp4";

    try {
        for (Part part : request.getParts()) {
            if ("audio/mp4".equals(part.getContentType())) {
                talkIndex = new Integer(talkIndex + 1);
                application.setAttribute(topicKey, talkIndex);
                fileName = "clip" + talkIndex + ".mp4";
                String dirPath = application.getRealPath("/images") + "/" + topic + "/";
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                part.write(dirPath + fileName);
                PushContext.getInstance(application).push(topic);
            }
        }
    } catch (Exception e) {
        //ignoring part decoding Exceptions
    }

    String href = contextPath + "/images/" + topic + "/" + fileName;

    if (newSecurity) {
        response.sendRedirect(clientURI);
    }
    StringBuffer requestURL = request.getRequestURL();
    String requestedPrefix = requestURL.substring(0,
            requestURL.indexOf(contextPath));
    String fullURI = requestedPrefix + clientURI;
%>

<html>
<head>
    <script type="text/javascript" src="code.icepush"></script>
    <script type="text/javascript" src="mobi.icepush"></script>
    <style>
        body {
            font-family: sans-serif;
        }

        .fill {
            max-width: 480;
            width: 100%;
        }

        .mic, .upload, .secure {
            background: white;
            border-radius: 8px;
            background-size: 50%;
            background-repeat: no-repeat;
            background-position: center;
            height: 60px;
            width: 65px;
            font-size: 90%;
        }

        .mic {
            background-image: url("/pushtotalk/images/mic.png");
        }

        .upload {
            background-image: url("/pushtotalk/images/upload.png");
        }

        .secure {
            background-image: url("/pushtotalk/images/secure.png");
            float: right;
        }
    </style>
</head>
<body>
<h3>Talk about "${topic}":</h3>

<form id="form1" method="post"
      action="<%out.print(contextPath + "/upload/" + topic); %>">
    <div class="fill">
        <input type="button" id="mic" class="mic"
               ontouchstart="ice.microphone('mic');"
               ontouchend="ice.microphone('mic');"
               value="talk">
        <input type="button" id="upload1" class="upload"
               onclick="if(window.ice&&ice.mobile){ice.upload('form1');}else{form.submit();}"
               value="upload">
        <input type="submit" id="secure" name="secure" class="secure"
               value="secure">
        <% out.print(keyField); %>
        <br><br>

        <div id="updateRegion">
            <audio id="clip" src="<% out.print(href); %>" controls="controls"
                   autoplay="true"></audio>
        </div>
        <input value="<% out.print(fullURI); %>" class="fill"
               style="color: grey;">
    </div>
</form>
<script>
    function update() {
        var req;
        if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            req = new ActiveXObject("Microsoft.XMLHTTP");
        }
        if (req) {
            req.open("GET", "?r=" + Math.random(), false);
            req.send("");
            var xml = req.responseXML;
            var itemRegion = document.getElementById("updateRegion");
            var update = req.responseXML.getElementsByTagName("update")[0].childNodes[0].nodeValue;
            updateRegion.innerHTML = update;
            var scripts = updateRegion.getElementsByTagName("script");
            for (var i = 0; i < scripts.length; i++) {
                eval(scripts[i].text);
            }
        }
    }
</script>
<script>
    ice.push.configuration.contextPath = "<% out.print(contextPath); %>";
    ice.push.configuration.uriPattern = "<% out.print(contextPath); %>/{{command}}";
</script>
<push:register group="${topic}" callback="update"/>
</body>
</html>
