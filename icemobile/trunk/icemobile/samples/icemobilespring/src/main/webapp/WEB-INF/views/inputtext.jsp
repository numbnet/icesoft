<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <title>ICEmobile | mvc-showcase</title>
        <link href="<c:url value="/resources/form.css" />" rel="stylesheet"
              type="text/css"/>
        <script type="text/javascript"
                src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>
</c:if>
<div id="inputTextContent">

    <form:form id="inputtextform" method="POST" modelAttribute="inputTextBean">

        <h4>Keybaord Input Test</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Text:</label>
                <mobi:inputtext id="text" type="text"
                                autoCorrect="off"
                                placeholder="Text input"
                                value="${inputTextBean.text}"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Number:</label>
                <mobi:inputtext id="number" type="number"
                                autoCorrect="off"
                                placeholder="Number"
                                value="${inputTextBean.number}"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Text area:</label>
                <mobi:inputtext id="textarea" type="textarea"
                                style="width:99%"
                                autoCorrect="off"
                                placeholder="Text area"
                                value="${inputTextBean.textarea}"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Password:</label>
                <mobi:inputtext id="password" type="password"
                                autoCorrect="off"
                                placeholder="Password input"
                                value="${inputTextBean.password}"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Date</label>
                <mobi:inputtext id="date" type="date"
                                autoCorrect="off"
                                placeholder="yyyy-mm-dd"
                                value="${inputTextBean.date}"/>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <%-- button types: default|important|attention| back--%>
        <mobi:commandButton buttonType='important'
                            style="float:right;margin-right: 25px;"
                            value="Submit"
                            type="submit"/>
        <div style="clear:right"/>

        <h4>Keybaord Input Test</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <label>Text:</label>
                <label style="float:right;">${inputTextBean.text}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Number:</label>
                <label style="float:right;">${inputTextBean.number}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Text area:</label>
                <label style="float:right;">${inputTextBean.textarea}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Password:</label>
                <label style="float:right;">${inputTextBean.password}</label>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <label>Date</label>
                <label style="float:right;">${inputTextBean.date}</label>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>

    </form:form>

    <script type="text/javascript">

        $(document).ready(function () {
            $("#inputtextform").submit(function () {
                if (window.ice && ice.upload) {
                    window.ice.handleResponse = function (data) {
                        $("#inputTextContent").replaceWith(unescape(data));
                        $('html, body').animate({ scrollTop:$("#message").offset().top }, 500);
                    }
                    ice.upload($(this).attr("id"));
                    return false;
                }

                var formData = new FormData(this);

                $.ajax({
                    url:$(this).attr("action"),
                    data:formData,
                    cache:false,
                    contentType:false,
                    processData:false,
                    type:'POST',
                    success:function (html) {
                        $("#inputTextContent").replaceWith(html);
                        $('html, body').animate({ scrollTop:$("#message").offset().top }, 500);
                    }
                });

                return false;
            });
        });

    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>