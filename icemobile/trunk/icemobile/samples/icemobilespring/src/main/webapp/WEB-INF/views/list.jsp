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
                The list component with and without insets.
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <p/>

         <h4>List with inset and Grouping</h4>

        <mobi:outputList inset="true" id="secondList">
            <mobi:outputListItem group="true" >
                Thumbnail display
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h4>ICEsoft Ice Sailer</h4>
                      <p>Put him on the ice and watch him go!</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h4>ICEsoft Icebreaker</h4>
                    <p>Used icebreaker with very few dents.</p>
            </mobi:outputListItem>

        </mobi:outputList>

         <h4>List with grouping and no inset</h4>

        <mobi:outputList inset="false" id="thirdList" >
            <mobi:outputListItem group="true">
                Thumbnail display
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h4>ICEsoft Ice Sailer</h4>
                      <p>Put him on the ice and watch him go!</p>
            </mobi:outputListItem>
            <mobi:outputListItem>
                    <h4>ICEsoft Icebreaker</h4>
                    <p>Used icebreaker with very few dents.</p>
            </mobi:outputListItem>
        </mobi:outputList>

         <h4>List with no inset or grouping</h4>

        <mobi:outputList id="firstList">
            <mobi:outputListItem>
                    <h4>ICEsoft Ice Sailer</h4>
                      <p>Put him on the ice and watch him go!</p>
            </mobi:outputListItem>

            <mobi:outputListItem>
                    <h4>ICEsoft Icebreaker</h4>
                    <p>Used icebreaker with very few dents.</p>
            </mobi:outputListItem>
        </mobi:outputList>

        <h4>Iterative list (Grouping on by default) </h4>

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