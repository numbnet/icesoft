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
<%@ page import="org.icemobile.layout.CarouselBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="info" class="org.icemobile.layout.CarouselBean" scope="page"/>
<jsp:setProperty name="info" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP carousel </title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>disabled: <%= info.isDisabled() %>  </li>
  <li>previousLabel: <%= info.getPreviousLabel() %> </li>
  <li>nextLabel: <%= info.getNextLabel() %> </li>
  <li>styleCLass: <%= info.getStyleClass() %></li>
  <li>style: <%= info.getStyle() %></li>

</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
  	   <mobi:fieldsetRow>
			<label>Previous Label</label>
			<mobi:inputText name="previousLabel" type="text" autoCorrect="off"
				placeholder="Style string" value="${info.previousLabel}" />
		</mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Next Label</label>
			<mobi:inputText name="nextLabel" type="text" autoCorrect="off"
				placeholder="Style string" value="${info.nextLabel}" />
		</mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>disabled: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="disabled" value="${info.disabled}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${info.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${info.styleClass}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>html submit button</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
      	<mobi:carousel id="cartwo" name="selectedIndex"
            disabled="${info.disabled}"
            nextLabel="${info.nextLabel}"
            previousLabel="${info.previousLabel}"
		    selectedItem="${info.selectedIndex}">
	        <c:forEach items="${info.textList}" var="item">
			    <mobi:carouselItem>
                    ${item}
                </mobi:carouselItem>
		    </c:forEach>
	    </mobi:carousel>
	<div style="clear: both; padding-top: 10px;">
		<mobi:commandButton type="submit" buttonType="important"
			styleClass="submit" />
	</div>
</form>
<mobi:fieldsetGroup>
    <mobi:fieldsetRow>
        <h3>value submitted by carousel</h3>
           <%= info.getSelectedIndex() %>
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