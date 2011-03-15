<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="ice" uri="http://www.icesoft.com/icefaces/component"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ICEfaces Auto Complete Tutorial</title>
        <link href="./xmlhttp/css/xp/xp.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <ice:form>
            <ice:panelGrid columns="1">
                <h1>ICEfaces Auto Complete Tutorial</h1>
                <ice:panelGroup>
                    <ice:outputText value="Enter some text to start the Auto Complete component:"/><br/><br/>
                    <ice:selectInputText rows="10" width="300" valueChangeListener="#{autoCompleteBean.updateList}">
                        <f:selectItems value="#{autoCompleteBean.list}"/>
                    </ice:selectInputText>
                </ice:panelGroup>
                <br/>
                <ice:panelGroup>
                    <ice:outputText value="Auto Complete Output:" style="font-weight:bold;"/>
                    <ice:panelGrid columns="2">
                        <ice:outputText value="City:"/>
                        <ice:outputText id="city"
                                        value="#{autoCompleteBean.currentCity.city}"/>
                        <ice:outputText value="State:"/>
                        <ice:outputText id="state"
                                        value="#{autoCompleteBean.currentCity.state} #{autoCompleteBean.currentCity.stateCode}"/>
                        <ice:outputText value="County:"/>
                        <ice:outputText id="county"
                                        value="#{autoCompleteBean.currentCity.country}"/>
                        <ice:outputText value="Zip:"/>
                        <ice:outputText id="zip"
                                        value="#{autoCompleteBean.currentCity.zip}"/>
                        <ice:outputText value="Area Code:"/>
                        <ice:outputText id="areaCode"
                                        value="#{autoCompleteBean.currentCity.areaCode}"/>
                    </ice:panelGrid>
                </ice:panelGroup>
            </ice:panelGrid>
        </ice:form>

    </body>
</html>
