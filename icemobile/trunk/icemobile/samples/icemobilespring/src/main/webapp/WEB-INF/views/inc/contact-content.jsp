<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="conform" method="POST" enctype="multipart/form-data"
           modelAttribute="contactBean">
    <mobi:fieldsetGroup style="text-align:center;">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
             <mobi:fetchContact id="contactOne" label="fetch Contacts"  
                pattern="a" fields="contact, phone" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>


     <mobi:commandButton buttonType='important'
                        styleClass="submit"
                        value="Submit"
                        type="submit"/>
     <div style="clear:both;"></div>

    <h4>Selected Contact List Echo Display</h4>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow styleClass="mobi-grid">
            <label class="mobi-col mobi-weight1" style="text-align:left;">Selected Contact(s):</label>
            <span class="mobi-col mobi-weight2">${contactBean.contactOne}</span>
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
