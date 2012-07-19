<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
        <meta name="apple-mobile-web-app-capable" content="yes"/>
        <meta name="apple-mobile-web-app-status-bar-style" content="black"/>


        <title>ICEmobile | mvc-showcase</title>
        <link href="<c:url value="/resources/form.css" />" rel="stylesheet"
              type="text/css"/>
        <script type="text/javascript"
                src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
        <link href="<c:url value="/resources/org.icefaces.component.skins/bberry/carousel/default-carousel.css" />"
              rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="code.icepush"></script>


    </head>
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
        MvcUtil.enhanceForm("#carouselform");
    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>