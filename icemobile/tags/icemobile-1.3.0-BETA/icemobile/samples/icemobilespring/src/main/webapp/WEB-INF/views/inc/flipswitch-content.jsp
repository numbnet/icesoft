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
<form:form id="flipswitchform" method="POST"
	modelAttribute="flipSwitchBean">

	<mobi:largeView><h3>FlipSwitch</h3></mobi:largeView>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The flipSwitch displays a command button that
			toggles between on and off states.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<label class="ui-input-text">Auto-Capitalization</label>
			<mobi:flipswitch id="onOffFlipSwitch" labelOn="On"
				labelOff="Off" name="onOffFlipSwitch" style="float:right"
				value="${flipSwitchBean.onOffFlipSwitch}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label class="ui-input-text">Auto-Correction</label>
			<mobi:flipswitch id="yesNoFlipSwitch" labelOn="Yes"
				labelOff="No" name="yesNoFlipSwitch" style="float:right"
				value="${flipSwitchBean.yesNoFlipSwitch}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label class="ui-input-text">Check Spelling</label>
			<mobi:flipswitch id="trueFalseFlipSwitch" labelOn="True"
				labelOff="False" name="trueFalseFlipSwitch" style="float:right"
				value="${flipSwitchBean.trueFalseFlipSwitch}" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:commandButton buttonType='important' styleClass="submit"
		value="Submit" type="submit" />

	<h3>Flip Switch Value Echo</h3>
	<mobi:fieldsetGroup styleClass="results">
		<mobi:fieldsetRow>
			<label>Auto-Capitalization</label>
			<span>${flipSwitchBean.onOffFlipSwitch}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Auto-Correction</label>
			<span>${flipSwitchBean.yesNoFlipSwitch}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Check Spelling</label>
			<span>${flipSwitchBean.trueFalseFlipSwitch}</span>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The flipSwitch displays a command button that
			toggles between on and off states. This UI element is common on the
			iOS platform for toggling on/off settings. This tag generates
			a server-side action event when pressed.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#flipswitchform");
</script>
