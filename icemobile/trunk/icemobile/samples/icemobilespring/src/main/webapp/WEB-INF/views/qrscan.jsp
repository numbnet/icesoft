<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | qrcode scanner page</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
	<div id="qrscancontent">

		<h2>Various Spring MVC tags</h2>
		<p>
			QR Code Scanner
		</p>

		<form:form id="qrcodeform" method="POST"  modelAttribute="QRScanBean" cssClass="cleanform">
			<div class="header">
			    Press here to launch scanner  <mobi:qrscan id="scanOne" />
			</div>

			<button type="submit">Submit</button>
			  <p>Scanned: 
              <span style="font-style:italic">${QRScanBean.scanOne}</span>

		</form:form>



		<script type="text/javascript">

			$(document).ready(function() {
				$("#qrcodeform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#qrscancontent").replaceWith(unescape(data));
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
						    $("#qrscancontent").replaceWith(html);
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