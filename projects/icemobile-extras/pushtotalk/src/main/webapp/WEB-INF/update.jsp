<%@page session="false" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="application/xml; charset=UTF-8" %>
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
    String contextPath = application.getContextPath();
    String topic = (String) request.getAttribute("brogcast.topic");
    String topicKey = "brogcast.topic-" + topic;
    Integer talkIndex = (Integer) application.getAttribute(topicKey);
    if (null == talkIndex) {
        talkIndex = new Integer(0);
    }
    String fileName = "clip" + talkIndex + ".mp4";
    String href = contextPath + "/images/" + topic + "/" + fileName;
%>
<?xml version="1.0" encoding="UTF-8"?>
<updates>
    <update><![CDATA[
    <audio id="clip" src="<% out.print(href); %>" autoplay="true" controls="controls" >
]]>
    </update>
</updates>
