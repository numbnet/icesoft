<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<%@ page import="com.icesoft.icefaces.samples.jspStore.Store" %>
<jsp:useBean id="store" class="com.icesoft.icefaces.samples.jspStore.Store" scope="session"/>
<jsp:setProperty name="store" property="*"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    	<link href="store.css" rel="stylesheet" type="text/css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Store 1</title>
    </head>
    <body>
    <h1>JSP Store 1</h1>

	<% if (request.getParameter("add") != null) 
    store.purchase(request.getParameter("add")); %>
    
    <% if (request.getParameter("remove") != null) 
    store.remove(request.getParameter("remove")); %>
    
    <h2>Store</h2>
    <table>
     <tr>
     <th>Quantity</th>
     <th>Item</th>
     <th>Price</th>
     <th>Add</th>
     </tr>
    
    <c:forEach var="item" items="${store.items}">
        <tr>
        <td>${item.quantity}</td>
        <td>${item.label}</td>
        <td>${item.price}</td>
        <td><form action="main.jsp?add=${item.sku}" method="post"><input type="submit" value="Add" /></form></td>   
        </tr>
    </c:forEach>
    </table>
    
     <h2>Cart</h2>
     <table>
     <tr>
     <th>Quantity</th>
     <th>Item</th>
     <th>Subtotal</th>
     <th>Remove</th>
     </tr>
         <c:forEach var="item" items="${store.items}">
                     <tr>
        <td>${item.purchasedQuantity}</td>
        <td> ${item.label}</td>
        <td>${item.subTotal}</td>
        <td><form action="main.jsp?remove=${item.sku}" method="post"><input type="submit" value="Remove" /></form></td>
        </tr>
    </c:forEach>
     </table>

    </body>
</html>