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
<%@ page import="org.icemobile.input.MenuButtonBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="menuButtonInfo" class="org.icemobile.input.MenuButtonBean" scope="session"/>
<jsp:setProperty name="menuButtonInfo" property="*" />
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
  <li>disabled: <%= menuButtonInfo.isDisabled() %>  </li>
  <li>selectTitle: <%= menuButtonInfo.getSelectTitle() %>  </li>
  <li>buttonLabel: <%= menuButtonInfo.getButtonLabel() %>  </li>
  <li>styleClass: <%=menuButtonInfo.getStyleClass() %> </li>
  <li>style: <%= menuButtonInfo.getStyle() %></li>
  <li>panelConfirmation id: <%= menuButtonInfo.getPanelConfirmation() %></li>
  <li>submitNotification id <%= menuButtonInfo.getSubmitNotification() %></li>
  <li>selectedValue: <%= menuButtonInfo.getSelectedValue() %></li>

</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
  	   <mobi:fieldsetRow>
			<label>Button Label</label>
			<mobi:inputText name="buttonLabel" type="text" autoCorrect="off"
				placeholder="button label string" value="${menuButtonInfo.buttonLabel}" />
		</mobi:fieldsetRow>
 	    <mobi:fieldsetRow>
			<label>Select Title</label>
			<mobi:inputText name="selectTitle" type="text" autoCorrect="off"
				placeholder="select title or may be null for none" value="${menuButtonInfo.selectTitle}" />
		</mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>disabled: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="disabled" value="${menuButtonInfo.disabled}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${menuButtonInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${menuButtonInfo.styleClass}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label> set PanelConfirmation Id  to pc1 for item 1A</label>
			<mobi:flipswitch id="flip3" labelOn="true" labelOff="false"
                             name="panelConfirmationSet"
				value="${menuButtonInfo.panelConfirmationSet}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>set SubmitNotification Id to sn 1for item 1A</label>
			<mobi:flipswitch id="flip4" labelOn="true" labelOff="false"
                    name="snSet"
				 value="${menuButtonInfo.snSet}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>html submit button</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form id="selFrm">
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>mobi menuButton 2A has SN 2B has PC and SN</label>
    	         <mobi:menuButton id="buttonOne"
                            name="selectedValue"
                            selectedValue="${menuButtonInfo.selectedValue}"
                            buttonLabel="${menuButtonInfo.buttonLabel}"
                            selectTitle="${menuButtonInfo.selectTitle}"
                            disabled="${menuButtonInfo.disabled}"
                            style="${menuButtonInfo.style}"
                            styleClass="${menuButtonInfo.styleClass}">
                      <mobi:menuButtonGroup label="Group1" >
                          <mobi:menuButtonItem id="item1a" label="oneA" value="1A"
                                  panelConfirmation="${menuButtonInfo.panelConfirmation}"
                                  submitNotification="${menuButtonInfo.submitNotification}"/>
                          <mobi:menuButtonItem id="item2a" label="twoA" value="2A"
                                  submitNotification="sn1"/>
                          <mobi:menuButtonItem id="item3a" label="threeA" value="3A"/>
                      </mobi:menuButtonGroup>
                      <mobi:menuButtonGroup label="Group2">
                          <mobi:menuButtonItem id="item1b" label="oneB" value="1B"/>
                          <mobi:menuButtonItem id="item2b" label="twoB" value="2B"
                                  panelConfirmation="pc1"
                                  submitNotification="sn1"/>
                          <mobi:menuButtonItem id="item3b" label="threeB" value="3B"/>
                      </mobi:menuButtonGroup>
                 </mobi:menuButton>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    <mobi:panelConfirmation id="pc1" message=" confirm? "
                            acceptLabel="Accept" cancelLabel="Cancel"/>
    <mobi:submitNotification id="sn1" >
        <h3>Working..with value submitted of:-</h3>
          <%= menuButtonInfo.getSelectedValue() %>
    </mobi:submitNotification>
</form>
<mobi:fieldsetGroup>
    <mobi:fieldsetRow>
        <h3>value submitted by mobi button</h3>
           <%= menuButtonInfo.getSelectedValue() %>
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