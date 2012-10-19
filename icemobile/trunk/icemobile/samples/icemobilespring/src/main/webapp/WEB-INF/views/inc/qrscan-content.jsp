<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="qrscanform" method="POST" modelAttribute="QRScanBean">

    <mobi:fieldsetGroup inset="true">
        <mobi:fieldsetRow>
            <label>Scan QR code:</label>
            <mobi:scan id="scanOne" />
            <br/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
           <mobi:commandButton buttonType='important'
                                style="float:right;"
                                value="Submit"
                                type="submit"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    <%-- QR scanner results --%>
    <mobi:fieldsetGroup inset="true">
        <mobi:fieldsetRow>
            <label>Scanned:</label>
            <span style="font-style:italic">${QRScanBean.scanOne}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

</form:form>


<script type="text/javascript">
    MvcUtil.enhanceForm("#qrscanform");
</script>
