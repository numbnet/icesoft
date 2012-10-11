<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="flipswitchform" method="POST" modelAttribute="flipSwitchBean">
   <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            <label>Auto-Capitalization</label>
            <mobi:flipSwitch id="onOffFlipSwitch" labelOn="On"
                             style="float:right"
                             labelOff="Off"
                             name="onOffFlipSwitch"
                             value="${flipSwitchBean.onOffFlipSwitch}"/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <label>Auto-Correction</label>
            <mobi:flipSwitch id="yesNoFlipSwitch" labelOn="Yes"
                             style="float:right"
                             labelOff="No"
                             name="yesNoFlipSwitch"
                             value="${flipSwitchBean.yesNoFlipSwitch}"/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <label>Check Spelling</label>
            <mobi:flipSwitch id="trueFalseFlipSwitch" labelOn="True"
                             style="float:right"
                             labelOff="False"
                             name="trueFalseFlipSwitch"
                             value="${flipSwitchBean.trueFalseFlipSwitch}"/>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>
    <%-- button types: default|important|attention| back--%>
    <mobi:commandButton buttonType='important'
                        style="float:right;margin-right: 25px;"
                        value="Submit"
                        type="submit"/><br>
    <h4>Flip Switch Value Echo</h4>
    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            <label>Auto-Capitalization</label>
            <label style="float:right">${flipSwitchBean.onOffFlipSwitch}</label>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <label>Auto-Correction</label>
            <label style="float:right">${flipSwitchBean.yesNoFlipSwitch}</label>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <label>Check Spelling</label>
            <label style="float:right">${flipSwitchBean.trueFalseFlipSwitch}</label>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>
</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#flipswitchform");
</script>
