/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.generator.xmlbuilder;

import java.lang.reflect.Field;

import org.icefaces.generator.Generator;
import org.icefaces.generator.context.ComponentContext;
import org.icefaces.generator.context.GeneratorContext;
import org.icefaces.generator.utils.PropertyValues;
import org.icefaces.generator.utils.Utility;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Required;

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
        addEffectBehavior("Animation");
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
        if (GeneratorContext.getInstance().getActiveComponentContext().isHasMethodExpression()) {
            addNode(component_element, "handler-class", clazz.getName()+ "Handler");
        }
        //addNode(component_element, "handler-class", component.handlerClass());
    }
    
    private void addEffectBehavior(String name) {
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "tag-name", name.toLowerCase());
        Element behavior = getDocument().createElement("behavior");
        tag.appendChild(behavior);
        addNode(behavior, "behavior-id", "org.icefaces.animation."+ name);
        addNode(behavior, "handler-class", "org.icefaces.component.animation.AnimationBehaviorHandler");   
    }
    
    public void addAttributeInfo(Field field) {
		ComponentContext component = GeneratorContext.getInstance().getActiveComponentContext();
		PropertyValues propertyValues = component.getPropertyValuesMap().get(field);
		
        Element attribute = getDocument().createElement("attribute");
        tag.appendChild(attribute);
  
        String des = propertyValues.tlddoc;
        if (des != Property.Null && des.trim().length() != 0) {
        	addNode(attribute, "description", des);
        }

        addNode(attribute, "name", field.getName());
        
        String required = "false";
        if (propertyValues.required == Required.yes) {
        	required = "true";
        }
        addNode(attribute, "required",required);

        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey( field.getName().toString().trim() );
        String returnAndArgumentType = field.getType().getName();

        if (isPrimitive) {
            if (GeneratorContext.WrapperTypes.containsKey( field.getType().getName() )) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get( field.getType().getName() );
            }
        }
        addNode(attribute, "type", returnAndArgumentType);            
    }    
}
