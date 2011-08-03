<%@page session="false" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@page import="org.icepush.PushContext" %>
<%@page import="javax.servlet.http.Part" %>
<%@page import="java.io.File" %>
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
    //topic decoded by BrogcastDispatch Filter
    String topic = (String) request.getAttribute("brogcast.topic");
    String clientURI = (String) request.getAttribute(
            "javax.servlet.forward.request_uri");

    String securityKeyName = "brogcast.topictoken-" + topic;
    String topicToken = (String) application.getAttribute(securityKeyName);
    String keyField = "";
    String userKey = request.getParameter("key");
    //reject request if token does not match
    if (null != topicToken) {
        if (!topicToken.equals(userKey)) {
            response.sendRedirect("src");
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
    List topicItems = (List) application.getAttribute(topicKey);
    if (null == topicItems) {
        topicItems = new ArrayList();
        application.setAttribute(topicKey, topicItems);
    }
    String item = null;

    String title = request.getParameter("title");
    if (null != title) {
        item = "<h4>" + title + "</h4>\n";
    }

    String comment = request.getParameter("comment");
    if (null != comment) {
        item += "<div class='fill' style='font-family: serif;' >\n" + comment + "\n</div><br>\n";
    }

    try {
        for (Part part : request.getParts()) {
            if ("image/jpeg".equals(part.getContentType())) {
                String fileName = "image" + String.valueOf(topicItems.size()) + ".jpg";
                String dirPath = application.getRealPath("/images") + "/" + topic + "/";
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                part.write(dirPath + fileName);
                item += "<img src=\"images/" + topic + "/" + fileName + "\">";
            }
        }
    } catch (Exception e) {
        //ignoring part decoding Exceptions
    }
    if (null != item) {
        topicItems.add(0, item);
        PushContext.getInstance(application).push(topic);
    }

    if (newSecurity) {
        response.sendRedirect(clientURI);
    }
    StringBuffer requestURL = request.getRequestURL();
    String requestedPrefix = requestURL.substring(0,
            requestURL.indexOf("/brogcast/"));
    String fullURI = requestedPrefix + clientURI;
%>

<html>
<head>
    <style>
        body {
            font-family: sans-serif;
        }

        .fill {
            max-width: 480;
            width: 100%;
        }

        .camera, .upload, .secure {
            background: white;
            border-radius: 8px;
            background-size: 50%;
            background-repeat: no-repeat;
            background-position: center;
            height: 60px;
            width: 65px;
            font-size: 90%;
        }

        .camera {
            background-image: url("/brogcast/images/camera.png");
        }

        .upload {
            background-image: url("/brogcast/images/upload.png");
        }

        .secure {
            background-image: url("/brogcast/images/secure.png");
            float: right;
        }
    </style>
</head>
<body>
<h3>Post to "<% out.print(topic); %>":</h3>

<form id="form1" method="post">
    <div class="fill">
        <input name="title" type="text" class="fill" placeholder="Title"><br>
        <textarea name="comment" class="fill" rows="5"
                  placeholder="Comments"></textarea><br>
        <input type="button" id="camera1" class="camera"
               onclick="ice.camera('camera1');"
               value="camera">
        <img style="height:60px;width:65px;vertical-align:middle;"
             id="camera1-thumb"
             src="/brogcast/images/camera.png">
        <input type="button" id="upload1" class="upload"
               onclick="if(window.ice&&ice.mobile){ice.upload('form1');}else{form.submit();}"
               value="upload">
        <input type="submit" id="secure" name="secure" class="secure"
               value="secure">
        <% out.print(keyField); %>
        <br><br>
        <input value="<% out.print(fullURI); %>" class="fill"
               style="color: grey;">
    </div>
</form>
</body>
</html>
