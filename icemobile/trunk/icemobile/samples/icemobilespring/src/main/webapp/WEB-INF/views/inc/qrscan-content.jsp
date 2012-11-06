<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="qrscanform" method="POST" modelAttribute="QRScanBean">

    <mobi:fieldsetGroup style="text-align:center;">
        <mobi:fieldsetRow>
            <mobi:getEnhanced/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="mobi-grid">
            <label class="mobi-col mobi-weight1" style="text-align:left;width:100px;">Scan QR code:</label>
            <mobi:scan id="scanOne" styleClass="mobi-col mobi-weight2"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow styleClass="mobi-grid">
            <label class="mobi-col mobi-weight1" style="text-align:left;width:100px;">Scanned:</label>
            <span styleClass="mobi-col mobi-weight2" style="font-style:italic">${QRScanBean.scanOne}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:commandButton buttonType='important'
                       styleClass="submit"
                       value="Submit"
                       type="submit"/>

</form:form>


<script type="text/javascript">
    MvcUtil.enhanceForm("#qrscanform");
</script>
