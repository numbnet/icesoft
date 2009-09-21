package com.icesoft.faces.component.generator.xmlbuilder;

import java.lang.reflect.Field;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.icesoft.faces.component.annotation.Component;
import com.icesoft.faces.component.generator.FileWriter;


public class FacesConfigBuilder extends XMLBuilder{
    public FacesConfigBuilder() {
        super("faces-config.xml");
        Element root = getDocument().createElement("faces-config");
        Element render_kit = getDocument().createElement("render-kit");
        getDocument().appendChild(root);
        root.appendChild(render_kit);        
    }
    
    public void addComponentInfo(Class clazz, Component component) {
        Element compElement = getDocument().createElement("component");
        Element compTypeElement = getDocument().createElement("component-type"); 
        Element compClassElement = getDocument().createElement("component-class"); 
        Text comp_class =getDocument().createTextNode(component.component_class());

        getDocument().getDocumentElement().appendChild(compElement);
        compElement.appendChild(compTypeElement);
        compElement.appendChild(compClassElement);
        try {
            System.out.println("TYP CLASS IN FACESCONFIG "+ clazz.getSimpleName());
            Field comp_type = clazz.getField("COMPONENT_TYPE");
            Text comp_type_text = getDocument().createTextNode(String.valueOf(comp_type.get(comp_type)));
            System.out.println("TYPE in CONFIG "+ comp_type_text);
            compTypeElement.appendChild(comp_type_text);
            compClassElement.appendChild(comp_class);
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
    }



    public void addRendererInfo(Class clazz, Component component) {
        try {
            //renderkit
            String rendererType = FileWriter.getPropertyValue(clazz, "RENDERER_TYPE","getRendererType" );
            Text comp_renderer_type_text = getDocument().createTextNode(rendererType);
            
            Field comp_family = clazz.getField("COMPONENT_FAMILY");
            Text comp_family_text = getDocument().createTextNode(String.valueOf(comp_family.get(comp_family)));
            
            String rendererClass = component.renderer_class();
            if (rendererClass.equals(component.EMPTY)) {
                rendererClass = component.component_class()+ "Renderer";
            }
            Text rendererClassText = getDocument().createTextNode(rendererClass);
            
            Element render_kit = (Element) getDocument().getElementsByTagName("render-kit").item(0);
            
            Element rendererElement = getDocument().createElement("renderer");            
            Element compFamilyElement = getDocument().createElement("component-family");
            Element rendererTypeElement = getDocument().createElement("renderer-type"); 
            Element rendererClassElement = getDocument().createElement("renderer-class"); 
            render_kit.appendChild(rendererElement);
            rendererElement.appendChild(compFamilyElement);
            rendererElement.appendChild(rendererTypeElement);
            rendererElement.appendChild(rendererClassElement);
            rendererTypeElement.appendChild(comp_renderer_type_text);
            compFamilyElement.appendChild(comp_family_text);
            rendererClassElement.appendChild(rendererClassText);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public void addEntry(Class clazz, Component component) {
        this.addComponentInfo(clazz, component);
        this.addRendererInfo(clazz, component);
    }

}
