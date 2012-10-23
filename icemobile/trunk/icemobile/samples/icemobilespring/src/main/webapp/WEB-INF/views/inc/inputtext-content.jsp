<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="inputtextform" method="POST" modelAttribute="inputTextBean">

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Text:</label>
            <mobi:inputText name="text" type="text"
                            autoCorrect="off"
                            placeholder="Text input"
                            value="${inputTextBean.text}"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Number:</label>
            <mobi:inputText name="number" type="number"
                            autoCorrect="off"
                            placeholder="Number"
                            value="${inputTextBean.number}"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Text area:</label>
            <mobi:inputText name="textarea" type="textarea"
                            style="width:99%;float:none;"
                            autoCorrect="off"
                            placeholder="Text area"
                            value="${inputTextBean.textarea}"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Password:</label>
            <mobi:inputText name="password" type="password"
                            autoCorrect="off"
                            placeholder="Password input"
                            value="${inputTextBean.password}"/>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Date</label>
            <mobi:inputText name="date" type="date"
                            autoCorrect="off"
                            placeholder="yyyy-mm-dd"
                            value="${inputTextBean.date}"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    <%-- button types: default|important|attention| back--%>
    <mobi:commandButton buttonType='important'
                        style="float:right;margin-right: 25px;"
                        value="Submit"
                        type="submit"/>
    <div style="clear:right"></div>

    <h4>Input Values</h4>
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>Text:</label>
            <label style="float:right;">${inputTextBean.text}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Number:</label>
            <label style="float:right;">${inputTextBean.number}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Text area:</label>
            <label style="float:right;">${inputTextBean.textarea}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Password:</label>
            <label style="float:right;">${inputTextBean.password}</label>
        </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>Date</label>
            <label style="float:right;">${inputTextBean.date}</label>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#inputtextform");
</script>
