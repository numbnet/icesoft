<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="qrscanform" method="POST" modelAttribute="QRScanBean" cssClass="form">

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Scan QR code:</label>
            <mobi:scan id="scanOne"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Scanned:</label>
            <span>${QRScanBean.scanOne}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:commandButton buttonType="important"
                       styleClass="submit"
                       value="Submit"
                       type="submit"/>

</form:form>


<script type="text/javascript">
    MvcUtil.enhanceForm("#qrscanform");
</script>
