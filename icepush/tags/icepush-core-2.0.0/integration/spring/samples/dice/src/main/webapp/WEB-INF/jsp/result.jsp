<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>

<%@ include file="/WEB-INF/jsp/header.jsp" %>

<!-- Display a matching image for the current dice value -->
<img src="./images/dice-${dice.value}.png" alt="Dice Roll" width="200px" height="200px"/>

<br/>

<!-- Display a message if the dice is done rolling -->
<c:if test="${dice.doneRolling}">
    <c:out value="After much tumbling the dice has rolled a ${dice.value}."/>
</c:if>