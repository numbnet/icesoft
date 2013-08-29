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
<%@ page import="org.icemobile.input.ButtonBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="buttonInfo" class="org.icemobile.input.ButtonBean" scope="page"/>
<jsp:setProperty name="buttonInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP commandButton</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>disabled: <%= buttonInfo.isDisabled() %>  </li>
  <li>buttonType: <%= buttonInfo.getButtonType() %>  </li>
  <li>type: <%=buttonInfo.getType() %> </li>
  <li>autoWidth: <%= buttonInfo.getStyleClass() %></li>
  <li>style: <%= buttonInfo.getStyle() %></li>

</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>ButtonType</label>
           <select id="selButType" name="buttonType" >
              <option value="default" ${buttonInfo.buttonType=='default' ? 'selected' : ''}>default</option>
              <option value="important" ${buttonInfo.buttonType=='important' ? 'selected' : ''}>important</option>
              <option value="back" ${buttonInfo.buttonType=='back' ? 'selected' : ''}>back</option>
              <option value="attention" ${buttonInfo.buttonType=='attention' ? 'selected' : ''}>attention</option>
              <option value="unimportant" ${buttonInfo.buttonType=='unimportant' ? 'selected' : ''}>unimportant</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>Type : button can only be submit for now until JSP ajax submit is complete</label>
           <select name="type" >
              <option value="button" ${buttonInfo.type=='button' ? 'selected' : ''}>button</option>
              <option value="submit" ${buttonInfo.type=='submit' ? 'selected' : ''}>submit</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>disabled: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="disabled" value="${buttonInfo.disabled}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${buttonInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${buttonInfo.styleClass}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>html updateOptions button</label>
            <input id="updateBtn" type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>some input text</label>
            <mobi:inputText name="someText" value="${buttonInfo.someText}"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>mobi commandButton</label>
    	         <mobi:commandButton id="buttonOne"
                            value="${buttonInfo.value}"
                            name="${buttonInfo.someText}"
                            disabled="${buttonInfo.disabled}"
                            style="${buttonInfo.style}"
                            styleClass="${buttonInfo.styleClass}"
                            type="${buttonInfo.type}"
                            buttonType="${buttonInfo.buttonType}"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<mobi:fieldsetGroup>
    <mobi:fieldsetRow>
        <h3>value submitted by mobi button</h3>
           <%= buttonInfo.getSomeText() %>
    </mobi:fieldsetRow>
</mobi:fieldsetGroup>

   <p>
       <ul>
         	<li><a href="../layoutComponents.html">Layout and Navigation Tags</a></li>
    	   <li><a href="../index.html">index</a></li>
       </ul>
   </p>
</body>

</html>