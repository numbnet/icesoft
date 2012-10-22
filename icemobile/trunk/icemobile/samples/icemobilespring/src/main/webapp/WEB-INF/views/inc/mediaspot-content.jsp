<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<h4>Add Marker Photo</h4>
<form:form id="mediaspotform" method="POST" enctype="multipart/form-data"
           modelAttribute="mediaspotBean">
    <mobi:fieldsetGroup inset="true">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Title: </label>
            <form:input path="title" placeholder="Title"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow style="height:90px;">
            <mobi:geolocation id="location"/>
            <mobi:camera id="spotcam"/>
            <mobi:thumbnail for="spotcam"
                        style="height:60px;width:65px;vertical-align:middle;float:right;margin:10px;"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:commandButton buttonType='important'
                                value="Submit"
                                type="submit"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:fieldsetGroup inset="true">
        <mobi:fieldsetRow>
            View augmented: 
            <mobi:augmentedReality id="selection">
                <c:forEach items="${locations}" var="location" >
                    <mobi:augmentedRealityLocation 
                        locationLabel="${location.title}"
                        locationLat="${location.latitude}"
                        locationLon="${location.longitude}"
                        locationAlt="${location.altitude}"
                        locationDir="${location.direction}"
                        locationIcon="${location.fileName}"
                         />
                </c:forEach>
            </mobi:augmentedReality>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <%-- button types: default|important|attention|back--%>
    <div style="clear:both;"/>
    <s:bind path="*">
        <c:if test="${status.error}">
            <div id="message" class="error">Form has errors</div>
        </c:if>
    </s:bind>
    <c:if test="${null != selection}">
        <mobi:fieldsetGroup inset="true">
            <mobi:fieldsetRow>
                Selected location:<br />
                <img style="height:120px;width:120px;padding:5px;"
                     src="${imgPath}"/>

                <div style="font-style:italic">${selection}</div>
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
    </c:if>
    
    <mobi:fieldsetGroup inset="true">
        <mobi:fieldsetRow>
            Use your camera to add location pictures to the
            augmented reality overlay. Touch an icon in the augmented
            reality view to select and view that image.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#mediaspotform");
</script>