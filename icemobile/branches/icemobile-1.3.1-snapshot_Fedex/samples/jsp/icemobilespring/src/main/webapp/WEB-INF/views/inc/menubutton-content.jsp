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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
    uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="menubutton" method="POST" enctype="multipart/form-data" modelAttribute="menuButtonBean">

    <mobi:largeView><h3>Menu Button</h3></mobi:largeView>

    <mobi:fieldsetGroup styleClass="intro">
        <mobi:fieldsetRow>
            The menuButton allows a user to select and execute a menu item.
        </mobi:fieldsetRow>
        <mobi:fieldsetRow style="text-align:center">
            <mobi:menuButton id="mnuBtn1"
                             name="menu1Value"
                             buttonLabel="Menu Button"
                             selectTitle="Select an item"
                             selectedValue="${menuButtonBean.menu1Value}">
                <mobi:menuButtonItem id="create"
                        label="Create Record"
                        value="create"/>
                <mobi:menuButtonItem id="update"
                        label="Update Record"
                        value="update"/>
                <mobi:menuButtonItem id="delete"
                        label="Delete Record"
                        value="delete"/>
            </mobi:menuButton>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="results">
            <label>Selected Command:</label>
            <span>${menuButtonBean.menu1Value}</span>
        </mobi:fieldsetRow>
        
        
        <mobi:fieldsetRow>The Menu Button also supports option groups with the menuButtonGroup component</mobi:fieldsetRow>
        <mobi:fieldsetRow style="text-align:center">
           <mobi:menuButton id="menuBtnGroup" name="menu2Value"
                        buttonLabel="Menu Button Group"
                        selectTitle="Select an item" 
                        selectedValue="${menuButtonBean.menu2Value}">
               <mobi:menuButtonGroup label="Menu Group A" >
                   <mobi:menuButtonItem id="itemA1" label="Item A1" value="A1"/>
                   <mobi:menuButtonItem id="itemA2" label="Item A2" value="A2"/>
                   <mobi:menuButtonItem id="itemA3" label="Item A3" value="A3"/>
               </mobi:menuButtonGroup>
               <mobi:menuButtonGroup label="Menu Group B">
                   <mobi:menuButtonItem id="itemB1" label="Item B1" value="B1"/>
                   <mobi:menuButtonItem id="itemB2" label="Item B2" value="B2"/>
                   <mobi:menuButtonItem id="itemB3" label="Item B3" value="B3"/>
                </mobi:menuButtonGroup>
           </mobi:menuButton>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="results">
            <label>Selected Command:</label>
            <span>${menuButtonBean.menu2Value}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
  
</form:form>
<script type="text/javascript">
    MvcUtil.enhanceForm("#menubutton");
    ice.mobi.userAjaxRequest = function( options){
        if (options.jspForm){
            $(options.jspForm).submit();
        }
    };
</script>

