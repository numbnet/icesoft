/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

import org.icefaces.ace.generator.context.MetaContext;
import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.generator.utils.Utility;
import org.icefaces.ace.meta.annotation.Required;
import org.w3c.dom.Element;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.Property;

public class FaceletTagLibBuilder extends XMLBuilder{
    private Element tag;
    
    public FaceletTagLibBuilder() {
        super("icefaces.taglib.xml");
        Element root = getDocument().createElement("facelet-taglib");
        root.setAttribute("xmlns",              "http://java.sun.com/xml/ns/javaee");
        root.setAttribute("xmlns:xsi",          "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd");
        root.setAttribute("version",            "2.0");
        getDocument().appendChild(root);
        addNode(root, "namespace", GeneratorContext.namespace);
    }

    public void addTagInfo(Class clazz, Component component) {
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "tag-name", component.tagName());
        Element component_element = getDocument().createElement("component");
        tag.appendChild(component_element);
        try {
            addNode(component_element, "component-type", Utility.getComponentType(component));
            if (!"".equals(component.rendererType())) {
                addNode(component_element, "renderer-type", component.rendererType());
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(component.handlerClass())) {
            addNode(component_element, "handler-class", component.handlerClass());
        }
        else if (GeneratorContext.getInstance().getActiveMetaContext().isHasMethodExpression()) {
            addNode(component_element, "handler-class", clazz.getName()+ "Handler");
        }
    }
	
    public void addTagInfo(TagHandler tagHandler) {
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "tag-name", tagHandler.tagName());
		addNode(tag, "handler-class", tagHandler.tagHandlerClass());
    }
    
    public void addBehaviorInfo(TagHandler tagHandler) {
	
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "tag-name", tagHandler.tagName());
        Element behavior = getDocument().createElement("behavior");
        tag.appendChild(behavior);
        addNode(behavior, "behavior-id", tagHandler.behaviorId());
        addNode(behavior, "handler-class", tagHandler.tagHandlerClass());
    }
    
    public void addAttributeInfo(Field field) {
		MetaContext metaContext = GeneratorContext.getInstance().getActiveMetaContext();
		PropertyValues propertyValues = metaContext.getPropertyValuesMap().get(field);
		
        Element attribute = getDocument().createElement("attribute");
        tag.appendChild(attribute);
  
        String des = propertyValues.tlddoc;
        if (des != Property.Null && des.trim().length() != 0) {
        	addNode(attribute, "description", des);
        }

        String propertyName = Utility.resolvePropertyName(field, propertyValues);
		addNode(attribute, "name", propertyName);
        
        String required = "false";
        if (propertyValues.required == Required.yes) {
        	required = "true";
        }
        addNode(attribute, "required",required);

        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey( field.getName().toString().trim() );

        boolean isArray = field.getType().isArray();

        String returnAndArgumentType = isArray ? field.getType().getComponentType().getName() + "[]"
                                               : field.getType().getName();

        if (isPrimitive) {
            if (GeneratorContext.WrapperTypes.containsKey( field.getType().getName() )) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get( field.getType().getName() );
            }
        }
        addNode(attribute, "type", returnAndArgumentType);            
    }    
}
