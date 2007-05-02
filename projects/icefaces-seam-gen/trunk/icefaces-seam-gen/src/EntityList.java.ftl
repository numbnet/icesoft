<#assign entityName = pojo.shortName>
<#assign componentName = util.lower(entityName)>
<#assign listName = componentName + "List">
${pojo.packageDeclaration}

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.RequestParameter;
import org.jboss.seam.framework.EntityQuery;
import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Arrays;

@Name("${listName}")
public class ${entityName}List extends EntityQuery
{
    private String[] selectedFields;	
    private static final String[] RESTRICTIONS = {
<#foreach property in pojo.allPropertiesIterator>
<#if !c2h.isCollection(property) && !c2h.isManyToOne(property)>
<#if c2j.isComponent(property)>
<#foreach componentProperty in property.value.propertyIterator>
<#if componentProperty.value.typeName == "string">
        "lower(${componentName}.${property.name}.${componentProperty.name}) like concat(lower(${'#'}{${listName}.${componentName}.${property.name}.${componentProperty.name}}),'%')",
</#if>
</#foreach>
<#else>
<#if property.value.typeName == "string">
        "lower(${componentName}.${property.name}) like concat(lower(${'#'}{${listName}.${componentName}.${property.name}}),'%')",
</#if>
</#if>
</#if>
</#foreach>
    };

/* list of string fields for search */
private static final SelectItem[] FIELDS = new SelectItem[]{
<#foreach property in pojo.allPropertiesIterator>
<#if !c2h.isCollection(property) && !c2h.isManyToOne(property)>
<#if c2j.isComponent(property)>
<#foreach componentProperty in property.value.propertyIterator>
<#if componentProperty.value.typeName == "string">
        new SelectItem("${property.name}.${componentProperty.name}"),
</#if>
</#foreach>
<#else>
<#if property.value.typeName == "string">
        new SelectItem("${property.name}"),
</#if>
</#if>
</#if>
</#foreach>
    };


<#if pojo.isComponent(pojo.identifierProperty)>
    private ${entityName} ${componentName};

    public ${entityName}List()
    {
        ${componentName} = new ${entityName}();
        ${componentName}.setId( new ${entityName}Id() );
    }
<#else>
    private ${entityName} ${componentName} = new ${entityName}();
</#if>

    @Override
    public String getEjbql() 
    { 
        return "select ${componentName} from ${entityName} ${componentName}";
    }
    
    @Override
    public Integer getMaxResults()
    {
    	return 25;
    }
    
    public ${entityName} get${entityName}()
    {
        return ${componentName};
    }
    
    @Override
    public List<String> getRestrictions()
    {
        return Arrays.asList(RESTRICTIONS);
    }
    public void setSelectedFields(String[] selectedFields) {
		this.selectedFields = selectedFields;
    }

    public String[] getSelectedFields() {
		return this.selectedFields;
    }

    public SelectItem[] getFieldsList() {
		return FIELDS;
    }

<#foreach property in pojo.allPropertiesIterator>
<#assign getter = pojo.getGetterSignature(property)>
<#if !c2h.isCollection(property) && !c2h.isManyToOne(property)>
<#if c2j.isComponent(property)>
<#foreach componentProperty in property.value.propertyIterator>
<#if componentProperty.value.typeName == "string">
   public boolean get${getter}Select {
	if (selectedFields !=null){
		for (int i=0; i<selectedFields.length; i++){
		   if (selectedFields[i].equalsIgnoreCase("${componentProperty.name}) return true;
	 	}
	}
	return false;
</#if>
</#foreach>
<#else>
<#if property.value.typeName == "string">
 public boolean ${getter}Select() {
	if (selectedFields !=null){
		for (int i=0; i<selectedFields.length; i++){
		   if (selectedFields[i].equalsIgnoreCase("${property.name}")) return true;
	 	}
	}
	return false;
}
</#if>
</#if>
</#if>
</#foreach>
    



}
