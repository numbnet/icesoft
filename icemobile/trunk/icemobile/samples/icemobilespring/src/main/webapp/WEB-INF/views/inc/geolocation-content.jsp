<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="geolocationform" method="POST"
           modelAttribute="geolocationBean" >

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Press for geolocation: </label>
            <mobi:geolocation id="geo1" name="geolocation"/>
            <%-- button types: default|important|attention| back--%>
            <mobi:commandButton buttonType='important'
                                style="float:right;margin-right: 25px;"
                                value="Locate"
                                type="submit"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h4>Position</h4>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Latitude:</label>
            <label style="float:right">${geolocationBean.lat}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Longitude:</label>
            <label style="float:right">${geolocationBean.lon}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Altitude:</label>
            <label style="float:right">${geolocationBean.alt}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Direction:</label>
            <label style="float:right">${geolocationBean.dir}</label>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#geolocationform");
</script>
