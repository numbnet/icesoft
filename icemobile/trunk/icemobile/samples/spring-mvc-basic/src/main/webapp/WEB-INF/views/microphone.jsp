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
	<div id="micContent">
        <style>
        .anchorbuttons a {
            text-decoration: none; font: menu;
            display: inline-block; padding: 2px 8px;
            border-style: solid; border-width: 2px;
            border-color: ButtonHighlight ButtonShadow ButtonShadow ButtonHighlight;
            border-radius: 10px;
        }
        .recordstyle {
            background: red;
        }
        </style>
		<h2>Microphone</h2>
		<p>
			ICEmobile Microphone Sample	
		</p>
		<form:form id="micform" method="POST" enctype="multipart/form-data" modelAttribute="microphoneBean" cssClass="cleanform">
			<div class="header">
		  		<h2>Form</h2>
		  		<c:if test="${not empty message}">
					<div id="message" class="success">${message}<br/>
                    <audio src="resources/clip.m4a" controls="controls" >
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
		  			Name <form:errors path="name" cssClass="error" />
		 		</form:label>
		  		<form:input path="name" />
		
		  	</fieldset>

		  	<fieldset class="anchorbuttons">
               <mobi:microphone />
               <audio style="float:right;height:120px;width:120px;" src="resources/clip.m4a" controls="controls" >
		  	</fieldset>

			<fieldset class="checkbox">
				<legend>Request Additional Info</legend>
				<label><form:checkbox path="additionalInfo[mvc]" value="true" />on Spring MVC</label>
				<label><form:checkbox path="additionalInfo[java]" value="true" />on Java (4-ever)</label>				
			</fieldset>
		  		  		
			<p><button type="submit">Submit</button></p>
		</form:form>


		<script type="text/javascript">

			$(document).ready(function() {
				$("#micform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#micContent").replaceWith(unescape(data));
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
						    $("#micContent").replaceWith(html);
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
