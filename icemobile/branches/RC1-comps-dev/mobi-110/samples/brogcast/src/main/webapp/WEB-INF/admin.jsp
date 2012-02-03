<%@page session="false" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@page import="org.icepush.PushContext" %>
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
        application.setAttribute(topicKey, topicItems);
    }
    topicItems.add(0, request.getParameter("title"));
    PushContext.getInstance(application).push(topic);
%>

<html>
<body>
<h3>Welcome to Brogcast Admin for <% out.print(topic); %>.</h3>

<form method="post">
    Title <input name="title" type="text"><br>
    Comment <textarea name="comment"></textarea><br>
    <input type="submit">
</form>
</body>
</html>
