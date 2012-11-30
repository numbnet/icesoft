<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
    uri="http://www.icepush.org/icepush/jsp/icepush.tld"
%>
<form:form id="buttonsform" method="POST" modelAttribute="buttonsBean">

    <c:if test="${viewSize eq 'large'}"><h3>Buttons</h3></c:if>

    <mobi:fieldsetGroup styleClass="intro">
        <mobi:fieldsetRow>
            The CommandButton provides themed buttons in a variety of named types for form submission.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h3>Button Types</h3>
    <mobi:fieldsetGroup styleClass="buttons">
        <mobi:fieldsetRow>
            <mobi:commandButton value="Default" style="" />
            <mobi:commandButton value="Unimportant"
                buttonType="unimportant"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton value="Attention" buttonType="attention"/>
            <mobi:commandButton value="Important" buttonType="important"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton buttonType="back" value="Back"
                styleClass="back"
            />
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton id="disabledDefault" value="Disabled"
                disabled="true"
            />
        </mobi:fieldsetRow>
        <mobi:fieldsetRow style="text-align:left">
            <label>Selected Value: </label>
            <span>${pressed}</span>
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
