package org.icefaces.generator.xmlbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import org.icefaces.component.annotation.Component;
import org.icefaces.generator.context.ComponentContext;
import org.icefaces.generator.context.GeneratorContext;
import org.icefaces.generator.utils.PropertyValues;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class EclipseAttributeMDBuilder extends XMLBuilder{
    
	private Element tag;
    private Properties  type2TypeMap = new Properties();
    private Properties name2TypeMap = new Properties();
    private Properties qeMap = new Properties();
    
    private static String PACKAGE="org/icefaces/generator/xmlbuilder/";
    
    public EclipseAttributeMDBuilder() {
        super("icefaces_ace.xml");
        
		try {
		   InputStream inType2Type = new FileInputStream("c:\\EclipseAttributeMDBuilder.properties");
           InputStream inName2Type = new FileInputStream("c:\\EclipseAttributeNameMap.properties");
	       InputStream inQE = new FileInputStream("c:\\EclipseAttributeQEMap.properties");

			type2TypeMap.load(inType2Type);
			name2TypeMap.load(inName2Type);
			qeMap.load(inQE);
		} catch (IOException e) {
			e.printStackTrace();
		}
        Element root = getDocument().createElement("md:metadatamodel");
        root.setAttribute("xmlns:md", "http://org.eclipse.jst.jsf.common.metadata/metadata.ecore");
        root.setAttribute("xmlns:cnst", "http://org.eclipse.jst.jsf.core/constraints.ecore" );
        root.setAttribute("xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
        root.setAttribute("xmlns:mdt", "http://org.eclipse.jst.jsf.common.metadata/metadataTraitTypes.ecore" );
        root.setAttribute("xmlns:qe", "http://org.eclipse.jsf.pagedesigner/QuickEditTabSections.ecore");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("id","http://www.icefaces.org/icefaces/components");
        root.setAttribute("type","tagFile");
        getDocument().appendChild(root);
    }
    
    

    public void addTagInfo(Component component) {
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("entity");
        tag.setAttribute("id", component.tagName());
        tag.setAttribute("type", "tag");
        root.appendChild(tag); 
        addQEInfo(component.tagName());
    }
    
    private void addQEInfo (String compName) {
        Element trait = getDocument().createElement("trait");
        trait.setAttribute("id", "quick-edit-tab");
        Element value = getDocument().createElement("value");
        trait.appendChild(value);
        value.setAttribute("xsi:type", "qe:QuickEditTabSections");
        
        String listAttr = qeMap.getProperty(compName);
        if (listAttr != null) {
        String[] attr = listAttr.split("[,]");
        for (int i = 0; i < attr.length; i++) {
        	String name = attr[i].trim();
        	Element section = getDocument().createElement("section");
            section.setAttribute("id", name);
            value.appendChild(section);
		}
        tag.appendChild(trait);
        }
        
       
    }
    
    public void addAttributeInfo(Field field) {
		ComponentContext component = GeneratorContext.getInstance().getActiveComponentContext();
		PropertyValues propertyValues = component.getPropertyValuesMap().get(field);
		
        Element attribute = getDocument().createElement("entity");
        attribute.setAttribute("id", field.getName());
        tag.appendChild(attribute);
        
        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey( field.getName().toString().trim() );
        String returnAndArgumentType = field.getType().getName();
        if (isPrimitive) {
            if (GeneratorContext.WrapperTypes.containsKey( field.getType().getName() )) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get( field.getType().getName() );
            }
        }
        Element specialCase = handleSpecialCase (attribute, field.getName());
        if (specialCase == null) {
        String type = name2TypeMap.getProperty(field.getName());
        if (type == null) {
        	type = type2TypeMap.getProperty(returnAndArgumentType);
        }      
        if (type!=null) {
        Element attTrait = getDocument().createElement("trait");
        attTrait.setAttribute("id", "attribute-value-runtime-type");
        Element value = getDocument().createElement("value");
        value.setAttribute("xsi:type", "mdt:StringValue");
        attTrait.appendChild(value);
        Text typeText = getDocument().createTextNode(type);
        value.appendChild(typeText);
        attribute.appendChild(attTrait);
        }
        }
        tag.appendChild(attribute);
    }



	private Element handleSpecialCase(Element attribute, String name) {
		Element trait = null;
		if (name.equals("converter")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.FacesConfigConverterIDType");
	        attribute.appendChild(trait);
			trait = addTraitElement("config-type", "javax.faces.convert.Converter");
	        attribute.appendChild(trait);
		} else if (name.equals("valueChangeListener")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.MethodBindingType");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-return-type", "void");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-param-types", "javax.faces.event.ValueChangeEvent");
	        attribute.appendChild(trait);	        
		} else if (name.equals("validator")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.MethodBindingType");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-return-type", "void");
	        attribute.appendChild(trait);
	        trait = addTraitElementValues("runtime-param-types", 
	        		new String[] {"javax.faces.context.FacesContext",
	        		"javax.faces.component.UIComponent",
	        		"java.lang.Object"});
	        attribute.appendChild(trait);	        
		} else if (name.equals("actionListener")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.MethodBindingType");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-return-type", "void");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-param-types","javax.faces.event.ActionEvent");
	        attribute.appendChild(trait);	        
		} else if (name.equals("action")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.ActionType");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-return-type", "java.lang.String");
	        attribute.appendChild(trait);	        
		} else if (name.endsWith("Path")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.WebPathType");
	        attribute.appendChild(trait);		
		} else if (name.endsWith("Listener")) {
			trait = addTraitElement("attribute-value-runtime-type", "org.eclipse.jst.jsf.core.attributevalues.MethodBindingType");
	        attribute.appendChild(trait);
	        trait = addTraitElement("runtime-return-type", "void");
	        attribute.appendChild(trait);			
		}
		return trait;
	}
	
	private Element addTraitElementValues(String id, String[] values) {
		Element attTrait = getDocument().createElement("trait");
        attTrait.setAttribute("id", id);
        Element value = getDocument().createElement("value");
        value.setAttribute("xsi:type", "mdt:ListOfValues");
        attTrait.appendChild(value);
        for (int i = 0; i < values.length; i++) {
			Element item = getDocument().createElement("item");
			Text typeText = getDocument().createTextNode(values[i]);
			item.appendChild(typeText);
	        value.appendChild(item);
		}
     return attTrait;
	}



	private Element addTraitElement (String id, String valueType) {
		 Element attTrait = getDocument().createElement("trait");
	        attTrait.setAttribute("id", id);
	        Element value = getDocument().createElement("value");
	        value.setAttribute("xsi:type", "mdt:StringValue");
	        attTrait.appendChild(value);
	        Text typeText = getDocument().createTextNode(valueType);
	        value.appendChild(typeText);
	     return attTrait;
	}
}
