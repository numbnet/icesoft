<%@ page contentType="text/html" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://www.icesoft.com/icefaces/component" prefix="ice"%>

<html>
<head>
<title>Sell Item - Shipping Summary</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style.css" type="text/css">
</head>
<body id="body">

<div id="logo">
	<img src="images/spring-logo.jpg" alt="Logo" border="0"> 
</div>

<f:view>
<div id="content">
	<div id="insert"><img src="images/webflow-logo.jpg"/></div>
	<h2>Purchase cost overview</h2>
	<hr>
	<table>
	<tr>
		<td>Item Price:</td><td><h:outputText value="#{sale.price}"/></td>
	</tr>
	<tr>
		<td>Item count:</td><td><h:outputText value="#{sale.itemCount}"/></td>
	</tr>
	<tr>
		<td>Discount Category:</td><td><h:outputText value="#{sale.realtimeCategory}"/></td>
        </tr>
	<tr>
		<td>Shipping details:</td>
        <td>
            <ice:panelGroup rendered="#{sale.shipping}" >
                <ice:outputText value="#{sale.shippingDescription}" />
            </ice:panelGroup>
             <ice:panelGroup rendered="#{!sale.shipping}" >
                 No shipping required: you're picking up the items
            </ice:panelGroup>

        </td>
	</tr>
	<tr>
		<td colspan="2"></td>
	</tr>
	<tr>
		<td>Base amount:</td><td><ice:outputText value="#{sale.amount}">
         <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
        </ice:outputText>
        </td>
	</tr>
	<tr>
		<td>Delivery Subtotal:</td><td><ice:outputText value="#{sale.deliverySubtotal}">
                 <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
        </ice:outputText>
        </td>
	</tr>
	<tr>
		<td>Discount:</td><td><ice:outputText value="#{sale.savings}">
                 <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
        </ice:outputText>
        (Discount rate: <h:outputText value="#{sale.discountRate}"/>)</td>
	</tr>
	<tr>
		<td colspan="2"><hr></td>
	</tr>
	<tr>
		<td><b>Total cost</b>:</td><td><ice:outputText value="#{sale.totalCost}">
                 <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
        </ice:outputText>
        </td>
	</tr>
	<tr>
        <td colspan="2" class="buttonBar">
            <ice:form id="startOverForm" >
            <ice:commandButton id="startButton"
                               value="another shipment"
                               action="#{navBean.whereToStart}"
                    rendered="true" />
            </ice:form>
		</td>

    </tr>
    </table>
</div>
</f:view>

<div id="copyright">
	<p>&copy; Copyright 2004-2007, <a href="http://www.springframework.org">www.springframework.org</a>, under the terms of the Apache 2.0 software license.</p>
</div>
</body>
</html>
