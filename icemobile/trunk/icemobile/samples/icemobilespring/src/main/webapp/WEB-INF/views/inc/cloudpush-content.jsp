<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="cloudpushform" method="POST" enctype="multipart/form-data"
           modelAttribute="cloudpushBean">

    <mobi:fieldsetGroup style="text-align:center;">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        
        <mobi:fieldsetRow styleClass="mobi-grid">
            <form:label path="title" style="width:100px;text-align:left;" cssClass="mobi-col mobi-weight1">
                Title: <form:errors path="title" cssClass="error"/>
            </form:label>
            <form:input path="title" cssClass="mobi-col mobi-weight2"/>
        </mobi:fieldsetRow>
        
        <mobi:fieldsetRow styleClass="mobi-grid">
            <form:label path="message" style="width:100px;text-align:left;" cssClass="mobi-col mobi-weight1">
                Message: <form:errors path="message" cssClass="error"/>
            </form:label>
            <form:input path="message" cssClass="mobi-col mobi-weight2"/>
        </mobi:fieldsetRow>
        
        <mobi:fieldsetRow styleClass="mobi-grid">
            <form:label path="delay" style="width:100px;text-align:left;" cssClass="mobi-col mobi-weight1">
                Delay: <form:errors path="delay" cssClass="error"/>
            </form:label>
            <form:select path="delay" cssClass="mobi-col mobi-weight2">
                <form:option value="5" label="5s" />
                <form:option value="10" label="10s" />
                <form:option value="15" label="15s" />
            </form:select>
        </mobi:fieldsetRow>

        <mobi:fieldsetRow styleClass="mobi-grid mobi-justify">
            <mobi:commandButton buttonType='important'
                                name="pushType"
                                value="Simple Push"
                                type="submit"
                                styleClass="mobi-col mobi-weight1"/>
            <mobi:commandButton buttonType='attention'
                                name="pushType"
                                value="Priority Push"
                                type="submit"
                                styleClass="mobi-col mobi-weight1"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h4>Message</h4>
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
