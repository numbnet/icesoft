<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>
<%@ page import="org.icemobile.input.SubmitNotificationBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="snInfo" class="org.icemobile.input.SubmitNotificationBean" scope="page"/>
<jsp:setProperty name="snInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP commandButton with SubmitNotification</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>style: <%= snInfo.getStyle() %></li>
  <li>styleClass: <%= snInfo.getStyleClass() %></li>
  <li>testInput: <%= snInfo.getTestInput() %></li>
</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${snInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${snInfo.styleClass}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>must submit changes in above values to apply to example below</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
     <h3>Submit Notification</h3>
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
                <mobi:inputText name="testInput" type="text" autoCorrect="off"
                         placeholder="test input for form submit" value="${snInfo.testInput}"/>
            </mobi:fieldsetRow>
            <mobi:fieldsetRow>
                    <mobi:commandButton value="TestSubmitNotification" id="button1"
                             type="button"
                             submitNotification="sn1"
                             buttonType="default"/>
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
        <mobi:submitNotification id="sn1"
                                style="${submitNotification.style}"
                                styleClass = "${submitNotification.styleClass}"
                                disabled="${submitNotification.disabled}">
            <h3>Working....</h3>
        </mobi:submitNotification>
</form>

   <p>
       <ul>
         	<li><a href="../layoutComponents.html">Layout and Navigation Tags</a></li>
    	   <li><a href="../index.html">index</a></li>
       </ul>
   </p>
</body>

</html>