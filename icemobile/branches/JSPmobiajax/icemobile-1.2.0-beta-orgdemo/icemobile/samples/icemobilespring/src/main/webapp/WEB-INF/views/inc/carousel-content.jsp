<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="carouselForm" method="POST" modelAttribute="carouselBean">

	<c:if test="${viewSize eq 'large'}">
		<h3>Carousel</h3>
	</c:if>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			Carousel items can be moved via a finger swipe or mouse drag gesture.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:carousel id="cartwo" name="index"
		selectedItem="${carouselBean.index}">
		<c:forEach items="${carouselBean.images}" var="item">
			<mobi:carouselItem>
                 ${item}
             </mobi:carouselItem>
		</c:forEach>
	</mobi:carousel>
	<div style="clear: both; padding-top: 10px;">
		<mobi:commandButton type="submit" buttonType="important"
			styleClass="submit" />
	</div>

</form:form>

<h3 style="clear: both; padding-top: 10px;">Carousel Echo</h3>
<div id="outputCarousel">
	<mobi:fieldsetGroup>
		<!-- Text output-->
		<mobi:fieldsetRow>
			<label style="vertical-align: top;">Current position:</label>
			<div>${carouselBean.selectedImage}</div>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</div>

<mobi:fieldsetGroup>
	<mobi:fieldsetRow group="true">
		Description
	</mobi:fieldsetRow>
	<mobi:fieldsetRow>
	The carousel tag displays a list of child elements, allowing a user to swipe right or left
	to view non-visible child elements. The most common use of this component is for displaying a list
	of images that the user can swipe to view additional images in the list.
	</mobi:fieldsetRow>
</mobi:fieldsetGroup>

<script type="text/javascript">
	MvcUtil.enhanceForm("#carouselForm");
</script>
