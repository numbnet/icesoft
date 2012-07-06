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
    <div class="ajaxzone">

    <h4>ICEmobile - Buttons</h4>
    <form:form id="buttonsform" method="POST" modelAttribute="buttonsBean">
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                <mobi:commandButton name="submitB" buttonType="default"
                                    value="default"/>
                <mobi:commandButton name="submitB" buttonType='important'
                                    value="important"/>
                <mobi:commandButton name="submitB" buttonType='attention'
                                    value="attention"/>
                <mobi:commandButton name="submitB" value="plain"
                                    style="float:none;"/>
            </mobi:fieldSetRow>
            <mobi:fieldSetRow>
                You Pressed:
                <c:if test="${pressed != null}">
                    ${pressed}
                </c:if>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
    </form:form>

    <script type="text/javascript">

        $(document).ready(function () {
            $("#buttonsform").click(function (e) {
                window.iceButtonTracker = e.target;
            });
            $("#buttonsform").submit(function () {
                var updateRegion = $(this).closest("div.ajaxzone");
                if (window.ice && ice.upload) {
                    window.ice.handleResponse = function (data) {
                        var tracker = document
                                .getElementById("iceButtonTrackerHidden");
                        if (tracker) {
                            tracker.parentNode.removeChild(tracker);
                        }
                        updateRegion.replaceWith(unescape(data));
                        $('html, body').animate({ scrollTop:$("#message").offset().top }, 500);
                    }
                    //ice.upload should accept a submitting element
                    //so the hidden field is not necessary here
                    if (window.iceButtonTracker) {
                        $('<input>').attr({
                            type:'hidden',
                            id:'iceButtonTrackerHidden',
                            name:window.iceButtonTracker.name,
                            value:window.iceButtonTracker.value
                        }).appendTo($(this));
                        window.iceButtonTracker = null;
                    }
                    ice.upload($(this).attr("id"));
                    return false;
                }

                var formData = new FormData(this);
                if (window.iceButtonTracker) {
                    if (-1 == navigator.userAgent.indexOf("WebKit")) {
                        formData.append(window.iceButtonTracker.name,
                                window.iceButtonTracker.value);
                        window.iceButtonTracker = null;
                    }
                }

                $.ajax({
                    url:$(this).attr("action"),
                    data:formData,
                    cache:false,
                    contentType:false,
                    processData:false,
                    type:'POST',
                    success:function (html) {
                        updateRegion.replaceWith(html);
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