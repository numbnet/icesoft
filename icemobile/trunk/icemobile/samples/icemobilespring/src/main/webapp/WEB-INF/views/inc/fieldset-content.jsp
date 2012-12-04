<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form id="fieldset" class="form fieldsetpage">

	<c:if test="${viewSize eq 'large'}">
		<h3>FieldSets</h3>
	</c:if>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The fieldsetGroup tag can be used to displaying application input.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup styleClass="form">
		<mobi:fieldsetRow group="true">
            Information
        </mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label for="songs">Songs</label>
			<mobi:inputText id="songs" value="2500"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label for="videos">Videos</label>
			<mobi:inputText id="videos" value="5"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label for="photos">Photos</label>
			<mobi:inputText id="photos" value="621"/>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup styleClass="form">
		<mobi:fieldsetRow>
			<label for="first">First
				Name</label>
			<mobi:inputText id="first" value="John"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label for="last">Last
				Name</label>
			<mobi:inputText id="last" value="Doe"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label for="address">Address</label>
			<mobi:inputText id="address" value="123 Fake St."/>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The fieldsetGroup displays sets of application input
			such as for settings panels or input forms. The fieldset 
			tag can contain one or more child fieldsetRows,
			which logically divides the fieldset into subsections. The tag
			has an optional inset attribute which can be useful when
			distinguishing multiple fieldsetGroups on one page.</p>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form>