<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="datetimeform" method="POST" modelAttribute="dateTimeSpinnerBean"
    cssClass="form">

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Date Spinner</label>
            <mobi:dateSpinner name="dateOne" id="d1"
                value="${dateTimeSpinnerBean.dateOne}"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Time Spinner</label>
            <mobi:timeSpinner name="timeOne" id="t1" 
                style="width:100px;"
                value="${dateTimeSpinnerBean.timeOne}"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <h3>Spinner Value Echo</h3>
    
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Date</label>
            <span>${dateTimeSpinnerBean.dateOne}</span>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Time</label>
            <span>${dateTimeSpinnerBean.timeOne}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:commandButton buttonType='important'
                        value="Submit"
                        type="submit"
                        styleClass="submit"/>
</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#datetimeform");
</script>
