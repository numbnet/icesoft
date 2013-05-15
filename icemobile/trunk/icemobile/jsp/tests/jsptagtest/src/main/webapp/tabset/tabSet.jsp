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
    <title>JSP tabSet Basic</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>orientation: <%= tabInfo.getOrientation() %> </li>
  <li>disabled: <%= tabInfo.isDisabled() %>  </li>
  <li>height: <%= tabInfo.getHeight() %>  </li>
  <li>autoHeight: <%=tabInfo.isAutoHeight() %> </li>
  <li>selectedId: <%= tabInfo.getSelectedId() %> </li>
  <li>autoWidth: <%= tabInfo.isAutoWidth() %></li>
  <li>style: <%= tabInfo.getStyle() %></li>
  <li>fixedPosition: <%= tabInfo.isFixedPosition() %></li>
</ul>
</p>
 <p>
     <B>TabSet bean: </B>
<% if (tabInfo.getSelectedId().equals("")) { %>
  no selectedId
<% } else {
       if ((request.getParameter("selectedId") == null) ||
           (request.getParameter("selectedId").equals(""))) { %>
         selectedId is null or empty
<%     } %>
       selectedId is <%= tabInfo.getSelectedId() %>.
<% } %>
</p>
<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>selected tabPane</label>
           <select name="selectedId" >
              <option value="pane1" ${tabInfo.selectedId=='pane1' ? 'selected' : ''}>pane 1</option>
              <option value="pane2" ${tabInfo.selectedId=='pane2' ? 'selected' : ''}>pane 2</option>
              <option value="pane3" ${tabInfo.selectedId=='pane3' ? 'selected' : ''}>pane 3</option>
          </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>Orientation</label>
           <select name="orientation" >
               <option value="top" ${tabInfo.orientation=='top' ? 'selected' : ''}> top</option>
               <option value="bottom" ${tabInfo.orientation=='bottom' ? 'selected' : ''}>  bottom</option>
           </select>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>autoWidth: </label>
           <mobi:flipswitch id="fs0" labelOn="true" labelOff="false"
                            name="autoWidth" value="${tabInfo.autoWidth}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>autoHeight: </label>
           <mobi:flipswitch id="fs1" labelOn="true" labelOff="false"
                            name="autoHeight" value="${tabInfo.autoHeight}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>disabled: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="disabled" value="${tabInfo.disabled}"/>
       </mobi:fieldsetRow>
        <mobi:fieldsetRow>
           <label>fixedPosition: </label>
           <mobi:flipswitch id="fh" labelOn="true" labelOff="false"
                            name="fixedPosition" value="${tabInfo.fixedPosition}"/>
       </mobi:fieldsetRow>
	   <mobi:fieldsetRow>
			<label>Height</label>
			<mobi:inputText name="height" type="text" autoCorrect="off"
				placeholder="valid css height string" value="${tabInfo.height}" />
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
    	<mobi:tabSet id="tabsetOne"
                            orientation="${tabInfo.orientation}"
                            autoWidth="${tabInfo.autoWidth}"
                            disabled="${tabInfo.disabled}"
                            height="${tabInfo.height}"
                            autoHeight="${tabInfo.autoHeight}"
                            style="${tabInfo.style}"
                            styleClass="${tabInfo.styleClass}"
                            fixedPosition="${tabInfo.fixedPosition}"
                            selectedId="${tabInfo.selectedId}">
		<mobi:tabPane id="pane1" title="Sailor">
		    <%@ include file="../includes/pane1.jsp"%>
		</mobi:tabPane>
	    <mobi:tabPane id="pane2" title="Breaker">
		    <%@ include file="../includes/pane2.jsp"%>
	    </mobi:tabPane>
	    <mobi:tabPane id="pane3" title="Skate">
			<%@ include file="../includes/pane3.jsp"%>
	    </mobi:tabPane>
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