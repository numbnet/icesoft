<rich:toolBar 
        xmlns="http://www.w3.org/1999/xhtml"
        xmlns:ui="http://java.sun.com/jsf/facelets"
        xmlns:h="http://java.sun.com/jsf/html"
        xmlns:f="http://java.sun.com/jsf/core"
        xmlns:s="http://jboss.com/products/seam/taglib"
        xmlns:rich="http://richfaces.ajax4jsf.org/rich">
    <rich:toolBarGroup>
        <h:outputText value="${'#'}{projectName}:"/>
	    <s:link view="/home.xhtml" value="Home"/>
	</rich:toolBarGroup>
<#foreach entity in c2j.getPOJOIterator(cfg.classMappings)>
	<s:link view="/${entity.shortName}List.xhtml" 
	       value="${entity.shortName} List" 
	 propagation="none"/>
</#foreach>         
     <rich:toolBarGroup location="right">
         <h:outputText value="Welcome, ${'#'}{identity.username}" rendered="${'#'}{identity.loggedIn}"/>
         <s:link view="/login.xhtml" value="Login" rendered="${'#'}{not identity.loggedIn}"/>
         <s:link view="/home.xhtml" action="${'#'}{identity.logout}" value="Logout" rendered="${'#'}{identity.loggedIn}"/>
     </rich:toolBarGroup>
</rich:toolBar>
