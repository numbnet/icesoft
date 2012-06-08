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
	<div id="flipswitchContent">

		<h2>Various Spring MVC tags</h2>
		<p>
			Flipswitches
		</p>
		<form:form id="flipswitchform" method="POST"  modelAttribute="flipSwitchBean" cssClass="cleanform">
			<div class="header">
		  		<mobi:flipSwitch id="onOffFlipSwitch" labelOn="On" labelOff="Off" value="${flipSwitchBean.onOffFlipSwitch}"/>
		  		<mobi:flipSwitch id="yesNoFlipSwitch" labelOn="Yes" labelOff="No" value="${flipSwitchBean.yesNoFlipSwitch}"/>
		  		<mobi:flipSwitch id="trueFalseFlipSwitch" labelOn="True" labelOff="False" value="${flipSwitchBean.trueFalseFlipSwitch}"/>
			</div>

		  		  		
			<p><button type="submit">Submit</button></p>

		</form:form>

On/Off Bean = ${flipSwitchBean.onOffFlipSwitch} <br/>
Yes/No Bean = ${flipSwitchBean.yesNoFlipSwitch} <br/>
True/False Bean = ${flipSwitchBean.trueFalseFlipSwitch} 


		<script type="text/javascript">

			$(document).ready(function() {
				$("#flipswitchform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#flipswitchContent").replaceWith(unescape(data));
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
						    $("#flipswitchContent").replaceWith(html);
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