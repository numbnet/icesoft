<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | mvc-showcase</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
	<div id="geolocationContent">

		<h2>ICEmobile - Geolocation</h2>
		<form:form id="geolocationform" method="POST"  modelAttribute="geolocationBean" cssClass="cleanform">
			<div class="header">
		  		<mobi:geolocation id="location" value="${geolocationBean.location}"/>
			</div>

		  		  		
			<p><input id="submitBtn" name="submitBtn" type="submit" value="Locate" /></p>

		</form:form>

Latitude = ${geolocationBean.lat}<br/>
Longitude = ${geolocationBean.lon}

		<script type="text/javascript">

			$(document).ready(function() {
				$("#geolocationform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#geolocationContent").replaceWith(unescape(data));
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
						    $("#geolocationContent").replaceWith(html);
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