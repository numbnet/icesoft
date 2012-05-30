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
		<form:form id="mediaspotform" method="POST" enctype="multipart/form-data" modelAttribute="mediaspotBean" cssClass="cleanform">
			<div class="header">
		  		<h2>Form</h2>
		  		<s:bind path="*">
		  			<c:if test="${status.error}">
				  		<div id="message" class="error">Form has errors</div>
		  			</c:if>
		  		</s:bind>
		  	<fieldset>
                <c:if test="${null != selection}">
                    <div>You selected ${selection}</div>
                    <img style="float:right;height:120px;width:120px;" src="${imgPath}" >
                </c:if>
		  	</fieldset>
			</div>
		  	<fieldset>
		  		<legend>Personal Info</legend>
		  		<form:label path="title">
		  			Name <form:errors path="title" cssClass="error" />
		 		</form:label>
		  		<form:input path="title" />
		
		  	</fieldset>

		  	<fieldset >
               <mobi:camera />
               <img style="height:60px;width:65px;vertical-align:middle;" id="camera-thumb" >
		  	</fieldset>
            <input type="button" id="selection" style="float:right;" data-id="selection" data-command="aug" data-params="${reality}" value="Reality" onclick="mobilesx(this);" >
            <input type="hidden" id="location" name="location" >
			<p><button type="submit">Submit</button></p>
		</form:form>


		<script type="text/javascript">

            var ampchar = String.fromCharCode(38);

            function getPosition()  {
                navigator.geolocation.getCurrentPosition(
                    function(pos) {
                        var lat = pos.coords.latitude;
                        var lon = pos.coords.longitude;
                        $("#location").attr("value", lat + "," + lon);
                });
            }

            function mobilesx(element) {
                var formAction = formOf(element).getAttribute("action");
                var command = element.getAttribute("data-command");
                var params = element.getAttribute("data-params");
                var id = element.getAttribute("data-id");

                var barURL = window.location.toString();
                var baseURL = barURL.substring(0, 
                    barURL.lastIndexOf("/")) + "/";
                params = "ub=" + escape(baseURL) + ampchar + params;


                if (window.ice  && ice.aug)  {
                    if ("aug" == command)  {
                        ice.aug(id, params);
                    }
                    return;
                }

               var uploadURL;
                if (0 === formAction.indexOf("/"))  {
                    uploadURL = window.location.origin + formAction;
                } else if ((0 === formAction.indexOf("http://")) || 
                           (0 === formAction.indexOf("https://"))) {
                    uploadURL = formAction;
                } else {
                    uploadURL = barURL.substring(0, barURL.lastIndexOf("/")) +
                    "/" + formAction;
                }
                
                var returnURL = window.location;
                if ("" == returnURL.hash)  {
                    returnURL += "#icemobilesx";
                }
                
                var sxURL = "icemobile://c=" + escape(command + 
                        "?id=" + id + ampchar + params) +
                        ampchar + "u=" + escape(uploadURL) + 
                        ampchar + "r=" + escape(returnURL);
                
                window.location = sxURL;
            }
            
            function formOf(element)  {
                var parent = element;
                while (null != parent)  {
                    if ("form" == parent.nodeName.toLowerCase())  {
                        return parent;
                    }
                    parent = parent.parentNode;
                }
            }

			$(document).ready(function() {
                getPosition();
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
                            getPosition();
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
