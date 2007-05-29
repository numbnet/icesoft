<!DOCTYPE composition PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
                             "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign entityName = pojo.shortName>
<#assign componentName = util.lower(entityName)>
<#assign homeName = componentName + "Home">
<#assign masterPageName = entityName + "List">
<#assign pageName = entityName>

<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:s="http://jboss.com/products/seam/taglib"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:f="http://java.sun.com/jsf/core"
                xmlns:h="http://java.sun.com/jsf/html"
		    xmlns:ice="http://www.icesoft.com/icefaces/component" 
                template="layout/template.xhtml">
                       
<ui:define name="body">
    
    <h:messages globalOnly="true" styleClass="message" id="globalMessages"/>
    
    <ice:form id="${componentName}editForm" styleClass="edit">
    
	  <ice:panelAccordion expanded="true" styleClass="accordion2">
		<f:facet name="header">
 			<ice:panelGrid id="accordionHeaderStyle" columns="1" columnClasses="leftMenu">
			    <ice:outputText value="Edit ${componentName}"/>
		      </ice:panelGrid>
	     </f:facet>        
           <ice:panelGroup styleClass="edit">
		 
                     <f:facet name="beforeInvalidField">
                                <ice:graphicImage styleClass="errorImg" value="img/error.png"/>
                        </f:facet>
                        <f:facet name="afterInvalidField">
                                    <s:message styleClass="errorMsg" />
                        </f:facet>
                        <f:facet name="aroundInvalidField">
                                <s:div styleClass="error"/>
                    </f:facet> 
			<s:validateAll>
			 <ice:panelGrid columns="2" rowClasses="prop" columnClasses="name,value">
<#foreach property in pojo.allPropertiesIterator>
<#include "editproperty.xhtml.ftl">
</#foreach>
                  <div style="clear:both">
                     <span class="required">*</span> 
                                    required fields
                  </div>
  		  
		</ice:panelGrid>
		</s:validateAll>
          </ice:panelGroup>
	  </ice:panelAccordion>
                  
        <div class="actionButtons">
        
            <h:commandButton id="save" 
                          value="Save" 
                         action="${'#'}{${homeName}.persist}"
                       disabled="${'#'}{!${homeName}.wired}"
                       rendered="${'#'}{!${homeName}.managed}"/>  
                          			  
            <h:commandButton id="update" 
                          value="Save" 
                         action="${'#'}{${homeName}.update}"
                       rendered="${'#'}{${homeName}.managed}"/>
                        			  
            <h:commandButton id="delete" 
                          value="Delete" 
                         action="${'#'}{${homeName}.remove}"
                       rendered="${'#'}{${homeName}.managed}"/>
                    
            <s:button id="done" 
                   value="Done"
             propagation="end"
                    view="/${pageName}.xhtml"
                rendered="${'#'}{${homeName}.managed}"/>
                
            <s:button id="cancel" 
                   value="Cancel"
             propagation="end"
                    view="/${'#'}{empty ${componentName}From ? '${masterPageName}' : ${componentName}From}.xhtml"
                rendered="${'#'}{!${homeName}.managed}"/>
                
        </div>
        
    </ice:form>
<#assign hasAssociations=false>
<#foreach property in pojo.allPropertiesIterator>
<#if c2h.isManyToOne(property) || c2h.isOneToManyCollection(property)>
<#assign hasAssociations=true>
</#if>
</#foreach>

<#if hasAssociations>
 <ice:form>  
        <ice:panelTabSet styleClass="componentPanelTabSetLayout" style="margin-bottom:5px;margin-top:10px;">
</#if>
<#foreach property in pojo.allPropertiesIterator>
<#if c2h.isManyToOne(property)>
<#assign parentPojo = c2j.getPOJOClass(cfg.getClassMapping(property.value.referencedEntityName))>
<#assign parentPageName = parentPojo.shortName>
<#assign parentName = util.lower(parentPojo.shortName)>
   
<#if property.optional>
	<ice:panelTab label="${property.name}">
<#else>
 	<ice:panelTab label="${property.name} *" labelClass="required">
</#if>
		<div class="association" id="${property.name}Parent">
    
        	<h:outputText value="No ${property.name}" 
                   rendered="${'#'}{${homeName}.instance.${property.name} == null}"/>
       	<ice:dataTable var="${parentName}" 
                   value="${'#'}{${homeName}.instance.${property.name}}" 
                rendered="${'#'}{${homeName}.instance.${property.name} != null}"
              rowClasses="rvgRowOne,rvgRowTwo"
		  columnClasses="allCols"
                      id="${property.name}Table">
<#foreach parentProperty in parentPojo.allPropertiesIterator>
<#if !c2h.isCollection(parentProperty) && !c2h.isManyToOne(parentProperty)>
<#if parentPojo.isComponent(parentProperty)>
<#foreach componentProperty in parentProperty.value.propertyIterator>
            <ice:column>
                <f:facet name="header">${componentProperty.name}</f:facet>
                ${'#'}{${parentName}.${parentProperty.name}.${componentProperty.name}}
            </ice:column>
</#foreach>
<#else>
            <ice:column>
                <f:facet name="header">${parentProperty.name}</f:facet>
                ${'#'}{${parentName}.${parentProperty.name}}
            </ice:column>
</#if>
</#if>
<#if c2h.isManyToOne(parentProperty)>
<#assign parentParentPojo = c2j.getPOJOClass(cfg.getClassMapping(parentProperty.value.referencedEntityName))>
<#if parentParentPojo.isComponent(parentParentPojo.identifierProperty)>
<#foreach componentProperty in parentParentPojo.identifierProperty.value.propertyIterator>
            <ice:column>
	    	    <f:facet name="header">${parentProperty.name} ${componentProperty.name}</f:facet>
		    	${'#'}{${parentName}.${parentProperty.name}.${parentParentPojo.identifierProperty.name}.${componentProperty.name}}
            </ice:column>
</#foreach>
<#else>
            <ice:column>
	    	    <f:facet name="header">${parentProperty.name} ${parentParentPojo.identifierProperty.name}</f:facet>
		    	${'#'}{${parentName}.${parentProperty.name}.${parentParentPojo.identifierProperty.name}}
            </ice:column>
</#if>
</#if>
</#foreach>
            <ice:column>
                <f:facet name="header">action</f:facet>
                <s:link view="/${parentPageName}.xhtml" 
                         id="view${parentName}" 
                      value="View" 
                propagation="none">
<#if parentPojo.isComponent(parentPojo.identifierProperty)>
<#foreach componentProperty in parentPojo.identifierProperty.value.propertyIterator>
                    <f:param name="${parentName}${util.upper(componentProperty.name)}" 
                            value="${'#'}{${parentName}.${parentPojo.identifierProperty.name}.${componentProperty.name}}"/>
</#foreach>
<#else>
                    <f:param name="${parentName}${util.upper(parentPojo.identifierProperty.name)}" 
                           value="${'#'}{${parentName}.${parentPojo.identifierProperty.name}}"/>
</#if>
                </s:link>
            </ice:column>
      </ice:dataTable>
<#if parentPojo.shortName!=pojo.shortName>
        <div class="actionButtons">
            <s:button value="Select ${property.name}"
                       view="/${parentPageName}List.xhtml">
                <f:param name="from" value="${pageName}Edit"/>
            </s:button>
        </div>
        
</#if>
    </div>
    </ice:panelTab>
</#if>
<#if c2h.isOneToManyCollection(property)>
 	<ice:panelTab label="${property.name}">
        <div class="association" id="${property.name}Children">
        
<#assign childPojo = c2j.getPOJOClass(property.value.element.associatedClass)>
<#assign childPageName = childPojo.shortName>
<#assign childEditPageName = childPojo.shortName + "Edit">
<#assign childName = util.lower(childPojo.shortName)>
            <h:outputText value="No ${property.name}" 
                       rendered="${'#'}{empty ${homeName}.${property.name}}"/>
           <ice:dataTable value="${'#'}{${homeName}.${property.name}}" 
                           var="${childName}" 
                      rendered="${'#'}{not empty ${homeName}.${property.name}}" 
                    rowClasses="rvgRowOne,rvgRowTwo"
			  columnClasses="allCols"
                            id="${property.name}Table">
<#foreach childProperty in childPojo.allPropertiesIterator>
<#if !c2h.isCollection(childProperty) && !c2h.isManyToOne(childProperty)>
<#if childPojo.isComponent(childProperty)>
<#foreach componentProperty in childProperty.value.propertyIterator>
               <ice:column>
                    <f:facet name="header">${componentProperty.name}</f:facet>
                    ${'#'}{${childName}.${childProperty.name}.${componentProperty.name}}
                </ice:column>
</#foreach>
<#else>
                <ice:column>
                    <f:facet name="header">${childProperty.name}</f:facet>
                    <h:outputText value="${'#'}{${childName}.${childProperty.name}}"/>
                </ice:column>
</#if>
</#if>
</#foreach>
                <ice:column>
                    <f:facet name="header">action</f:facet>
                    <s:link view="/${childPageName}.xhtml" 
                              id="select${childName}" 
                           value="Select"
                     propagation="none">
<#if childPojo.isComponent(childPojo.identifierProperty)>
<#foreach componentProperty in childPojo.identifierProperty.value.propertyIterator>
                        <f:param name="${childName}${util.upper(componentProperty.name)}" 
                                value="${'#'}{${childName}.${childPojo.identifierProperty.name}.${componentProperty.name}}"/>
</#foreach>
<#else>
                        <f:param name="${childName}${util.upper(childPojo.identifierProperty.name)}" 
                                value="${'#'}{${childName}.${childPojo.identifierProperty.name}}"/>
</#if>
                        <f:param name="${childName}From" value="${entityName}"/>
                    </s:link>
                </ice:column>
           </ice:dataTable>
      </div>
        <f:subview rendered="${'#'}{${homeName}.managed}" id="${property.name}">
        <div class="actionButtons">
            <s:button id="add${childName}" 
                   value="Add ${childName}"
                    view="/${childEditPageName}.xhtml"
             propagation="none">
                 <f:param name="${componentName}${util.upper(pojo.identifierProperty.name)}" 
                         value="${'#'}{${homeName}.instance.${pojo.identifierProperty.name}}"/>
                 <f:param name="${childName}From" value="${entityName}"/>
            </s:button>
        </div>
        </f:subview>
    </ice:panelTab>
</#if>
</#foreach>
<#if hasAssociations>
</ice:panelTabSet>
</ice:form>
</#if>
</ui:define>

</ui:composition>



