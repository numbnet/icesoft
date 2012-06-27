<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | flipswitch demo</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
	<div id="accordionContent">

		<h2>Various Spring MVC tags</h2>
		<p>
			Accordion Example
		</p>
		<form:form id="accordionform" method="POST"  modelAttribute="accordionBean" cssClass="cleanform">

            <mobi:accordion id="ACCID" selectedId="tab1" >
                <mobi:content>
                    <mobi:contentPane botitle="First drop down item" id="tab1" >
                       <mobi:commandButton name="submitB" buttonType="important"
                                    value="Button 1"/>
                    </mobi:contentPane>
                    <mobi:contentPane botitle="Second drop down item" id="tab2">
                        <mobi:commandButton name="submitC" buttonType="important"
                                    value="Button 2"/>
                    </mobi:contentPane>
                    <mobi:contentPane botitle="Third drop down item" id="tab3">
                       <mobi:commandButton name="submitA" buttonType="important"
                                    value="Button 3"/>
                    </mobi:contentPane>
                </mobi:content>
            </mobi:accordion>

			<p><button type="submit">Submit</button></p>

		</form:form>

		<script type="text/javascript">
			$(document).ready(function() {
				$("#accordionform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#accordionContent").replaceWith(unescape(data));
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
						    $("#accordionContent").replaceWith(html);
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