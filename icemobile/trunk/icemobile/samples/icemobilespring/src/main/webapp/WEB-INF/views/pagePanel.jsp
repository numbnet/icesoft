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

    <mobi:pagePanel>
        <mobi:pagePanelHeader >
            PagePanel Custom Header
        </mobi:pagePanelHeader>

        <mobi:pagePanelBody>

        <mobi:outputList inset="true" id="firstList">
            <mobi:outputListItem>
                    <h4>ICEsoft Ice Sailer</h4>
                      <p>Put him on the ice and watch him go!</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h4>ICEsoft Icebreaker</h4>
                    <p>Used icebreaker with very few dents.</p>
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h4>ICEsoft Ice Skate</h4>
                    <p>A single sharpened ice skate, size 7.</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h4>ICEsoft Ice Car</h4>
                    <p>Beautiful ice car with metal car filling.</p>
            </mobi:outputListItem>
        </mobi:outputList>

        <h4>Iterative list</h4>

          <mobi:outputList  id="pageIterator">
            <mobi:outputListItem group="true"> List of cars </mobi:outputListItem>
            <c:forEach items="${pagePanelBean.carCollection}" var="currCar" >
              <mobi:outputListItem>
                 Car title "${currCar.title}", cost: "${currCar.cost}"
              </mobi:outputListItem>
            </c:forEach>
          </mobi:outputList>

        </mobi:pagePanelBody>

        <mobi:pagePanelFooter >
           PagePanel Custom Footer
        </mobi:pagePanelFooter>

    </mobi:pagePanel>
  </form:form>


    <script type="text/javascript">
        MvcUtil.enhanceForm("#pagepanelform");
    </script>
	</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>