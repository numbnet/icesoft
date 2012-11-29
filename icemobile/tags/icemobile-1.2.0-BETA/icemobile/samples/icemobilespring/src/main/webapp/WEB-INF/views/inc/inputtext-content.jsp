<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="inputtextform" method="POST"
	modelAttribute="inputTextBean" cssClass="form">

	<h2>HTML5 Input Components</h2>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<p>The HTML5 input component exposes the new set of HTML5 input
				features on supported browsers. Most notable is the type attribute,
				which can act as a hint to load a specific keyboard, which matches
				the specified type.</p>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<label>Text:</label>
			<mobi:inputText name="text" type="text" autoCorrect="off"
				placeholder="Text input" value="${inputTextBean.text}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Number:</label>
			<mobi:inputText name="number" type="number" autoCorrect="off"
				placeholder="Number" value="${inputTextBean.number}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Text area:</label>
			<mobi:inputText name="textarea" type="textarea" autoCorrect="off"
				placeholder="Text area" value="${inputTextBean.textarea}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Password:</label>
			<mobi:inputText name="password" type="password" autoCorrect="off"
				placeholder="Password input" value="${inputTextBean.password}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Date: (yyyy-mm-dd)</label>
			<mobi:inputText name="date" type="date" autoCorrect="off"
				placeholder="yyyy-mm-dd" value="${inputTextBean.date}" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:commandButton buttonType='important' styleClass="submit"
		value="Submit" type="submit" />

	<h3 style="clear: both; margin-top: 10px;">Input Values</h3>
	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<label>Text:</label>
			<span>${inputTextBean.text}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Number:</label>
			<span>${inputTextBean.number}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Text area:</label>
			<span>${inputTextBean.textarea}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Password:</label>
			<span>${inputTextBean.password}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Date</label>
			<span>${inputTextBean.date}</span>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#inputtextform");
</script>
