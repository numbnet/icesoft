package org.icefaces.generator.xmlbuilder;

import org.icefaces.generator.Generator;
import org.w3c.dom.Element;

import org.icefaces.component.annotation.Component;

public class FaceletTagLibBuilder extends XMLBuilder{
    private Element tag;
    
    public FaceletTagLibBuilder() {
        super("icefaces.taglib.xml");
        Element root = getDocument().createElement("facelet-taglib");
        getDocument().appendChild(root);
        addNode(root, "namespace", "http://www.icesoft.com/icefaces/component/annotated");
    }

    public void addTagInfo(Class clazz, Component component) {
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "tag-name", component.tagName());
        Element component_element = getDocument().createElement("component");
        tag.appendChild(component_element);
        try {
            addNode(component_element, "component-type", Generator.getComponentType(component));
            if (!"".equals(component.rendererType())) {
                addNode(component_element, "renderer-type", component.rendererType());
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        } 
        //addNode(component_element, "handler-class", component.handlerClass());
    }
}
