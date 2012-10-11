<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="camcorderform" method="POST" enctype="multipart/form-data"
               modelAttribute="camcorderBean">
    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            <mobi:getEnhanced/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <form:label path="name">Author: <form:errors path="name"
                                                         cssClass="error"/></form:label>
            <form:input path="name"/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
            <mobi:camcorder id="camvid"/>
            <mobi:thumb for="camvid"
                        style="height:60px;width:65px;vertical-align:middle;float:right;margin:10px;"/>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>
    <%-- button types: default|important|attention| back--%>
    <mobi:commandButton buttonType='important'
                        style="float:right;margin-right: 25px;"
                        value="Submit"
                        type="submit"/>
    <div style="clear:both;"/>

    <c:if test="${not empty message}">
        <div id="message" class="success">${message}<br/>
        </div>
    </c:if>
    <c:if test="${mediaReady}">
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <video src="media/video.mp4" controls="controls"/>
                <br><a href="media/video.mp4">Play</a>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
    </c:if>
    <s:bind path="*">
        <c:if test="${status.error}">
            <div id="message" class="error">Form has errors</div>
        </c:if>
    </s:bind>
</form:form>
<script type="text/javascript">
    MvcUtil.enhanceForm("#camcorderform");
</script>