<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <title>ICEmobile | jQuery Microphone demo</title>
        <mobi:deviceResource/>
        <link href="<c:url value="/resources/style.css" />" rel="stylesheet"
              type="text/css"/>
        <link rel="stylesheet"
              href="./resources/jquery.mobile-1.1.1/jquery.mobile-1.1.1.min.css"/>
        <script src="./resources/jquery/1.7.1/jquery-1.7.1.min.js"></script>
        <script src="./resources/jquery.mobile-1.1.1/jquery.mobile-1.1.1.min.js"></script>
    </head>
    <body>
</c:if>
<div data-role="page" class="type-interior">
<div id="micContent">
    <style>
        .recordstyle {
            background: red;
        }
        .ui-body-c .mobi-button.mobi-button-important.ui-link{
            color: white;
        }
        .mobi-button{
            max-width:auto;
        }
    </style>
    <div data-role="header" data-theme="c">
        <h1>Microphone</h1>
        
    </div>
    <div data-role="content">
    
    <h3>
        jQuery ICEobile Microphone Demo
    </h3>

    <fieldset>
        <mobi:getEnhanced/>
    </fieldset>
    
    <form:form id="micform" method="POST" enctype="multipart/form-data"
               modelAttribute="microphoneBean">
        <div class="header">
            <c:if test="${not empty message}">
                <div id="message" class="success">${message}<br/>
                    <audio src="media/clip.mp4" controls="controls">
                </div>
            </c:if>
            <s:bind path="*">
                <c:if test="${status.error}">
                    <div id="message" class="error">Form has errors</div>
                </c:if>
            </s:bind>
        </div>
        <fieldset>
            <legend>Personal Info</legend>
            <form:label path="name">
                Name <form:errors path="name" cssClass="error"/>
            </form:label>
            <form:input path="name"/>

        </fieldset>

        <fieldset>
            <mobi:microphone id="mic" buttonLabel="Record Audio" />
        </fieldset>
        
        <c:if test="${micUploadReady}">
            <fieldset>
                <audio style="float:right;" src="${micUpload}"
                       controls="controls"></audio>
            </fieldset>
        </c:if>
        

        <fieldset class="checkbox">
            <legend>Request Additional Info</legend>
            <label><form:checkbox path="additionalInfo[mvc]" value="true"/>on
                Spring MVC</label>
            <label><form:checkbox path="additionalInfo[java]" value="true"/>on
                Java (4-ever)</label>
        </fieldset>

        <p>
            <button type="submit">Submit</button>
        </p>
    </form:form>


    <script type="text/javascript">

        $(document).ready(function () {
            $("#micform").submit(function () {
                if (window.ice && ice.upload) {
                    window.ice.handleResponse = function (data) {
                        $("#micContent").replaceWith(unescape(data));
                        $("#micContent").trigger("create");
                        var msgElem = $("#message");
                        if( msgElem.length > 0 ){
                            $('html, body').animate({ scrollTop:msgElem.offset().top }, 500);
                        }
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
                        $("#micContent").replaceWith(html);
                        $("#micContent").trigger("create");
                        var msgElem = $("#message");
                        if( msgElem.length > 0 ){
                            $('html, body').animate({ scrollTop:msgElem.offset().top }, 500);
                        }
                    }
                });

                return false;
            });
        });

    </script>
    </div>
</div>
</div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>
