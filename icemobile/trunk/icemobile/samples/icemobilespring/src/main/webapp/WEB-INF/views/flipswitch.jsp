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
		<form:form id="flipswitchform" method="POST"  modelAttribute="flipSwitchBean" cssClass="cleanform">

            <h4>Flip Switch</h4>
            <mobi:fieldSetGroup inset="true">
                <mobi:fieldSetRow>
                    <label>Auto-Capitalization</label>
                    <mobi:flipSwitch id="onOffFlipSwitch" labelOn="On" style="float:right"
                             labelOff="Off" value="${flipSwitchBean.onOffFlipSwitch}"/>
                </mobi:fieldSetRow>
                <mobi:fieldSetRow>
                    <label>Auto-Correction</label>
                    <mobi:flipSwitch id="yesNoFlipSwitch" labelOn="Yes" style="float:right"
                             labelOff="No" value="${flipSwitchBean.yesNoFlipSwitch}"/>
                </mobi:fieldSetRow>
                <mobi:fieldSetRow>
                    <label>Check Spelling</label>
                    <mobi:flipSwitch id="trueFalseFlipSwitch" labelOn="True" style="float:right"
                             labelOff="False" value="${flipSwitchBean.trueFalseFlipSwitch}"/>
                </mobi:fieldSetRow>
			</mobi:fieldSetGroup>
            <h4>Flip Switch Value Echo</h4>
            <mobi:fieldSetGroup inset="true">
                <mobi:fieldSetRow>
                    <label>Auto-Capitalization</label>
                    <label style="float:right">${flipSwitchBean.onOffFlipSwitch}</label>
                </mobi:fieldSetRow>
                <mobi:fieldSetRow>
                    <label>Auto-Correction</label>
                    <label style="float:right">${flipSwitchBean.yesNoFlipSwitch}</label>
                </mobi:fieldSetRow>
                <mobi:fieldSetRow>
                    <label>Check Spelling</label>
                    <label style="float:right">${flipSwitchBean.trueFalseFlipSwitch}</label>
                </mobi:fieldSetRow>
            </mobi:fieldSetGroup>
            <%-- button types: default|important|attention| back--%>
            <mobi:commandButton buttonType='important'
                                style="float:right;margin-right: 25px;"
                                value="Submit"
                                type="submit"/>
		</form:form>

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