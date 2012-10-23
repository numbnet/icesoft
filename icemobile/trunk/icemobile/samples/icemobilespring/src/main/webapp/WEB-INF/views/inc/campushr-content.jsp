<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="campushrform" method="POST" enctype="multipart/form-data"
           modelAttribute="cameraBean">

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h4>Form</h4>

    <c:if test="${not empty message}">
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
                <div id="message" class="success">${message}<br/>
                    <img style="height:60px;width:60px;" src="${imgPath}">
                </div>
            </mobi:fieldsetRow>
       </mobi:fieldsetGroup>
   </c:if>
   
   <s:bind path="*">
      <c:if test="${status.error}">
           <mobi:fieldsetGroup>
                <mobi:fieldsetRow>
                        <div id="message" class="error">Form has errors</div>
                </mobi:fieldsetRow>
           </mobi:fieldsetGroup>
       </c:if>
   </s:bind>
   
   <mobi:fieldsetGroup>
        <mobi:fieldsetRow group="true">Personal Info</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <form:label path="name">
                Name <form:errors path="name" cssClass="error"/>
            </form:label>
            <form:input path="name"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <mobi:camera id="cam"/>
            <mobi:thumbnail for="cam"/>
            <push:region group="camPush" page="/camregion"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow group="true">Request Additional Info</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label><form:checkbox path="additionalInfo[mvc]" value="true"/>on
                Spring MVC</label>
            <label><form:checkbox path="additionalInfo[java]" value="true"/>on
                Java (4-eva)</label>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <button type="submit">Submit</button>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h3>New tags </h3><br/>

    <mobi:fieldsetGroup id="parent">
        <mobi:fieldsetRow id="first">
            <h4> This element in first field set row</h4>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow id="second">
            <h3> This element is in the second row </h3>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#campushrform");
</script>
