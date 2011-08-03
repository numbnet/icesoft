<%@page session="false" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="application/xml; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
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

<% String topic = (String) request.getAttribute("brogcast.topic");
    Integer clientCount = (Integer) request.getAttribute("n");
    String topicKey = "brogcast.topic-" + topic;
    List topicItems = (List) application.getAttribute(topicKey);
    if (null == topicItems) {
        topicItems = new ArrayList();
    }
    List updateItems = topicItems.subList(0, topicItems.size() - clientCount);
    pageContext.setAttribute("topicItems", updateItems);
%>
<?xml version="1.0" encoding="UTF-8"?>
<updates>
    <count><%out.print(topicItems.size());%></count>
    <update><![CDATA[
    <c:forEach items="${topicItems}" var="item">
        ${item}
    </c:forEach>
            ]]>
    </update>
</updates>
