<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="ice" uri="http://www.icesoft.com/icefaces/component"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Successfull Registration</title>
        <link href="./xmlhttp/css/xp/xp.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <f:view>
            <ice:form>
                <p>
                    Welcome <b><ice:outputText value="#{user.firstName}"/></b>
                    you have successfully registered with the following
                    information:                
                </p>
                <p>Name: <b><ice:outputText value="#{user.firstName} #{user.lastName}"/></b></p>
                <p>Address: <b><ice:outputText value="#{user.address}"/></b></p>
                <p>City: <b><ice:outputText value="#{user.city}"/></b></p>
                <p>Phone: <b><ice:outputText value="#{user.phone}">
                                <f:converter converterId="icefaces.PhoneConverter"/>
                             </ice:outputText></b>
                </p>
                <p>Age: <b><ice:outputText value="#{user.age}"/></b></p>
                <p>Birth Date: <b><ice:outputText value="#{user.birthDate}">
                                    <f:convertDateTime dateStyle="short"/>
                                  </ice:outputText></b>
                </p>
                <p>Salary: <b><ice:outputText value="#{user.salary}">
                                <f:convertNumber maxFractionDigits="2"
                                             groupingUsed="true"
                                             currencySymbol="$"
                                             maxIntegerDigits="7"
                                             type="currency"/>
                              </ice:outputText></b>
                </p>
            </ice:form>    
        </f:view>
       
    </body>
</html>
