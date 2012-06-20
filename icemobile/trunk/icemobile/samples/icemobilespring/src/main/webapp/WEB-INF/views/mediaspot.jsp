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
		<h4>MediaSpot</h4>
		<mobi:fieldSetGroup inset="true">
            <mobi:fieldSetRow>
                ICEmobile Augmented Reality
            </mobi:fieldSetRow>
		</mobi:fieldSetGroup>

        <h4>Photo Information</h4>
		<form:form id="mediaspotform" method="POST" enctype="multipart/form-data" modelAttribute="mediaspotBean" >
            <mobi:fieldSetGroup inset="true">
                <mobi:fieldSetRow>
                    <label>Title: </label>
		  		    <form:input path="title" placeholder="Title" />
                </mobi:fieldSetRow>
                <mobi:fieldSetRow style="height:90px;">
                   <mobi:geolocation id="location" />
                   <mobi:camera id="camera" />
                   <mobi:thumb for="camvid"
                                style="height:60px;width:65px;vertical-align:middle;float:right;margin:10px;"/>
                </mobi:fieldSetRow>
                <mobi:fieldSetRow>
                    <mobi:augmentedReality id="selection" params="${reality}"/>
                </mobi:fieldSetRow>
		  	</mobi:fieldSetGroup>

            <%-- button types: default|important|attention|back--%>
            <mobi:commandButton buttonType='important'
                                style="float:right;margin-right: 25px;"
                                value="Submit"
                                type="submit"/>
            <div style="clear:both;" />
            <s:bind path="*">
                <c:if test="${status.error}">
                    <div id="message" class="error">Form has errors</div>
                </c:if>
            </s:bind>
            <c:if test="${null != selection}">
                <mobi:fieldSetGroup inset="true">
                    <mobi:fieldSetRow>
                        Selected location:<br />
                        <img style="height:120px;width:120px;padding:5px;"
                             src="${imgPath}" />
                        <div style="font-style:italic">${selection}</div>
                    </mobi:fieldSetRow>
                </mobi:fieldSetGroup>
            </c:if>
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
