<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | List demo</title>
	<link href="<c:url value="/resources/style.css" />" rel="stylesheet"
              type="text/css"/>
    <script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
    <div class="ajaxzone">

      <form:form id="listform" method="POST" modelAttribute="listBean" style="margin-top:10px;" >

    <mobi:pagePanel>
        <mobi:pagePanelHeader >
            PagePanel Custom Header
        </mobi:pagePanelHeader>

        <mobi:pagePanelBody>

        <mobi:fieldSetGroup id="groupOne">
            <mobi:fieldSetRow style="padding-bottom:10px;">
A demonstration of two layout tags: mobi:pagePanel, which allows a fixed header
and footer to be defined, and mobi:list, which allows content to be organized
into styled lists.
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <p/>

         <h4>List with inset and Grouping</h4>

        <mobi:outputList inset="true" id="secondList">
            <mobi:outputListItem group="true" >
               List 
            </mobi:outputListItem>

            <mobi:outputListItem>
                    ICEsoft Ice Sailer
            </mobi:outputListItem>

            <mobi:outputListItem>
                    ICEsoft Icebreaker
            </mobi:outputListItem>

        </mobi:outputList>

         <h4>List with grouping and no inset</h4>

        <mobi:outputList inset="false" id="thirdList" >
            <mobi:outputListItem group="true">
               List 
            </mobi:outputListItem>
            <mobi:outputListItem>
                    ICEsoft Ice Sailer
            </mobi:outputListItem>
            <mobi:outputListItem>
                    ICEsoft Icebreaker
            </mobi:outputListItem>
        </mobi:outputList>

         <h4>List with no inset or grouping</h4>

        <mobi:outputList id="firstList">
            <mobi:outputListItem>
                    ICEsoft Ice Sailer
            </mobi:outputListItem>

            <mobi:outputListItem>
                    ICEsoft Icebreaker
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

        </mobi:pagePanelBody>

        <mobi:pagePanelFooter >
           PagePanel Custom Footer
        </mobi:pagePanelFooter>

    </mobi:pagePanel>

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
