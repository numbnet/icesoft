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
<form:form id="conform" method="POST" enctype="multipart/form-data"
	modelAttribute="contactBean">

	<mobi:largeView><h3>Contact List</h3></mobi:largeView>

	<mobi:getEnhanced />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The fetchContact tag provides access to the contact database
            or address book on the device.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow style="text-align:center">
			<mobi:fetchContact id="rawContact" buttonLabel="Fetch Contact"
				fields="name, phone, email" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:commandButton buttonType='important' styleClass="submit"
		value="Submit" type="submit" />

	<h3>Selected Contact</h3>
	<mobi:fieldsetGroup styleClass="results">
		<mobi:fieldsetRow>
			<label>Name:</label>
			<span>${contactBean.name}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Phone:</label>
			<span>${contactBean.phone}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Email:</label>
			<span>${contactBean.email}</span>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<s:bind path="*">
		<c:if test="${status.error}">
			<div id="message" class="error">Form has errors</div>
		</c:if>
	</s:bind>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The fetchContact tag provides access to the contact database
            or address book on the device.  The user selects a single contact,
            and the name, email address, and phone number are returned as
            values.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#conform");
</script>
