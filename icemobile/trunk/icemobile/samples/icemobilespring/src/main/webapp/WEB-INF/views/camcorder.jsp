<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | mvc-showcase</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
</c:if>
	<div id="vidContent">
		<h4>Camcorder</h4>
        <mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
			    ICEmobile Camcorder Sample
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
		<form:form id="camcorderform" method="POST" enctype="multipart/form-data" modelAttribute="camcorderBean" cssClass="cleanform">
            <h4>Video Info</h4>
            <mobi:fieldSetGroup inset="true">
                <mobi:fieldSetRow>
                    <form:label path="name">Author: <form:errors path="name" cssClass="error" /></form:label>
                    <form:input path="name" />
                </mobi:fieldSetRow>
                <mobi:fieldSetRow>
                    <mobi:camcorder id="camvid" />
                    <mobi:thumb for="camvid"
                                style="height:60px;width:65px;vertical-align:middle;float:right;margin:10px;"/>
                </mobi:fieldSetRow>
            </mobi:fieldSetGroup>
            <%-- button types: default|important|attention| back--%>
            <mobi:commandButton buttonType='important'
                                style="float:right;margin-right: 25px;"
                                value="Submit"
                                type="submit"/>
            <div style="clear:both;" />

            <c:if test="${not empty message}">
                <div id="message" class="success">${message}<br/>
                    <video src="media/video.mp4" controls="controls" />
                </div>
            </c:if>
            <s:bind path="*">
                <c:if test="${status.error}">
                    <div id="message" class="error">Form has errors</div>
                </c:if>
            </s:bind>
		</form:form>


		<script type="text/javascript">

			$(document).ready(function() {
				$("#camcorderform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#vidContent").replaceWith(unescape(data));
						    $('html, body').animate({ scrollTop: $("#message").offset().top }, 500);
                        }
                        ice.upload($(this).attr("id"));
                        return false;  
                    }

                    var formData = new FormData(this);

                    $.ajax({
                        url: $(this).attr("action"),
                        data: formData,
                        cache: false,
                        contentType: false,
                        processData: false,
                        type: 'POST',
                        success: function(html) {
						    $("#vidContent").replaceWith(html);
						    $('html, body').animate({ scrollTop: $("#message").offset().top }, 500);
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
