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
import org.w3c.dom.NodeList;

public class RADJLDBuilder extends XMLBuilder{
    
	private Element currentTag;
	private Element tagLibrary;
	
    private Properties  type2TypeMap = new Properties();
    private Properties name2TypeMap = new Properties();
    
    private static String PACKAGE="org/icefaces/generator/xmlbuilder/";
    private static String ICON_PATH = "${org.icefaces.ee.rad.visualizer}/icons/palette/ICEfaces/small/";
    
    public RADJLDBuilder() {
        super("ace.jld");
        
		try {
		   InputStream inType2Type = new FileInputStream("c:\\RADAttribTypeMap.properties");
           InputStream inName2Type = new FileInputStream("c:\\RADAttribNameTypeMap.properties");

			type2TypeMap.load(inType2Type);
			name2TypeMap.load(inName2Type);
		} catch (IOException e) {
			e.printStackTrace();
		}
        Element root = getDocument().createElement("library-definition");
        root.setAttribute("min-jsf-level", "2.0");
        root.setAttribute("prefix", "ace");
        root.setAttribute("taglib-uri","http://www.icesoft.com/icefaces/component" );
        root.setAttribute("tld-file", "META-INF/icefaces_component.tld");
        root.setAttribute("version", "2.0" );
        root.setAttribute("xmlns", "http://www.ibm.com/facesLibrary");
        root.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation","http://www.ibm.com/facesLibrary http://www.ibm.com/facesLibrary.xsd");
        getDocument().appendChild(root);
        
        Element description = getDocument().createElement("description");
        description.appendChild(getDocument().createTextNode("ICEfaces Advanced Components"));
        root.appendChild(description);
        
        tagLibrary = getDocument().createElement("tag-library");
        tagLibrary.setAttribute("icon", "${org.icefaces.ee.rad.visualizer}/icons/icefaces.gif");
        tagLibrary.setAttribute("label", "ICEfaces Advanced Components");
        tagLibrary.setAttribute("visible", "true");
        tagLibrary.setAttribute("initially-opened", "true");
        tagLibrary.setAttribute("initially-pinned", "false");    
        root.appendChild(tagLibrary);
    }
    
    

    public void addTagInfo(Component component) {
        currentTag = getDocument().createElement("tag");
        String name = component.tagName();
        currentTag.setAttribute("name", name);
        currentTag.setAttribute("label", getComponentLabel(name));
        currentTag.setAttribute("large-icon", ICON_PATH+getComponentIconFile(name));
        currentTag.setAttribute("small-icon", ICON_PATH+getComponentIconFile(name)); 
        currentTag.setAttribute("palette-visibility","visible");
        tagLibrary.appendChild(currentTag);

        Element description = getDocument().createElement("description");
        description.appendChild(getDocument().createTextNode(component.tlddoc()));
        currentTag.appendChild(description);
        
        Element drop = getDocument().createElement("drop-info");
        drop.setAttribute("allows-children", "false");
        drop.setAttribute("requires-form", "true");
        drop.setAttribute("tag-type", "uicomponent");
        currentTag.appendChild(drop);
        
        Element visualization = getDocument().createElement("visualization");
        Element markup = getDocument().createElement("markup");
        
        markup.appendChild(getDocument().createCDATASection("<span>"+name+"</span>"));
        currentTag.appendChild(visualization);
        visualization.appendChild(markup);
        
        Element attributes = getDocument().createElement("attributes");
        currentTag.appendChild(attributes);
    }
    
    public void addAttributeInfo(Field field) {
		ComponentContext component = GeneratorContext.getInstance().getActiveComponentContext();
		PropertyValues propertyValues = component.getPropertyValuesMap().get(field);
		
		NodeList list = currentTag.getElementsByTagName("attributes");
		if (list.getLength() == 0) { return; }
		Element attributes = (Element) list.item(0);
		
		
        Element attribute = getDocument().createElement("attribute");
        attribute.setAttribute("name", field.getName());
        attributes.appendChild(attribute);
        
        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey( field.getName().toString().trim() );
        String returnAndArgumentType = field.getType().getName();
        if (isPrimitive) {
            if (GeneratorContext.WrapperTypes.containsKey( field.getType().getName() )) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get( field.getType().getName() );
            }
        }
        String type = name2TypeMap.getProperty(field.getName());
        if (type == null) {
        	type = type2TypeMap.getProperty(returnAndArgumentType);
        }      
        if (type!=null) {
        	attribute.setAttribute("type", type);
        } else {
        	attribute.setAttribute("type", returnAndArgumentType);
        }

        attributes.appendChild(attribute);
        
        String des = propertyValues.tlddoc;
        if ("null".endsWith(des)) {
        	des = "&nbsp;";
        }
        Element description = getDocument().createElement("description");
        description.appendChild(getDocument().createTextNode(des));
        attribute.appendChild(description);
    }


	public static String getComponentLabel (String name) {
		return name.substring(0,1).toUpperCase() + name.substring(1);
	}
	
	public static String getComponentIconFile (String name) {
		return getComponentLabel(name)+"_C16.png";
	}	
}
