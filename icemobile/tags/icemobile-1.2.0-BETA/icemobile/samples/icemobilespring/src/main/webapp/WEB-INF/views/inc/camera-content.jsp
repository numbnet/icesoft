<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="camform" method="POST" enctype="multipart/form-data"
	modelAttribute="cameraBean" cssClass="form">

	<c:if test="${viewSize eq 'large'}">
		<h3>Camera</h3>
	</c:if>

	<mobi:getEnhanced />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The camera tag allows access to a mobile devices' camera. 
        </mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<form:label path="name">
                Author: <form:errors path="name" cssClass="error" />
			</form:label>
			<mobi:inputText id="name" name="name" value="${cameraBean.name}"
				placeholder="Your name" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:thumbnail for="cam" />
			<mobi:camera id="cam" style="margin-left:20px" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<c:if test="${!sxRegistered}">
		<!-- SX on iOS auto-submits -->
		<mobi:commandButton buttonType="important" styleClass="submit"
			value="Submit" type="submit" />
	</c:if>

	<h3>Camera Upload</h3>

	<c:if test="${not empty cameraMessage}">
		<div id="message" class="success">${cameraMessage}</div>
		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<img src="${cameraUpload}" class="imageView">
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>
	</c:if>
	<s:bind path="*">
		<c:if test="${status.error}">
			<div id="message" class="error">Form has errors</div>
		</c:if>
	</s:bind>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The camera tag allows access to a mobile devices' camera. The tag renders
			a simple button that, when pressed, launches the native camera application 
			on the device. Once a picture has been taken and accepted, the user is returned 
			to the web application.  
        </mobi:fieldsetRow>
	</mobi:fieldsetGroup>

</form:form>
<script type="text/javascript">
	MvcUtil.enhanceForm("#camform");
</script>
