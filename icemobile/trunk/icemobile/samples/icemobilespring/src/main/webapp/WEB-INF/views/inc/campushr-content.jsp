<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="campushrform" method="POST" enctype="multipart/form-data"
           modelAttribute="cameraBean">

    <mobi:fieldsetGroup style="text-align:center;">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <c:if test="${not empty message}">
            <mobi:fieldsetRow>
                <div id="message" class="success">${message}<br/>
                    <img style="height:60px;width:60px;" src="${imgPath}">
                </div>
            </mobi:fieldsetRow>
       </c:if>
       <s:bind path="*">
          <c:if test="${status.error}">
                <mobi:fieldsetRow>
                        <div id="message" class="error">Form has errors</div>
                </mobi:fieldsetRow>
           </c:if>
       </s:bind>
       <mobi:fieldsetRow group="true" style="text-align:left;">Personal Info</mobi:fieldsetRow>
       <mobi:fieldsetRow styleClass="mobi-grid">
            <form:label path="name" cssClass="mobi-col mobi-weight1" style="width:100px;text-align:left;">
                Name <form:errors path="name" cssClass="error"/>
            </form:label>
            <form:input path="name" cssClass="mobi-col mobi-weight2"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <mobi:camera id="cam"/>
            <mobi:thumbnail for="cam"/>
            <push:region group="camPush" page="/camregion"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow group="true" style="text-align:left;">Request Additional Info</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label><form:checkbox path="additionalInfo[mvc]" value="true"/>on
                Spring MVC</label>
            <label><form:checkbox path="additionalInfo[java]" value="true"/>on
                Java (4-eva)</label>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:commandButton buttonType='important'
                        style="float:right;margin-right: 10px;width:50%;"
                        value="Submit"
                        type="submit"/>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#campushrform");
</script>
