<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="conform" method="POST" enctype="multipart/form-data"
           modelAttribute="contactBean" cssClass="form">
           
    <mobi:getEnhanced/>
    
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
             <label style="width:50px">Contact Filter:</label>
             <mobi:inputText name="pattern" value="${contactBean.pattern}" style="width:80px"/>
             <mobi:commandButton value="Update" type="submit" style="padding: 0.6em 5px;"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
             <mobi:fetchContact id="rawContact" buttonLabel="Fetch Contact"  
                pattern="${contactBean.pattern}" fields="name, phone, email"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:commandButton buttonType='important'
                        styleClass="submit"
                        value="Submit"
                        type="submit"/>

    <h3>Selected Contact</h3>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Name:</label>
            <span>${contactBean.name}</span>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Phone:</label>
            <span>${contactBean.phone}</span>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Email:</label>
            <span>${contactBean.email}</span>
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
