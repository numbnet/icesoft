<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="micform" method="POST" enctype="multipart/form-data"
	modelAttribute="microphoneBean" cssClass="form">

	<c:if test="${viewSize eq 'large'}"><h3>Microphone</h3></c:if>
    
    <mobi:getEnhanced />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The microphone tag allows access to a mobile devices'
			microphone. The tag renders a simple button that, when
			pressed, activates a native interface that captures audio using the
			device's microphone.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
           Microphone Upload
    </mobi:fieldsetRow>
		<mobi:fieldsetRow>
            Use your mobile device's microphone to make a recording and upload
            it to the server.
        </mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<form:label path="name">
                Author: <form:errors path="name" cssClass="error" />
			</form:label>
			<form:input path="name" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow styleClass="mobi-center">
			<mobi:microphone id="mic" buttonLabel="Record Audio" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<c:if test="${!ios}"><!-- SX on iOS auto-submits -->
        <mobi:commandButton buttonType="important"
                            value="Submit"
                            type="submit"
                            styleClass="submit"/>
    </c:if>

	<c:if test="${not empty micMessage}">
		<div id="message" class="success">${micMessage}</div>
	</c:if>
	<c:if test="${micUploadReady}">
		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<audio src="${micUpload}" controls="controls"></audio>
				<br>
				<a href="${micUpload}">Play</a>
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>
	</c:if>
	<s:bind path="*">
		<c:if test="${status.error}">
			<div id="message" class="error">Form has errors</div>
		</c:if>
	</s:bind>
</form:form>


<script type="text/javascript">
	MvcUtil.enhanceForm("#micform");
</script>
