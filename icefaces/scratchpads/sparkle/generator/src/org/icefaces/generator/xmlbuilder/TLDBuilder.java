package org.icefaces.generator.xmlbuilder;

import java.lang.reflect.Field;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

public class TLDBuilder extends XMLBuilder{
    private Element tag;
    
    public TLDBuilder() {
        super("components.tld");
        Element root = getDocument().createElement("taglib");
        getDocument().appendChild(root);
        addNode(root, "tlib-version", "1.7");
        addNode(root, "jsp-version", "1.2");
        addNode(root, "short-name", "ann");
        addNode(root, "uri", "http://www.icesoft.com/icefaces/component/annotated");
        addNode(root, "display-name", "ICEfaces Component Suite");         
    }

    public void addTagInfo(Class clazz, Component component) {
        String tagName = component.componentClass().substring((component.componentClass().lastIndexOf('.')+1));
        
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");        
        root.appendChild(tag);
        addNode(tag, "name", component.tagName());
        addNode(tag, "tag-class", component.componentClass()+"Tag");
        addNode(tag, "body-content", "JSP");
        Element description = getDocument().createElement("description");  
        CDATASection descriptionCDATA = getDocument().createCDATASection( component.tlddoc());
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
    }
    
    public void addAttributeInfo(Field field, Property property) {
        Element attribute = getDocument().createElement("attribute");
        tag.appendChild(attribute);
        addNode(attribute, "name", field.getName());
        addNode(attribute, "required", String.valueOf(property.required()));
        addNode(attribute, "rtexprvalue", "false");        
        Element description = getDocument().createElement("description");  
        CDATASection descriptionCDATA = getDocument().createCDATASection(property.tlddoc());
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);        
    }
    
    
}
