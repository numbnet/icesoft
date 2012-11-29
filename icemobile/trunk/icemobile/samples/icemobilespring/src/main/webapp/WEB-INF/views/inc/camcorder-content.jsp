<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="camcorderform" method="POST"
	enctype="multipart/form-data" modelAttribute="camcorderBean"
	cssClass="form">

	<mobi:getEnhanced />

	<h2>Camcorder Component</h2>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<p>The camcorder component allows access to a mobile devices'
				video camera. The component renders a simple button that, when
				pressed, launches the native video camera application on the device.
				Once a video has been taken and accepted, the user is returned to
				the web application.</p>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<form:label path="name">
                Author: <form:errors path="name" cssClass="error" />
			</form:label>
			<form:input path="name" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:camcorder id="camvid" />
			<mobi:thumbnail for="camvid" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<c:if test="${!ios}">
		<!-- SX on iOS auto-submits -->
		<mobi:commandButton buttonType='important' styleClass="submit"
			value="Submit" type="submit" />
	</c:if>

	<h3>Camcorder Upload</h3>

	<c:if test="${not empty camcorderMessage}">
		<div id="message" class="success">
			${camcorderMessage}<br />
		</div>
	</c:if>
	<c:if test="${camcorderUploadReady}">
		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<video src="${camcorderUpload}" controls="controls"
					class="imageView"></video>
				<br>
				<a href="${camcorderUpload}">Play</a>
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
	MvcUtil.enhanceForm("#camcorderform");

	
</script>