<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | page panel layout demo</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
  <div id="pageContent">

   <form:form id="pagepanelform" method="POST" modelAttribute="pagePanelBean" style="margin-top:10px;" >

    <mobi:pagePanel id="mainPage">
        <mobi:pagePanelHeader id="mainPageHeader" >
            Header Placeholder
        </mobi:pagePanelHeader>

        <mobi:pagePanelBody id="panelBody">

          <mobi:outputList id="firstList">
           <mobi:outputListItem>
             Approval and Reward
           </mobi:outputListItem>
           <mobi:outputListItem >
             Apathy and Scorn
           </mobi:outputListItem>
          </mobi:outputList>

        And finally an iterative list of items

          <mobi:outputList  id="pageIterator">
            <mobi:outputListItem group="true"> List of cars </mobi:outputListItem>
            <c:forEach items="${pagePanelBean.carCollection}" var="currCar" >
              <mobi:outputListItem>
                 Car title "${currCar.title}", cost: "${currCar.cost}"
              </mobi:outputListItem>
            </c:forEach>
          </mobi:outputList>

        </mobi:pagePanelBody>

        <mobi:pagePanelFooter id="mainFooter" >
           Footer Placeholder
        </mobi:pagePanelFooter>

    </mobi:pagePanel>
  </form:form>


		<script type="text/javascript">
			$(document).ready(function() {
				$("#pagepanelform").submit(function() {
                    if (window.ice && ice.upload)  {
                        window.ice.handleResponse = function(data)  {
						    $("#pageContent").replaceWith(unescape(data));
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
						    $("#pageContent").replaceWith(html);
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