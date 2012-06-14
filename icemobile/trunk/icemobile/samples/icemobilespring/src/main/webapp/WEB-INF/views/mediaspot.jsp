<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | MediaSpot</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
</c:if>
	<div id="mediaspot">
		<h2>MediaSpot</h2>
		<p>
			ICEmobile Augmented Reality	
		</p>
		<form:form id="mediaspotform" method="POST" enctype="multipart/form-data" modelAttribute="mediaspotBean" style="width: 290px" >
			<div class="header">
		  		<s:bind path="*">
		  			<c:if test="${status.error}">
				  		<div id="message" class="error">Form has errors</div>
		  			</c:if>
		  		</s:bind>
            <c:if test="${null != selection}">
                <fieldset style="text-align:center">
                    <div>Selected location:</div>
                    <img style="height:120px;width:120px;padding:5px;"
                        src="${imgPath}" >
                    <div style="font-style:italic">${selection}</div>
                </fieldset>
            </c:if>
			</div>
		  	<fieldset>
		  		<form:input path="title" placeholder="Title" />
		  	</fieldset>

		  	<fieldset >
               <mobi:geolocation id="location" />
               <mobi:camera id="camera" />
               <img style="height:60px;width:65px;vertical-align:middle;" 
                    id="camera-thumb" >
               <mobi:augmentedReality id="selection" 
                    style="float:right" params="${reality}"/>
		  	</fieldset>
			<button type="submit">Submit</button>
		</form:form>


		<script type="text/javascript">

            var ampchar = String.fromCharCode(38);

			$(document).ready(function() {
				$("#mediaspotform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#mediaspot").replaceWith(unescape(data));
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
						    $("#mediaspot").replaceWith(html);
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
