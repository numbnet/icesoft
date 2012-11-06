<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="camcorderform" method="POST" enctype="multipart/form-data"
               modelAttribute="camcorderBean">
    <mobi:fieldsetGroup style="text-align:center;">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="mobi-grid">
            <form:label path="name" cssClass="mobi-col mobi-weight1" style="width:100px;text-align:left;">
                Author: <form:errors path="name" cssClass="error"/>
            </form:label>
            <form:input path="name" cssClass="mobi-col mobi-weight2"/>
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
                        
    <div style="clear:both;"></div>

    <c:if test="${not empty message}">
        <div id="message" class="success">${message}<br/>
        </div>
    </c:if>
    <c:if test="${mediaReady}">
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
                <video src="media/video.mp4" controls="controls"/>
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