<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="cloudpushform" method="POST" enctype="multipart/form-data" cssClass="form"
           modelAttribute="cloudpushBean">
           
    <mobi:getEnhanced/>

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <form:label path="title">
                Title: <form:errors path="title" cssClass="error"/>
            </form:label>
            <form:input path="title"/>
        </mobi:fieldsetRow>
        
        <mobi:fieldsetRow>
            <form:label path="message">
                Message: <form:errors path="message" cssClass="error"/>
            </form:label>
            <form:input path="message"/>
        </mobi:fieldsetRow>
        
        <mobi:fieldsetRow>
            <form:label path="delay">
                Delay: <form:errors path="delay" cssClass="error"/>
            </form:label>
            <form:select path="delay">
                <form:option value="5" label="5s" />
                <form:option value="10" label="10s" />
                <form:option value="15" label="15s" />
            </form:select>
        </mobi:fieldsetRow>

        <mobi:fieldsetRow styleClass="mobi-justify">
            <mobi:commandButton buttonType='important'
                                name="pushType"
                                value="Simple Push"
                                type="submit"/>
            <mobi:commandButton buttonType='attention'
                                name="pushType"
                                value="Priority Push"
                                type="submit"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h3>Message</h3>
    <push:region group="cloudPush" page="/cloudpushregion"/>

    <div style="clear:both;"></div>
    <s:bind path="*">
        <c:if test="${status.error}">
            <div id="message" class="error">Form has errors</div>
        </c:if>
    </s:bind>
</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#cloudpushform");
</script>
