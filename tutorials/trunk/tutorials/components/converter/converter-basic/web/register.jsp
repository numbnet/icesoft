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
        <title>ICEfaces Converter Example - Basic</title>
        <link href="./xmlhttp/css/xp/xp.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <f:view>
            <ice:form id="RegisterForm">
                <h1>ICEfaces Converter Example - Basic</h1>
                <p>The following example uses standard JSF converters to convert
                   the input data to the appropriate data types.</p>
                <table border="0">
                    <tr/>
                    <td>First Name:</td>
                    <td>
                        <ice:inputText id="firstname" value="#{user.firstName}"/>
                    </td>
                    <tr/>
                    <td>Last Name:</td>
                    <td>
                        <ice:inputText id="lastname" value="#{user.lastName}"/>
                    </td>
                    <tr/>
                    <td>Address:</td>
                    <td>
                        <ice:inputText id="address" value="#{user.address}"/>
                    </td>
                    <tr/>
                    <td>City:</td>
                    <td>
                        <ice:inputText id="city" value="#{user.city}"/>
                    </td>
                    <tr/>
                    <td>Age:</td>
                    <td>
                        <ice:inputText id="age" value="#{user.age}">
                            <f:converter converterId="javax.faces.Integer"/>
                        </ice:inputText>
                    </td>
                    <tr/>
                    <td>Birth Date:</td>
                    <td>
                        <ice:inputText id="birthdate" value="#{user.birthDate}">
                            <f:convertDateTime dateStyle="short"/>
                        </ice:inputText>
                    </td>
                    <tr/>
                    <td>Salary:</td>
                    <td>
                        <ice:inputText id="salary" value="#{user.salary}">
                            <f:convertNumber maxFractionDigits="2"
                                            groupingUsed="true"
                                            currencySymbol="$"
                                            maxIntegerDigits="7"
                                            type="currency"/>
                        </ice:inputText>
                    </td>
                </table>
                <ice:messages/>
                <ice:commandButton id="registercommand" type="submit" value="Register" action="#{registerBean.register}"/>
            </ice:form>
            
        </f:view>
        
    </body>
</html>
