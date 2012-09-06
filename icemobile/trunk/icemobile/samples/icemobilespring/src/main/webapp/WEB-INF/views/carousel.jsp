<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <jsp:include page="./inc/head.jsp"/>
    <body>
</c:if>
    <div class="ajaxzone">
    <h4>Carousel</h4>
    <form:form id="carouselForm" method="POST" modelAttribute="carouselBean">
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                Carousel of images
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <mobi:carousel id="carouselOne"
                       collection="${carouselBean.images}"
                       selectedIndex="${carouselBean.carouselOne}">
            <mobi:carouselItem ref="myitem" type="java.lang.String"/>
        </mobi:carousel>
    </form:form>

    <script type="text/javascript">
        MvcUtil.enhanceForm("#carouselForm");
    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>