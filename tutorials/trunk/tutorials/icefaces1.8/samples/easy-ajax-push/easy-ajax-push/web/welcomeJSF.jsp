<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="ice" uri="http://www.icesoft.com/icefaces/component"%>
<f:view>
    <ice:outputHtml>
        <ice:outputHead>
            <title>JSP Page</title>
            <ice:outputStyle href="./xmlhttp/css/xp/xp.css"/>
        </ice:outputHead>
        <ice:outputBody>        
            <ice:form>               
                <ice:outputText value="Welcome to ICEfaces" />                
            </ice:form>                
        </ice:outputBody>
    </ice:outputHtml>
</f:view> 