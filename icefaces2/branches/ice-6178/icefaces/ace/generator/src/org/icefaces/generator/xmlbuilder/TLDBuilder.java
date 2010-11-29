package org.icefaces.generator.xmlbuilder;

import java.lang.reflect.Field;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.generator.Generator;
import org.icefaces.generator.context.GeneratorContext;
import org.icefaces.generator.context.ComponentContext;

import org.icefaces.generator.utils.PropertyValues;

public class TLDBuilder extends XMLBuilder{
    private Element tag;
    
    public TLDBuilder() {
        super("components.tld");
        Element root = getDocument().createElement("taglib");
        root.setAttribute("xmlns",              "http://java.sun.com/xml/ns/javaee");
        root.setAttribute("xmlns:xsi",          "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
        root.setAttribute("version",            "2.1");
        getDocument().appendChild(root);
        addNode(root, "display-name", GeneratorContext.getDisplayName());
        addNode(root, "tlib-version", GeneratorContext.getVersion());
        //addNode(root, "jsp-version", "1.2");
        addNode(root, "short-name", GeneratorContext.shortName);
        addNode(root, "uri", GeneratorContext.namespace);
    }

    public void addTagInfo(Class clazz, Component component) {
        String tagName = component.componentClass().substring((component.componentClass().lastIndexOf('.')+1));
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        Element description = getDocument().createElement("description");
        CDATASection descriptionCDATA = getDocument().createCDATASection( component.tlddoc());
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
        addNode(tag, "name", component.tagName());
        addNode(tag, "tag-class", component.componentClass()+"Tag");
        addNode(tag, "body-content", "JSP");
//
    }
    
    public void addAttributeInfo(Field field) {
		ComponentContext component = GeneratorContext.getInstance().getActiveComponentContext();
		PropertyValues propertyValues = component.getPropertyValuesMap().get(field);
		
        Element attribute = getDocument().createElement("attribute");
        tag.appendChild(attribute);
        Element description = getDocument().createElement("description");
        String des = propertyValues.tlddoc;
        if ("null".endsWith(des)) {
        	des = "&nbsp;";
        }
        CDATASection descriptionCDATA = getDocument().createCDATASection(des);
        description.appendChild(descriptionCDATA);
        attribute.appendChild(description);
        addNode(attribute, "name", field.getName());
        addNode(attribute, "required", String.valueOf(propertyValues.required));
        addNode(attribute, "rtexprvalue", "false");
        addNode(attribute, "type", field.getType().getName());

//
    }
    
    
}
