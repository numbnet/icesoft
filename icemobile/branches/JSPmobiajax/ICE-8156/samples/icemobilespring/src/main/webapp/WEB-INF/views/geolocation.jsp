<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <title>ICEmobile | Geolocation demo</title>
        <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
              type="text/css"/>
        <script type="text/javascript"
                src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>
</c:if>
    <div class="ajaxzone">

    <form:form id="geolocationform" method="POST"
               modelAttribute="geolocationBean" >

        <h4>Geolocation</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Press for geolocation: </label>
                <mobi:geolocation id="geo1" name="geolocation"/>
                <%-- button types: default|important|attention| back--%>
                <mobi:commandButton buttonType='important'
                                    style="float:right;margin-right: 25px;"
                                    value="Locate"
                                    type="submit"/>
                <div style="clear:both"/>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>

        <h4>Position</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Latitude:</label>
                <label style="float:right">${geolocationBean.lat}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Longitude:</label>
                <label style="float:right">${geolocationBean.lon}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Altitude:</label>
                <label style="float:right">${geolocationBean.alt}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Direction:</label>
                <label style="float:right">${geolocationBean.dir}</label>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>

    </form:form>

    <script type="text/javascript">
        MvcUtil.enhanceForm("#geolocationform");
    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>