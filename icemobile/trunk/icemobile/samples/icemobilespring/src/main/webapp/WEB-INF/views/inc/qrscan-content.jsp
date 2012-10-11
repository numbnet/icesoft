<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="qrscanform" method="POST" modelAttribute="QRScanBean">

    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            <label>Scan QR code:</label>
            <mobi:qrscan id="scanOne" />
            <br/>
        </mobi:fieldSetRow>
        <mobi:fieldSetRow>
           <mobi:commandButton buttonType='important'
                                style="float:right;"
                                value="Submit"
                                type="submit"/>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>
    <%-- QR scanner results --%>
    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            <label>Scanned:</label>
            <span style="font-style:italic">${QRScanBean.scanOne}</span>
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>

</form:form>


<script type="text/javascript">
    MvcUtil.enhanceForm("#qrscanform");
</script>
