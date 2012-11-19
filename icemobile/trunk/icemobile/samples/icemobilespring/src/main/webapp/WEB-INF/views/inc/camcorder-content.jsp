<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="camcorderform" method="POST" enctype="multipart/form-data"
               modelAttribute="camcorderBean" cssClass="form">
               
    <mobi:getEnhanced/>
    
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <form:label path="name">
                Author: <form:errors path="name" cssClass="error"/>
            </form:label>
            <form:input path="name"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:camcorder id="camvid"/>
            <mobi:thumbnail for="camvid"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:commandButton buttonType='important'
                        styleClass="submit"
                        value="Submit"
                        type="submit"/>
                        
    <h3>Camcorder Upload</h3>
                        
    <c:if test="${not empty message}">
        <div id="message" class="success">${message}<br/>
        </div>
    </c:if>
    <c:if test="${mediaReady}">
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
                <video src="media/video.mp4" controls="controls"></video>
                <br><a href="media/video.mp4">Play</a>
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
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