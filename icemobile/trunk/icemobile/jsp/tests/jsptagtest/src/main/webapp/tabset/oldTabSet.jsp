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
<%@ page import="org.icemobile.layout.TabSetBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="tabInfo" class="org.icemobile.layout.TabSetBean" scope="page"/>
<jsp:setProperty name="tabInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP old markup tabSet Basic</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>selectedTab index: <%= tabInfo.getIndex() %> </li>
  <li>autoWidth: <%= tabInfo.isAutoWidth() %></li>
  <li>style: <%= tabInfo.getStyle() %></li>
  <li>styleClass: <%= tabInfo.getStyleClass()  %></li>
</ul>
</p>
 <p>
     <B>TabSet bean: </B>
<% if (tabInfo.getIndex() < 0) { %>
  no selectedId
<% }else { %>
       selectedTab is <%= tabInfo.getIndex() %>.
<% } %>
</p>
<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>selected tabPane</label>
           <select name="index" >
              <option value=0>pane 1</option>
              <option value=1>pane 2</option>
              <option value=2>pane 3</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>autoWidth: </label>
           <mobi:flipswitch id="fs0" labelOn="true" labelOff="false"
                            name="autoWidth" value="${tabInfo.autoWidth}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${tabInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${tabInfo.styleClass}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>submit button</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
  	<mobi:tabSet id="tabsetOne" selectedTab="${tabInfo.index}"
		autoWidth="${tabInfo.autoWidth}"
        style="${tabInfo.style}"
        styleClass="${tabInfo.styleClass}">
		<mobi:headers>
			<mobi:header>
				<span class="mobitab">Ice Sailer</span>
			</mobi:header>
			<mobi:header>
				<span class="mobitab">Ice Breaker</span>
			</mobi:header>
			<mobi:header>
				<span class="mobitab">Ice Skater</span>
			</mobi:header>
		</mobi:headers>
		<mobi:content>
			<mobi:contentPane>
		        <%@ include file="../includes/pane1.jsp"%>
			</mobi:contentPane>
			<mobi:contentPane>
				<%@ include file="../includes/pane2.jsp"%>
			</mobi:contentPane>
			<mobi:contentPane>
				<%@ include file="../includes/pane3.jsp"%>
			</mobi:contentPane>
		</mobi:content>
	</mobi:tabSet>
</form>
   <p>
       <ul>
         	<li><a href="../layoutComponents.html">Layout and Navigation Tags</a></li>
    	   <li><a href="../index.html">index</a></li>
       </ul>
   </p>
</body>

</html>