<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="datetimeform" method="POST" modelAttribute="dateTimeSpinnerBean">

    <mobi:fieldsetGroup style="text-align:right">
        <mobi:fieldsetRow>
            <label style="float:left">Date Spinner</label>
            <div style="display:inline-block">
                <span style="font-size: 10px;float:right;">(yyyy-mm-dd)</span>
                <div style="clear:both;">
                    <mobi:dateSpinner name="dateOne" id="d1"
                        value="${dateTimeSpinnerBean.dateOne}"/>
                </div>
            </div>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label style="float:left">Time Spinner</label>
            <div style="display:inline-block">
                <span style="font-size: 10px;float:right;">(hh:mm AM/PM)</span>
                <div style="clear:both;">
                    <mobi:timeSpinner name="timeOne" id="t1" 
                        style="width:100px;"
                        value="${dateTimeSpinnerBean.timeOne}"/>
                </div>
            </div>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:commandButton buttonType='important'
                        value="Submit"
                        type="submit"
                        styleClass="submit"/>

    <h3>Date & Time Values</h3>
    
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Date</label>
            <span style="float:right">${dateTimeSpinnerBean.dateOne}</span>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Time</label>
            <span style="float:right">${dateTimeSpinnerBean.timeOne}</span>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#datetimeform");
</script>
