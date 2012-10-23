<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="listform" method="POST" modelAttribute="listBean">

    <mobi:fieldsetGroup id="groupOne">
        <mobi:fieldsetRow>
            A demonstration of the mobi:list, which allows content to be organized
            into styled lists.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <h4>List with inset and Grouping</h4>

    <mobi:outputList id="secondList">
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
 
</form:form >