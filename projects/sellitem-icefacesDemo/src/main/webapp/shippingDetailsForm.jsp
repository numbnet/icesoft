<%@ page contentType="text/html" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://www.icesoft.com/icefaces/component" prefix="ice"%>

<html>
<head>
<title>Sell Item - Enter Shipping Information</title>
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
	<h2>Enter shipping information</h2>
	<hr>
	<table>
	<tr>
		<td>Price:</td><td><ice:outputText value="#{sale.price}"/></td>
	</tr>
	<tr>
		<td>Item count:</td><td><ice:outputText value="#{sale.itemCount}"/></td>
	</tr>
	<tr >
		<td>Discount:</td><td><ice:outputText value="#{sale.realtimeCategory}"/></td>
        </tr>
	<tr >
		<td>Shipping:</td><td><ice:outputText value="#{sale.shipping}"/></td>
	</tr>

    <h:form id="shippingForm">


        <tr>
            <td>Shipping Source:</td>
            <td>
                <ice:selectOneMenu id="SourceCity"
                                   styleClass="selectTagMenu"
                                   value="#{sale.sourceCity}"
                                   partialSubmit="true">
                    <f:selectItems id="SlctcompTypeItms"
                                   value="#{sale.sourceCities}"/>
                </ice:selectOneMenu>
            </td>

            <td>Shipping Destination:</td>
            <td>
                <ice:selectOneMenu id="DestinationCity"
                                   styleClass="selectTagMenu"
                                   value="#{sale.destinationCity}"
                                   partialSubmit="true">
                    <f:selectItems id="listofDestinations"
                                   value="#{sale.destinationCities}"/>
                </ice:selectOneMenu>
            </td>
        </tr>

        <%--<tr class="readOnly">--%>
            <%--<td>Shipping Cost Per Item</td>--%>
            <%--<td>--%>
                <%--<ice:outputText id="costPerItem" value="#{sale.costPerItem}" />--%>
            <%--</td>--%>
        <%--</tr>--%>

        <tr>
            <td>Shipping type:</td>
            <td>
               <ice:selectOneMenu id="shippingType"
                                   styleClass="selectTagMenu"
                                   value="#{sale.shippingType}"
                                   partialSubmit="true">
                    <f:selectItems id="listofShippingOptions"
                                   value="#{sale.shippingOptions}"/>
                </ice:selectOneMenu>
			</td>
		</tr>
        <tr>
            <td colspan="3"><hr></td>
        </tr>
      <tr>
        <td colspan="2"><b>Shipping Summary</b></td>
    </tr>
        <tr>
            <td>Item amount:</td><td><ice:outputText value="#{sale.amount}">
            <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
            </ice:outputText>
            </td>
        </tr>
         <tr>
            <td>Shipping cost (per item):</td><td><ice:outputText value="#{sale.deliveryRate}">
            <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
            </ice:outputText>
            </td>
        </tr>
        <tr>
            <td>Delivery subtotal:</td><td>
            <ice:outputText value="#{sale.deliverySubtotal}">
                <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
            </ice:outputText>
        </td>
        </tr>
        <tr>
            <td>Discount:</td><td>
            <ice:outputText value="#{sale.savings}"> 
             <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency"/>
            </ice:outputText>
            (Discount rate: <ice:outputText value="#{sale.discountRate}"/>)</td>
        </tr>
        <tr>
            <td colspan="3"><hr></td>
        </tr>
        <tr>
            <td><b>Total cost</b>:</td><td>
            <ice:outputText value="#{sale.totalCost}">
             <f:convertNumber maxFractionDigits="2"
                                 groupingUsed="true"
                                 currencySymbol="$"
                                 maxIntegerDigits="7"
                                 type="currency" />
            </ice:outputText>            

        </td>
        </tr>
        <tr>
            <td colspan="2"><hr></td>
        </tr>
        <tr>
            <td colspan="2" class="buttonBar">
				<h:commandButton type="submit" value="Confirm Shipment" action="submit" immediate="false" />
			</td>
		</tr>
		</h:form>
	</table>

</div>

<div class="icesoft">

    Get access to server side logic, all while remaining in the same Application state. This page
    demonstrates Partial Submit on several shipping parameter fields, allowing the user to quickly
    tailor shipping details without time consuming page transitions, and without having javascript
    business logic exposed at the client. When the details are correct,
    the User can complete the shipping transaction and complete the Webflow.

</div>
</f:view>

<div id="copyright">
	<p>&copy; Copyright 2004-2007, <a href="http://www.springframework.org">www.springframework.org</a>, under the terms of the Apache 2.0 software license.</p>
</div>
</body>
</html>
