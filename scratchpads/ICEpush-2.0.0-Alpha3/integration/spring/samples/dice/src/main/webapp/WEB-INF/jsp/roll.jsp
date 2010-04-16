<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>

<%@ include file="/WEB-INF/jsp/header.jsp" %>

<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Simple Dice Roller - ICEpush and Spring MVC</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>
       <center>
           <!-- The actual dice result, separated into a custom push region so it can easily be updated -->
           <icep:region group="${push.groupName}" page="/result.htm"/>

           <br/>

           <!-- Form to start the dice rolling process -->
           <form:form method="post" commandName="roll">
               <input type="submit" value="Roll the Dice"/>
           </form:form>
       </center>
    </body>
</html>
