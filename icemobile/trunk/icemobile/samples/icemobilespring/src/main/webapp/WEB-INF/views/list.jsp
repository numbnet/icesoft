<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | list item demo</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
    <div class="ajaxzone">

      <form:form id="listform" method="POST" modelAttribute="listBean" style="margin-top:10px;" >

        <mobi:fieldSetGroup id="groupOne">
            <mobi:fieldSetRow style="padding-bottom:10px;">
                The list component shown without and with insets.
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <p/>


        <mobi:outputList id="firstList">
            <mobi:outputListItem>
                Approval and Reward
            </mobi:outputListItem>
            <mobi:outputListItem >
                Apathy and Scorn
            </mobi:outputListItem>
        </mobi:outputList>


        <mobi:outputList inset="true" id="secondList">
            <mobi:outputListItem group="true" >
                Thumbnail display
            </mobi:outputListItem>

            <mobi:outputListItem>

                    <h3>ICEsoft Ice Sailer</h3>
                      <p>Put him on the ice and watch him go!</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h3>ICEsoft Icebreaker</h3>
                    <p>Used icebreaker with very few dents.</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h3>ICEsoft Ice Skate</h3>
                    <p>A single sharpened ice skate, size 7.</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h3>ICEsoft Ice Car</h3>
                    <p>Beautiful ice car with metal car filling.</p>
            </mobi:outputListItem>
        </mobi:outputList>

         <h4>Here is an list with no inset but yes Grouping</h4>

        <mobi:outputList inset="false" id="thirdList" >
            <mobi:outputListItem group="true">
                Thumbnail display
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h3>ICEsoft Ice Sailer</h3>
                      <p>Put him on the ice and watch him go!</p>
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h3>ICEsoft Icebreaker</h3>
                    <p>Used icebreaker with very few dents.</p>
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h3>ICEsoft Ice Skate</h3>
                    <p>A single sharpened ice skate, size 7.</p>
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h3>ICEsoft Ice Car</h3>
                    <p>Beautiful ice car with metal car filling.</p>
            </mobi:outputListItem>
        </mobi:outputList>

        <h3> And finally an iterative list of items (Grouping on by default) </h3>

        <mobi:outputList  id="listIterator">
          <mobi:outputListItem group="true"> List of cars </mobi:outputListItem>
          <c:forEach items="${listBean.carCollection}" var="currCar" >
            <mobi:outputListItem>
                Car title "${currCar.title}"
            </mobi:outputListItem>
           </c:forEach>
        </mobi:outputList>

    <h:form>
    </div>

         </form:form >


    <script type="text/javascript">
        MvcUtil.enhanceForm("#listform");
    </script>
	</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>