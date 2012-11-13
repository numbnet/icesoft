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
    <mobi:carousel id="cartwo"  name="index"
                     selectedItem="${carouselBean.index}">
         <c:forEach items="${carouselBean.images}" var="item">
             <mobi:carouselItem>
                 ${item}
             </mobi:carouselItem>
         </c:forEach>
    </mobi:carousel>
    <mobi:commandButton type="submit" buttonType="important"
        styleClass="submit" style="margin-top:10px;"/>

</form:form>

<h3 style="clear:both;padding-top:10px;">Carousel Echo</h3>
<div id="outputCarousel">
     <mobi:fieldsetGroup>
         <!-- Text output-->
         <mobi:fieldsetRow>
             <label style="vertical-align:top;">Current position:</label>
             <div>
                ${carouselBean.selectedImage}
            </div>
         </mobi:fieldsetRow>
     </mobi:fieldsetGroup>
</div>

<script type="text/javascript">
    MvcUtil.enhanceForm("#carouselForm");
</script>
