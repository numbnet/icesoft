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
    uri="http://www.icepush.org/icepush/jsp/icepush.tld"
%>
<form:form id="buttonsform" method="POST" modelAttribute="buttonsBean">

    <mobi:largeView><h3>Buttons</h3></mobi:largeView>

    <mobi:fieldsetGroup styleClass="intro">
        <mobi:fieldsetRow>
            The CommandButton provides themed buttons in a variety of named types for form submission.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h3>Button Types</h3>
    <mobi:fieldsetGroup style="text-align:center">
        <mobi:fieldsetRow>
            <mobi:commandButton id="default" value="Default"
                name="selectedType"
                styleClass="span-2"/>
            <mobi:commandButton id="unimportant" value="Unimportant"
                name="selectedType"
                buttonType="unimportant" styleClass="span-2"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton id="attention" value="Attention" 
                name="selectedType"
                buttonType="attention" styleClass="span-2"/>
            <mobi:commandButton id="important" value="Important" 
                name="selectedType"
                buttonType="important" styleClass="span-2"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton id="back" value="Back"
                name="selectedType"
                buttonType="back" styleClass="back"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton id="disabled" value="Disabled"
                name="selectedType"
                disabled="true" />
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="results">
            <label>Selected:</label>
            <span>${buttonsBean.selectedType}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <h3>Vertical Button List</h3>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <mobi:commandButtonGroup id="verticalGroup"
                                     name="selectedVertical"
                                     selectedId="${buttonsBean.selectedVertical}"
                                     orientation="vertical"
                                     style="width:80%;margin:0 auto;">
                <mobi:commandButton value="Yes"  groupId="verticalGroup"
                         type="button" id="yes_a"/>
                <mobi:commandButton value="No"  groupId="verticalGroup"
                        type="button"  id="no_a"/>
                <mobi:commandButton value="Please"  groupId="verticalGroup"
                        type="button" id="please_a"/>
            </mobi:commandButtonGroup>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="results">
            <label>Selected:</label>
            <span>${buttonsBean.selectedVertical}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
        
    <h3>Horizontal button list</h3>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <mobi:commandButtonGroup id="horizontalGroup" 
                                     name="selectedHorizontal"
                                     selectedId="${buttonsBean.selectedHorizontal}"
                                     orientation="horizontal"
                                     style="width:80%;margin:0 auto;">
                <mobi:commandButton value="Yes" id="yes_b" groupId="horizontalGroup"
                         type="button" style="width:33%"/>
                <mobi:commandButton value="No" groupId="horizontalGroup"
                        type="button"  id="no_b" style="width:33%"/>
                <mobi:commandButton value="Please" groupId="horizontalGroup"
                        type="button" id="please_b" style="width:33%"/>
            </mobi:commandButtonGroup>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="results">
            <label>Selected:</label>
            <span>${buttonsBean.selectedHorizontal}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
        
    <mobi:fieldsetGroup>
    	<mobi:fieldsetRow group="true">
    		Description
    	</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            The CommandButton provides themed buttons in a variety of named types for form submission.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

</form:form>
<script type="text/javascript">
    MvcUtil.enhanceForm("#buttonsform");
    ice.mobi.userAjaxRequest = function( options){
        if (options.jspForm){
            $(options.jspForm).submit();
        }
    };
</script>
