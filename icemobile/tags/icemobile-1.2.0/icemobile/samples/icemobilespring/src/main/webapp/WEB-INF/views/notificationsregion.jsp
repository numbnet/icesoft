<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>

<mobi:fieldsetGroup>
    <mobi:fieldsetRow>
        <label>Title:</label> 
        <span style="text-align:right">${sessionScope.notificationsBean.title}</span>
    </mobi:fieldsetRow>
    <mobi:fieldsetRow>
        <label>Message:</label> 
        <span style="text-align:right">${sessionScope.notificationsBean.message}</span>
    </mobi:fieldsetRow>
</mobi:fieldsetGroup>
