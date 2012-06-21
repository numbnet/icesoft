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
	<div id="tabsetContent">

		<h2>Various Spring MVC tags</h2>
		<p>
			Tabset Example
		</p>
		<form:form id="tabsetform" method="POST"  modelAttribute="tabsetBean" cssClass="cleanform">

            <mobi:tabset id="testTab" selectedTab="1">
                <mobi:headers>
              	    <mobi:header>Tab #1 </mobi:header>
              	    <mobi:header>Tab #2 </mobi:header>
              	    <mobi:header>Tab #3 </mobi:header>
                </mobi:headers>
                <mobi:content>
                    <mobi:contentPane > This is the first content in the page  </mobi:contentPane>
                    <mobi:contentPane > This is the second content in the page </mobi:contentPane>
                    <mobi:contentPane > This is the third content in the page  </mobi:contentPane>
                </mobi:content>
            </mobi:tabset>

			<p><button type="submit">Submit</button></p>

		</form:form>


		<script type="text/javascript">

			$(document).ready(function() {
				$("#tabsetform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#tabsetContent").replaceWith(unescape(data));
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
						    $("#tabsetContent").replaceWith(html);
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