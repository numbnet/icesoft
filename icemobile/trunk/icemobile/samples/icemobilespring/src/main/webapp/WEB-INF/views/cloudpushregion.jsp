<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>

<mobi:fieldsetGroup>
    <mobi:fieldsetRow>
        <label>Title:</label> ${sessionScope.cloudpushBean.title}
    </mobi:fieldsetRow>
    <mobi:fieldsetRow>
        <label>Message:</label> ${sessionScope.cloudpushBean.message}
    </mobi:fieldsetRow>
</mobi:fieldsetGroup>
