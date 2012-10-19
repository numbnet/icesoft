<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="camform" method="POST" enctype="multipart/form-data"
           modelAttribute="cameraBean">
    <mobi:fieldsetGroup inset="true">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <form:label path="name">
                Author: <form:errors path="name" cssClass="error"/>
            </form:label>
            <form:input path="name"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:camera id="cam" />
            <mobi:thumbnail for="cam"
                        style="height:60px;width:65px;vertical-align:middle;float:right;margin:10px;"/>

        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    <%-- button types: default|important|attention| back--%>
    <mobi:commandButton buttonType='important'
                        style="float:right;margin-right: 25px;"
                        value="Submit"
                        type="submit"/>
    <div style="clear:both;"></div>
    <c:if test="${not empty message}">
        <div id="message" class="success">${message}<br/>
            <img style="height:60px;width:60px;" src="${imgPath}">
        </div>
    </c:if>
    <c:if test="${imgUploaded}">
        <mobi:fieldsetGroup inset="true">
            <mobi:fieldsetRow>
                <img style="text-align:middle;width:100%;padding:5px" src="${imgPath}">
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
    MvcUtil.enhanceForm("#camform");
</script>
