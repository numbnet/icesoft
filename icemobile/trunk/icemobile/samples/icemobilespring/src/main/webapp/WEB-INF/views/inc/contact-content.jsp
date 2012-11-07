<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="conform" method="POST" enctype="multipart/form-data"
           modelAttribute="contactBean" cssClass="form">
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <div class="center">
                 <mobi:fetchContact id="contactOne" label="fetch Contacts"  
                    pattern="a" fields="contact, phone"/>
            </div>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:commandButton buttonType='important'
                        styleClass="submit"
                        value="Submit"
                        type="submit"/>

    <h4>Selected Contact List Echo Display</h4>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Selected Contact(s):</label>
            <span>${contactBean.contactOne}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <s:bind path="*">
        <c:if test="${status.error}">
            <div id="message" class="error">Form has errors</div>
        </c:if>
    </s:bind>
</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#conform");
</script>
