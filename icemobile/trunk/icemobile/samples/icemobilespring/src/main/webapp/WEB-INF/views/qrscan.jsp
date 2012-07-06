<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <title>ICEmobile | qrcode scanner page</title>
        <link href="<c:url value="/resources/form.css" />" rel="stylesheet"
              type="text/css"/>
        <script type="text/javascript"
                src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>
</c:if>
    <div class="ajaxzone">

    <h4>QR Code Scanner</h4>
    <form:form id="qrscanform" method="POST" modelAttribute="QRScanBean">

        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Scan QR code:</label>
                <mobi:qrscan id="scanOne"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <%-- button types: default|important|attention| back--%>
                <mobi:commandButton buttonType='important'
                                    style="float:right;margin-right: 25px;"
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

</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>