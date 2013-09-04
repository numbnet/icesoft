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
<form:form id="micform" method="POST" enctype="multipart/form-data"
	modelAttribute="microphoneBean">

	<mobi:largeView><h3>Microphone</h3></mobi:largeView>

	<mobi:getEnhanced />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
            Use your mobile device's microphone to make a recording and upload
            it to the server.
        </mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<mobi:inputText id="name" name="name" label="Author:"
                value="${microphoneBean.name}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow style="text-align:center">
			<mobi:microphone id="mic" buttonLabel="Record Audio"/>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<c:if test="${!mobiClient.SXRegistered}">
		<!-- SX on iOS auto-submits -->
		<mobi:commandButton buttonType="important" value="Submit"
			type="submit" styleClass="submit" />
	</c:if>

	<h3>Audio Upload</h3>

	<c:if test="${not empty micMessage}">
		<div id="message" class="success">${micMessage}</div>
	</c:if>
	<c:if test="${micUploadReady}">
		<mobi:fieldsetGroup>
			<mobi:fieldsetRow style="text-align:center">
				<mobi:audioPlayer linkLabel="Play"
                            url="${micUpload}"
                            controls="true" preload="auto" style="width:60%"/>
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>
	</c:if>
	<c:if test="${not empty micError}">
        <div id="message" class="error">${micError}</div>
    </c:if>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The microphone tag allows access to a mobile devices'
			microphone. The tag renders a simple button that, when
			pressed, activates a native interface that captures audio using the
			device's microphone.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form:form>


<script type="text/javascript">
	MvcUtil.enhanceForm("#micform");
</script>
