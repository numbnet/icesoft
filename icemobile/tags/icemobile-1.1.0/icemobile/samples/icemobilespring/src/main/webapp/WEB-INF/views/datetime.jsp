<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <title>ICEmobile | Date Time demo</title>
        <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
              type="text/css"/>
        <script type="text/javascript"
                src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>
</c:if>

    <div class="ajaxzone">
    <form:form id="datetimeform" method="POST" modelAttribute="dateTimeSpinnerBean">

        <h4>Date and Time Spinners</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Date Spinner</label>
                <mobi:dateSpinner name="dateOne" id="d1"
                        value="${dateTimeSpinnerBean.dateOne}"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Time Spinner</label>
                <mobi:timeSpinner name="timeOne" id="t1" 
                        value="${dateTimeSpinnerBean.timeOne}"/>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <h4>Spinner Value Echo</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Date</label>
                <label style="float:right">${dateTimeSpinnerBean.dateOne}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Time</label>
                <label style="float:right">${dateTimeSpinnerBean.timeOne}</label>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <%-- button types: default|important|attention| back--%>
        <mobi:commandButton buttonType='important'
                            style="float:right;margin-right: 25px;"
                            value="Submit"
                            type="submit"/>
    </form:form>

    <script type="text/javascript">
        MvcUtil.enhanceForm("#datetimeform");
    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>