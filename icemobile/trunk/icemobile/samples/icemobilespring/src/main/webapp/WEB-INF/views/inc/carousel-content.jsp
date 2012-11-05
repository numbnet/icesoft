<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="carouselForm" method="POST" modelAttribute="carouselBean">
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            Carousel items can be moved via a finger swipe or mouse drag gesture.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    <mobi:carousel id="cartwo"
                     selectedItem="${carouselBean.carouselTwo}">
         <c:forEach items="${carouselBean.imagesA}" var="item">
             <mobi:carouselItem>
                 ${item}
             </mobi:carouselItem>
         </c:forEach>
    </mobi:carousel>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#carouselForm");
</script>
