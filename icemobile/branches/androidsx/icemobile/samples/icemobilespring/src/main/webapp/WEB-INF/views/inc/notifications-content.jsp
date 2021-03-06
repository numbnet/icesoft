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
<form:form id="notificationsform" method="POST"
	enctype="multipart/form-data" cssClass="form"
	modelAttribute="notificationsBean">

	<c:if test="${viewSize eq 'large'}">
		<h3>Cloud Push</h3>
	</c:if>

	<mobi:getEnhanced />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			Enter a custom message below and send a delayed asynchronous
				notification with either a 'Simple Push' or 'Priority Push'
				strategy. The Priority Push strategy will use a native device
				notification if ICEmobile has been activated on your device, but the
				notification is not deliverable via the normal web route. This
				mechanism can use Cloud Push to actively deliver the message over
				cloud-based message delivery networks.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<h3>Send Notification</h3>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<form:label path="title">
                Title: <form:errors path="title" cssClass="error" />
			</form:label>
			<form:input path="title" />
		</mobi:fieldsetRow>

		<mobi:fieldsetRow>
			<form:label path="message">
                Message: <form:errors path="message" cssClass="error" />
			</form:label>
			<form:input path="message" />
		</mobi:fieldsetRow>

		<mobi:fieldsetRow>
			<form:label path="delay">
                Delay: <form:errors path="delay" cssClass="error" />
			</form:label>
			<form:select path="delay">
				<form:option value="5" label="5s" />
				<form:option value="10" label="10s" />
				<form:option value="15" label="15s" />
			</form:select>
		</mobi:fieldsetRow>

		<mobi:fieldsetRow styleClass="mobi-center">
			<mobi:commandButton buttonType='important' name="pushType"
				value="Simple Push" type="submit" />
			<mobi:commandButton buttonType='attention' name="pushType"
				value="Priority Push" type="submit" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<h3>Message</h3>
	<push:region group="notifications" page="/notificationsregion" />

	<div style="clear: both;"></div>
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
			Real-time, asynchronous, push-based user notifications can be used
			with ICEmobile. User notifications can be delivered via a pure-web
			mechanism of Ajax Push, or higher priority delivery mechanisms that
			leverage native integration and a cloud-based infrastructure with <strong>Cloud
				Push</strong>. A multi-channel delivery system can deliver a user
			notification via a web-based Ajax Push, through a device notification, 
			or through email.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#notificationsform");
</script>
