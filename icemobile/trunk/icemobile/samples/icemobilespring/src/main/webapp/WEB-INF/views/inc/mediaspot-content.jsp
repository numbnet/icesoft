<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<h4>Add Marker Photo</h4>
<form:form id="mediaspotform" method="POST" enctype="multipart/form-data"
           modelAttribute="mediaspotBean">
    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            <mobi:getEnhanced/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <label>Title: </label>
            <form:input path="title" placeholder="Title"/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow style="height:90px;">
            <mobi:geolocation id="location"/>
            <mobi:camera id="spotcam"/>
            <mobi:thumb for="spotcam"
                        style="height:60px;width:65px;vertical-align:middle;float:right;margin:10px;"/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <mobi:commandButton buttonType='important'
                                value="Submit"
                                type="submit"/>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>

    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
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
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>

    <%-- button types: default|important|attention|back--%>
    <div style="clear:both;"/>
    <s:bind path="*">
        <c:if test="${status.error}">
            <div id="message" class="error">Form has errors</div>
        </c:if>
    </s:bind>
    <c:if test="${null != selection}">
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                Selected location:<br />
                <img style="height:120px;width:120px;padding:5px;"
                     src="${imgPath}"/>

                <div style="font-style:italic">${selection}</div>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
    </c:if>
    
    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            Use your camera to add location pictures to the
            augmented reality overlay. Touch an icon in the augmented
            reality view to select and view that image.
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#mediaspotform");
</script>