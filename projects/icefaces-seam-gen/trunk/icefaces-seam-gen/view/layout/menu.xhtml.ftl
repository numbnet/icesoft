<div class="menuButtons" 
		xmlns="http://www.w3.org/1999/xhtml"
		xmlns:ui="http://java.sun.com/jsf/facelets"
		xmlns:h="http://java.sun.com/jsf/html"
		xmlns:f="http://java.sun.com/jsf/core"
		xmlns:s="http://jboss.com/products/seam/taglib">
	<s:link view="/home.xhtml" value="Home"/>
<#foreach entity in c2j.getPOJOIterator(cfg.classMappings)>
	<s:link view="/${entity.shortName}List.xhtml" 
	       value="${entity.shortName} List" 
	 propagation="none"/>
</#foreach>         
</div>
