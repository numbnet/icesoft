<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="buttonsform" method="POST" modelAttribute="buttonsBean"
	cssClass="form">

	<h2>Button Components</h2>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<p>TBD</p>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<h3>Button Types</h3>
	<mobi:fieldsetGroup>
		<mobi:fieldsetRow styleClass="mobi-center">
			<mobi:commandButton value="Default" style="width:130px" />
			<mobi:commandButton value="Unimportant" buttonType="unimportant"
				style="width:130px" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow styleClass="mobi-center">
			<mobi:commandButton value="Attention" buttonType="attention"
				style="width:130px" />
			<mobi:commandButton value="Important" buttonType="important"
				style="width:130px" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow styleClass="mobi-center">
			<mobi:commandButton buttonType="back" value="Back"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:commandButton id="disabledDefault" value="Disabled"
				disabled="true" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Selected Value: </label>
			<c:if test="${pressed != null}">
				<span>${pressed}</span>
			</c:if>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
	<input type="hidden" name="submitB" id="hiddenSubmitVal" />

</form:form>
<script type="text/javascript">
	$('input[type=submit]').click(function(e) {
		$('#hiddenSubmitVal').val(this.value);
	});
	MvcUtil.enhanceForm("#buttonsform");
</script>
