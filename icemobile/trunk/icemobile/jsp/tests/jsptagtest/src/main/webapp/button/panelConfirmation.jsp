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
<%@ page import="org.icemobile.input.PanelConfirmationBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="pcInfo" class="org.icemobile.input.PanelConfirmationBean" scope="page"/>
<jsp:setProperty name="pcInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP commandButton with PanelConfirmation</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>type: <%= pcInfo.getType() %>  </li>
  <li>title: <%= pcInfo.getTitle() %>  </li>
  <li>message: <%= pcInfo.getMessage() %>  </li>
  <li>acceptLabel: <%= pcInfo.getAcceptLabel() %>  </li>
  <li>cancelLabel: <%= pcInfo.getCancelLabel() %>  </li>
  <li>style: <%= pcInfo.getStyle() %></li>
  <li>styleClass: <%= pcInfo.getStyleClass() %></li>
  <li>testInput: <%= pcInfo.getTestInput() %></li>
</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>Type : button can only be submit for now until JSP ajax submit is complete</label>
           <select name="type" >
              <option value="both" ${pcInfo.type=='both' ? 'selected' : ''}>both</option>
              <option value="acceptOnly" ${pcInfo.type=='acceptOnly' ? 'selected' : ''}>acceptOnly</option>
              <option value="cancelOnly" ${pcInfo.type=='cancelOnly' ? 'selected' : ''}>cancelOnly</option>
          </select>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Title</label>
			<mobi:inputText name="title" type="text" autoCorrect="off"
				placeholder="String title" value="${pcInfo.title}" />
	   </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Message</label>
			<mobi:inputText name="message" type="text" autoCorrect="off"
				placeholder="String message" value="${pcInfo.message}" />
	   </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>accept Button label</label>
			<mobi:inputText name="acceptLabel" type="text" autoCorrect="off"
				placeholder="Accept Label" value="${pcInfo.acceptLabel}" />
	   </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Cancel Button label</label>
			<mobi:inputText name="cancelLabel" type="text" autoCorrect="off"
				placeholder="Cancel Label" value="${pcInfo.cancelLabel}" />
	   </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${pcInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${pcInfo.styleClass}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>must submit changes in above values to apply to example below</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
     <h3>Panel Confirmation</h3>
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
                <mobi:inputText name="testInput" type="text" autoCorrect="off"
                         placeholder="test input for form submit" value="${pcInfo.testInput}"/>
            </mobi:fieldsetRow>
            <mobi:fieldsetRow>
                    <mobi:commandButton value="TestPanelConfirmation" id="button1"
                             type="button"
                             panelConfirmation="pc1"
                             buttonType="default"/>
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
        <mobi:panelConfirmation id="pc1"
                title="${pcInfo.title}"
                message="${pcInfo.message}"
                type="${pcInfo.type}"
                acceptLabel="${pcInfo.acceptLabel}"
                cancelLabel="${pcInfo.cancelLabel}"
                style="${pcInfo.style}"
                styleClass="${pcInfo.styleClass}"/>
</form>

   <p>
       <ul>
         	<li><a href="../layoutComponents.html">Layout and Navigation Tags</a></li>
    	   <li><a href="../index.html">index</a></li>
       </ul>
   </p>

</body>

</html>