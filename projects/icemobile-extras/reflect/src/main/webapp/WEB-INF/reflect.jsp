<%@page session="true" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@page import="javax.servlet.http.Part" %>
<%@page import="java.io.File" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.net.URLEncoder" %>
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
    String IMAGE_KEY = "org.icemobile.imageURI";
    String clientURI = (String) request.getAttribute(
            "javax.servlet.forward.request_uri");
    StringBuffer requestURL = request.getRequestURL();
    String contextPath = request.getContextPath();
    String requestedPrefix = requestURL.substring(0,
            requestURL.indexOf(contextPath));
    String contextPrefix = requestedPrefix + contextPath;


    String imageURI = (String) session.getAttribute(IMAGE_KEY);
    if (null == imageURI)  {
        imageURI = contextPrefix + "/images/camera.png";
    }
    try {
        for (Part part : request.getParts()) {
            if ("image/jpeg".equals(part.getContentType())) {
                String fileName = session.getId() + ".jpg";
                String dirPath = application.getRealPath("/images") + "/";
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                String fullPath = dirPath + fileName;
                part.write(fullPath);
                imageURI = contextPrefix + "/images/" + fileName;
                session.setAttribute(IMAGE_KEY, imageURI);
            }
        }
    } catch (Exception e) {
        //ignoring part decoding Exceptions
    }

    String fullURI = requestedPrefix + clientURI;
    String encodedURI = URLEncoder.encode(fullURI);
%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <title>PhotoUp Upload and View</title>
    <style>
        body {
            font-family: sans-serif;
        }

        .fill {
            max-width: 480;
            width: 100%;
            text-align:center;
        }

        .camera {
            background: white;
            border-radius: 8px;
            background-size: 50%;
            background-repeat: no-repeat;
            background-position: center;
            height: 60px;
            width: 100px;
            font-size: 90%;
        }

        .camera {
            background-image: url("<% out.print(contextPrefix); %>/images/camera.png");
        }

    </style>
    <script>
    function updateLink()  {
        var url = document.getElementById('inputURL').value;
        var idParam = document.getElementById('idParam').value;
        var command = 'camera&id=' + idParam;
        document.getElementById('computedLink').value = 
            'icemobile://c=' + escape(command) + '&u=' + escape(url);
    }
    </script>
</head>
<body>

<form id="form1" method="post">
    <div class="fill">
        <h3>Upload and View a Photo</h3>
        <input type="button" class="camera"
               onclick="window.location='icemobile://c=camera%3Fid%3Dnone&u=<% out.print(encodedURI); %>&JSESSIONID=<% out.print(session.getId()); %>';" value="camera">
        <br/>
        <img style="width:80%;margin-top:10px;margin-bottom:10px;"
             src="<% out.print(imageURI); %>">
        <h3>Construct Custom URL</h3>
        <input type="text" id="inputURL" value="<% out.print(fullURI); %>" onchange="updateLink()" style="width:80%" ><br>
        <input type="text" id="idParam" value="id" onchange="updateLink()" size="10"><br>
        <textarea id="computedLink" style="width:80%">
        </textarea>
        <script>
        updateLink();
        </script>
    </div>
</form>
</body>
</html>
