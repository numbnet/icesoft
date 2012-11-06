<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>

<mobi:fieldsetGroup>
    <mobi:fieldsetRow>
        <label style="width:100px;text-align:left;display:inline-block;">Title:</label> ${sessionScope.cloudpushBean.title}
    </mobi:fieldsetRow>
    <mobi:fieldsetRow>
        <label style="width:100px;text-align:left;display:inline-block;">Message:</label> ${sessionScope.cloudpushBean.message}
    </mobi:fieldsetRow>
</mobi:fieldsetGroup>
