<%@ page contentType="text/html" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://www.icesoft.com/icefaces/component" prefix="ice"%>

<html>
<head>
<title>Sell Item - Enter Price and Item Count</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style.css" type="text/css">
</head>
<body>

<div id="logo">
	<img src="images/spring-logo.jpg" alt="Logo" border="0"> 
</div>

<f:view>
<div id="content">
	<div id="insert"><img src="images/webflow-logo.jpg" /></div>
	<h2>Enter price and item count</h2>
	<hr>
	<table>
        <ice:form id="itemForm">
			<tr>
				<td>Price:</td>
				<td>

                    <ice:inputText id="price" value="#{sale.price}" tabindex="1"
                                   required="true" >
					</ice:inputText>
				</td>
				<td>
					<h:message for="price" errorClass="error"/>
				</td>
			</tr>
            <tr>
				<td>Item count:</td>
				<td>
                     <%--<ice:selectInputText id="searchString"--%>
                                          <%--valueChangeListener="#{sale.handleItemCountChange}"--%>
                                          <%--value="#{sale.itemCount}"--%>
                                          <!--partialSubmit="true" >-->
                    <ice:inputText id="itemCount"
                                   value="#{sale.itemCount}"
                                   required="true"
                                   partialSubmit="true"
                                   tabindex="2">
                        
					</ice:inputText>
				</td>
				<td>
					<h:message for="itemCount" errorClass="error"/>
				</td>
			</tr>
            <tr>
                <td>Is shipping required?:</td>
                <td>
                    <ice:selectBooleanCheckbox value="#{sale.shipping}" tabindex="3" />
                </td>
            </tr>
            <tr>
                <td>Discount Category:</td>
                <td>
                    <ice:outputText style="color: gray;" value="#{sale.realtimeCategory}"/>
                </td>
            </tr>
            
                <tr>
				<td colspan="2" class="buttonBar">
					<ice:commandButton type="submit" value="Next" action="submit" immediate="false"
                                tabindex="4"/>
				</td>
				<td></td>
            </tr>
        </ice:form>
    </table>
</div>
    <div class="icesoft">

        A trivial ICEfaces extension to this application added Partial Submit capability to the
        'Item Count' input text field. On loss of focus, the field is submitted to the server, and
        manifests in an accurate 'Discount Category' description for the number of items to ship.
        The point is that the logic remains on the server and the User sees increased interactivity
        and functionality, all while remaining on the same page in the same Webflow state, without
        requiring modification to the Webflow definitions. Partial Submit does not necessarily
        change application Webflow state (although it certainly can).

    </div>
</f:view>

<div id="copyright">
	<p>&copy; Copyright 2004-2007, <a href="http://www.springframework.org">www.springframework.org</a>, under the terms of the Apache 2.0 software license.</p>
</div>
</body>
</html>