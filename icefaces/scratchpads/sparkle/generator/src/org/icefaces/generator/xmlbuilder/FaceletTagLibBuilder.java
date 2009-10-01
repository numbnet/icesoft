package org.icefaces.generator.xmlbuilder;

import java.lang.reflect.Field;

import org.icefaces.generator.FileWriter;
import org.w3c.dom.Element;

import org.icefaces.component.annotation.Component;

public class FaceletTagLibBuilder extends XMLBuilder{
    private Element tag;
    
    public FaceletTagLibBuilder() {
        super("icefaces.taglib.xml");
        Element root = getDocument().createElement("facelet-taglib");
        getDocument().appendChild(root);
        addNode(root, "namespace", "http://www.icesoft.com/icefaces/component");
    }

    public void addTagInfo(Class clazz, Component component) {
        String tagName = component.component_class().substring((component.component_class().lastIndexOf('.')+1));
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "tag-name", component.tag_name());
        Element component_element = getDocument().createElement("component");
        tag.appendChild(component_element);
        try {
            Field comp_type = clazz.getField("COMPONENT_TYPE");
            addNode(component_element, "component-type", String.valueOf(comp_type.get(comp_type)));

            String rendererType = FileWriter.getPropertyValue(clazz, "RENDERER_TYPE","getRendererType" );
            addNode(component_element, "renderer-type", rendererType);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        addNode(component_element, "handler-class", component.handler_class());
    }
}
