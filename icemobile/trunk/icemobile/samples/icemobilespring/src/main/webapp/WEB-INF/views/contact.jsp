<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <jsp:include page="./inc/head.jsp"/>
    <body>
</c:if>
    <div class="ajaxzone">
    <form:form id="conform" method="POST" enctype="multipart/form-data"
               modelAttribute="contactBean">
        <h4>Contact List</h4>


        <mobi:fetchContact id="contactOne" label="fetch Contacts"  multipleSelect="true"   pattern="a"
        fields="contact, phone" />


        <mobi:commandButton buttonType='important'
                            style="float:right;margin-right: 25px;"
                            value="Submit"
                            type="submit"/><br>

        <h4>Selected Contact List Echo Display</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Selected Contact(s):</label>
                <label style="float:right">${contactBean.contactOne}</label>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>


        <s:bind path="*">
            <c:if test="${status.error}">
                <div id="message" class="error">Form has errors</div>
            </c:if>
        </s:bind>
    </form:form>

    <script type="text/javascript">
        MvcUtil.enhanceForm("#conform");
    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>