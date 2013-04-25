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
<form id="panelconfirmation" class="form">

    <h3>Panel Confirmation</h3>
    
    <mobi:fieldsetGroup styleClass="intro">
        <mobi:fieldsetRow>
            The panelConfirmation component will be dipsplayed when the
            execute button is pressed. The user then has the choice to
            veto execution or let it proceed.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
                <mobi:commandButton value="Open the confirmation panel" 
                         type="button"
                         panelConfirmation="panelConfirmation"
                         buttonType="important"/>
                         
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:panelConfirmation id="panelConfirmation"
            title="Please confirm"
            message="Please select 'Yes' to proceed or 'Cancel' to not."
            type="both"
            acceptLabel="Yes"
            cancelLabel="Cancel"/>
                
</form>