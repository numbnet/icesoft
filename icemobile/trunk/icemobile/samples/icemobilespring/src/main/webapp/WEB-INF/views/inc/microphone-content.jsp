<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="micform" method="POST" enctype="multipart/form-data"
	modelAttribute="microphoneBean" cssClass="form">

	<mobi:getEnhanced />

	<h2>Microphone Component</h2>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
            Description
    </mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<p>The microphone component allows access to a mobile devices'
				microphone. The component renders a simple button that, when
				pressed, activates a native interface that captures audio using the
				device's microphone.</p>
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

	<mobi:commandButton buttonType="important" value="Submit" type="submit"
		styleClass="submit" />

	<c:if test="${not empty message}">
		<div id="message" class="success">${message}</div>
	</c:if>
	<c:if test="${mediaReady}">
		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<audio src="${clipName}" controls="controls"></audio>
				<br>
				<a href="${clipName}">Play</a>
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
