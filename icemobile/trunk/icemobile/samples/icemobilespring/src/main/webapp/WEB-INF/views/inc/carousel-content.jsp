<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="carouselForm" method="POST" modelAttribute="carouselBean">
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            Carousel of images
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    <mobi:carousel id="carouselOne"
                   collection="${carouselBean.images}"
                   selectedIndex="${carouselBean.carouselOne}">
        <mobi:carouselItem ref="myitem" type="java.lang.String"/>
    </mobi:carousel>
</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#carouselForm");
</script>
