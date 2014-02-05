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
<%@ page import="org.icemobile.input.ButtonGroupBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="buttonGroupInfo" class="org.icemobile.input.ButtonGroupBean" scope="session"/>
<jsp:setProperty name="buttonGroupInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP commandButtonGroup</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>orientation: <%= buttonGroupInfo.getOrientation() %>  </li>
  <li>disabled: <%= buttonGroupInfo.isDisabled() %>  </li>
  <li>buttonType: <%= buttonGroupInfo.getButtonType() %>  </li>
  <li>type: <%=buttonGroupInfo.getType() %> </li>
  <li>autoWidth: <%= buttonGroupInfo.getStyleClass() %></li>
  <li>style: <%= buttonGroupInfo.getStyle() %></li>
  <li>selectedId: <%= buttonGroupInfo.getSelectedId() %> </li>

</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>ButtonType</label>
           <select name="buttonType" >
              <option value="default" ${buttonGroupInfo.buttonType=='default' ? 'selected' : ''}>default</option>
              <option value="important" ${buttonGroupInfo.buttonType=='important' ? 'selected' : ''}>important</option>
              <option value="back" ${buttonGroupInfo.buttonType=='back' ? 'selected' : ''}>back</option>
              <option value="attention" ${buttonGroupInfo.buttonType=='attention' ? 'selected' : ''}>attention</option>
              <option value="unimportant" ${buttonGroupInfo.buttonType=='unimportant' ? 'selected' : ''}>unimportant</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>Type : button can only be submit for now until JSP ajax submit is complete</label>
           <select name="type" >
              <option value="button" ${buttonGroupInfo.type=='button' ? 'selected' : ''}>button</option>
              <option value="submit" ${buttonGroupInfo.type=='submit' ? 'selected' : ''}>submit</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>Orientation: </label>
           <select name="orientation" >
              <option value="horizontal" ${buttonGroupInfo.orientation=='horizontal' ? 'selected' : ''}>horizontal</option>
              <option value="vertical" ${buttonGroupInfo.orientation=='vertical' ? 'selected' : ''}>vertical</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>disabled: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="disabled" value="${buttonGroupInfo.disabled}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${buttonGroupInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${buttonGroupInfo.styleClass}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>must submit changes in above values to apply to example below</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
     <h3>Button Group</h3>
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
                <mobi:commandButtonGroup id="group1"
                                         name="selectedId"
                                         selectedId="${buttonGroupInfo.selectedId}"
                                         orientation="${buttonGroupInfo.orientation}"
                                         styleClass="${buttonGroupInfo.styleClass}"
                                         disabled="${buttonGroupInfo.disabled}"
                                         style="${buttonGroupInfo.style}">
                    <mobi:commandButton value="Yes" id="button1"
                             type="button"  groupId="group1"
                             buttonType="${buttonGroupInfo.buttonType}"/>
                    <mobi:commandButton value="No"  groupId="group1"
                            type="button"  id="button2"
                            buttonType="${buttonGroupInfo.buttonType}"/>
                    <mobi:commandButton value="Please" groupId="group1"
                            type="button" id="button3"
                            buttonType="${buttonGroupInfo.buttonType}" />
                </mobi:commandButtonGroup>
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
</form>

   <p>
       <ul>
         	<li><a href="../layoutComponents.html">Layout and Navigation Tags</a></li>
    	   <li><a href="../index.html">index</a></li>
       </ul>
   </p>

</body>

</html>