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

    <c:if test="${viewSize eq 'large'}"><h3>Buttons</h3></c:if>

    <mobi:fieldsetGroup styleClass="intro">
        <mobi:fieldsetRow>
            The CommandButton provides themed buttons in a variety of named types for form submission.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h3>Button Types</h3>
    <mobi:fieldsetGroup styleClass="buttons">
        <mobi:fieldsetRow>
            <mobi:commandButton value="Default" style="" />
            <mobi:commandButton value="Unimportant"
                buttonType="unimportant"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton value="Attention" buttonType="attention"/>
            <mobi:commandButton value="Important" buttonType="important"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton buttonType="back" value="Back"
                styleClass="back"
            />
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton id="disabledDefault" value="Disabled"
                disabled="true"
            />
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
               <mobi:commandButtonGroup id="group1"
                                         name="selectedId"
                                         selectedId="${buttonsBean.selectedId}"
                                         orientation="horizontal">
                    <mobi:commandButton value="Yes" id="button1"
                             type="button"/>
                    <mobi:commandButton value="No"
                            type="button"  id="button2"/>
                    <mobi:commandButton value="Please"
                            type="button" id="button3" />
                </mobi:commandButtonGroup>
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
    mobi.AjaxRequest = function( options){
        if (options.jspForm){
            $(options.jspForm).submit();
        }
    };
</script>
