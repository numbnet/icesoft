<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <title>ICEmobile | mvc-showcase</title>
        <link href="<c:url value="/resources/form.css" />" rel="stylesheet"
              type="text/css"/>
        <script type="text/javascript"
                src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
    </head>
    <body>
</c:if>
    <div class="ajaxzone">

    <form:form id="micform" method="POST" enctype="multipart/form-data"
               modelAttribute="microphoneBean" >
        <h4>Microphone</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <form:label path="name">
                    Author: <form:errors path="name" cssClass="error"/>
                </form:label>
                <form:input path="name"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                <mobi:microphone id="mic"/>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>

        <%-- button types: default|important|attention| back--%>
        <mobi:commandButton buttonType='important'
                            style="float:right;margin-right: 25px;"
                            value="Submit"
                            type="submit"/>
        <div style="clear:both"/>
        <c:if test="${not empty message}">
            <div id="message" class="success">${message}<br/>
                <audio src="media/clip.mp4" controls="controls"/>
            </div>
        </c:if>
        <s:bind path="*">
            <c:if test="${status.error}">
                <div id="message" class="error">Form has errors</div>
            </c:if>
        </s:bind>
    </form:form>


    <script type="text/javascript">
        MvcUtil.enhanceForm("#micform");
    </script>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>
