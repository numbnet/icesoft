/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.generator.xmlbuilder;

import java.lang.reflect.Field;

import org.icefaces.ace.generator.utils.Utility;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.context.MetaContext;

import org.icefaces.ace.generator.utils.PropertyValues;

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
        CDATASection descriptionCDATA = getDocument().createCDATASection( component.tlddoc() + getFacetsTlddoc(clazz) + getClientEventsTlddoc(clazz));
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
        addNode(tag, "name", component.tagName());
        addNode(tag, "tag-class", component.componentClass()+"Tag");
        addNode(tag, "body-content", "JSP");
//
    }
	
    public void addTagInfo(Class clazz, TagHandler tagHandler) {
        //String tagName = component.componentClass().substring((component.componentClass().lastIndexOf('.')+1));
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        Element description = getDocument().createElement("description");
        CDATASection descriptionCDATA = getDocument().createCDATASection( tagHandler.tlddoc());
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
        addNode(tag, "name", tagHandler.tagName());
        //addNode(tag, "tag-class", component.componentClass()+"Tag");
		addNode(tag, "tag-class", "");
        addNode(tag, "body-content", "JSP");
    }
    
    public void addAttributeInfo(Field field) {
		MetaContext metaContext = GeneratorContext.getInstance().getActiveMetaContext();
		PropertyValues propertyValues = metaContext.getPropertyValuesMap().get(field);
		
        Element attribute = getDocument().createElement("attribute");
        tag.appendChild(attribute);
        Element description = getDocument().createElement("description");
        String des = propertyValues.tlddoc;
        boolean meaningfulDes = true;
        if ("null".endsWith(des)) {
        	des = "&nbsp;";
            meaningfulDes = false;
        } 

        // ICE-6209 Append the default value to the description, if present
        String defaultVal = propertyValues.defaultValue;
        if (! "null".endsWith(defaultVal)) {
        	des += (meaningfulDes) ? " Default = '" : "Default = '";
            des += defaultVal + "'.";
            meaningfulDes = true;
        }

        String propertyName = Utility.resolvePropertyName(field, propertyValues);

        CDATASection descriptionCDATA = getDocument().createCDATASection(des);
        description.appendChild(descriptionCDATA);
        attribute.appendChild(description);
        addNode(attribute, "name", propertyName);
        addNode(attribute, "required", String.valueOf(propertyValues.required));
        addNode(attribute, "rtexprvalue", "false");

        if (field.getType().isArray())
            addNode(attribute, "type", field.getType().getComponentType().getName()+"[]");
        else
            addNode(attribute, "type", field.getType().getName());

    }       
	
	private String getClientEventsTlddoc(Class clazz) {
	
        if (clazz.isAnnotationPresent(ClientBehaviorHolder.class)) {
            ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) clazz.getAnnotation(ClientBehaviorHolder.class);
			ClientEvent[] events = clientBehaviorHolder.events();
			if (events.length > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append("<hr><table border='1' cellpadding='3' cellspacing='0' width='100%'>");
				builder.append("<tr bgcolor='#CCCCFF' class='TableHeadingColor'><td colspan='2'><font size='+2'><b>Client Events</b></font></td></tr>");

				for (int i = 0; i < events.length; i++) {
					builder.append("<tr><td>");
					ClientEvent event = events[i];
					builder.append(event.name());
					builder.append("</td><td>");
					builder.append(event.tlddoc());
					builder.append("</td></tr>");
				}
				builder.append("</table><i>Client events can be used with Client Behaviors and the ace:ajax tag.</i><br>");

				return builder.toString();
			}
		}
		return "";
	}
	
	private String getFacetsTlddoc(Class clazz) {

		Class[] classes = clazz.getDeclaredClasses();
		if (classes.length > 0) {
			boolean hasFacets = false;
			for (int i = 0; i < classes.length; i++) {
				Class childClass = classes[i];
				if (childClass.isAnnotationPresent(Facets.class)) {
					hasFacets = true;
					break;
				}
			}
			
			if (hasFacets) {			
				StringBuilder builder = new StringBuilder();
				builder.append("<hr><table border='1' cellpadding='3' cellspacing='0' width='100%'>");
				builder.append("<tr bgcolor='#CCCCFF' class='TableHeadingColor'><td colspan='2'><font size='+2'><b>Facets</b></font></td></tr>");
				for (int i = 0; i < classes.length; i++) {
					Class childClass = classes[i];
					if (childClass.isAnnotationPresent(Facets.class)) {
						Field[] fields = childClass.getDeclaredFields();
						for (int j = 0; j < fields.length; j++) {
							Field field = fields[j];
							if (field.isAnnotationPresent(Facet.class)) {
								Facet facet = (Facet) field.getAnnotation(Facet.class);
								String name = facet.name().trim();
								if ("".equals(name)) name = field.getName();
								builder.append("<tr><td>");
								builder.append(name);
								builder.append("</td><td>");
								builder.append(facet.tlddoc());
								builder.append("</td></tr>");
							}
						}
					}
				}
				builder.append("</table><br>");

				return builder.toString();
			}
		}
		return "";
	}
}
