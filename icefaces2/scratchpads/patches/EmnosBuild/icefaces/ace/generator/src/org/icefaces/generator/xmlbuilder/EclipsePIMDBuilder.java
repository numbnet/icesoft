package org.icefaces.generator.xmlbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.icefaces.component.annotation.Component;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class EclipsePIMDBuilder extends XMLBuilder{
    private Element value;
    private Properties  type2TypeMap = new Properties();
//    private Properties name2TypeMap = new Properties();
//    private Properties qeMap = new Properties();
    
    private static String PACKAGE="org/icefaces/generator/xmlbuilder/";
    
    private List<Component> components = new ArrayList<Component>();
    public EclipsePIMDBuilder() {
        super("icefaces_ace_pi.xml");
        try {
 	       InputStream inType2Type = new FileInputStream("c:\\EclipsePITemplateMap.properties");
 			type2TypeMap.load(inType2Type);

 		} catch (IOException e) {
 			e.printStackTrace();
 		}
        Element root = getDocument().createElement("md:metadatamodel");
        root.setAttribute("xmlns:md", "http://org.eclipse.jst.jsf.common.metadata/metadata.ecore");;
        root.setAttribute("xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
        root.setAttribute("xmlns:mdt", "http://org.eclipse.jst.jsf.common.metadata/metadataTraitTypes.ecore" );
        root.setAttribute("xmlns:pi", "http://org.eclipse.jsf.pagedesigner/paletteInfos.ecore");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("id","http://www.icefaces.org/icefaces/components");
        root.setAttribute("type","tagFile");
        root.appendChild(addTraitElementValue("is-jsf-component-library", "mdt:BooleanValue", "true"));
        root.appendChild(addTraitElement("images-base-path", "/icons/palette/ICEfaces/ace/"));
        root.appendChild(addTraitElement("display-label", "ICEfaces Advanced Components"));
        root.appendChild(addTraitElement("description", "ICEfaces Advanced Components"));
        root.appendChild(addTraitElement("default-prefix", "ice"));
        getDocument().appendChild(root);
        

	 
	 Element attTrait = getDocument().createElement("trait");
     attTrait.setAttribute("id","paletteInfos");
     
     value = getDocument().createElement("value");
     value.setAttribute("xsi:type","pi:PaletteInfos");
     attTrait.appendChild(value);
     root.appendChild(attTrait);   
      
    }
    public void addTagInfo(Component component) {
//    public void addPaletteInfos (Component[] components) {
    	components.add(component);
	    Element item = getDocument().createElement("item");
	    item.setAttribute("id",component.tagName());
		value.appendChild(item);
    }
    
    public void write () {
    	addTagDetail(components);
    	super.write();
    }
    
    public void addTagDetail (List<Component> components) {
    	for (Component component : components) {
			
		
        Element root = (Element)getDocument().getDocumentElement();
        Element tag = getDocument().createElement("entity");
        String name = component.tagName();
        String firstLetter = name.substring(0,1);
        String remainder = name.substring(1);
        tag.setAttribute("id", component.tagName());
        tag.setAttribute("type", "tag");
        root.appendChild(tag); 
//        Element trait = addTraitElement("display-label", component.tagName());
//        tag.appendChild(trait);
        Element trait = addTraitElement("small-icon", "small/"+firstLetter.toUpperCase()+remainder+"_C16.png");
        tag.appendChild(trait);
		String template = type2TypeMap.getProperty(name);
		if (template != null) {
			trait = addTraitElementWithTemplate(name, template);
			tag.appendChild(trait);
		}
		}
        
    }


	private Element addTraitElement (String id, String typeValue) {
		 Element attTrait = getDocument().createElement("trait");
	        attTrait.setAttribute("id", id);
	        Element value = getDocument().createElement("value");
	        value.setAttribute("xsi:type", "mdt:StringValue");
	        attTrait.appendChild(value);
	        Text typeText = getDocument().createTextNode(typeValue);
	        value.appendChild(typeText);
	     return attTrait;
	}
	
	private Element addTraitElementWithTemplate (String name, String content) {
		    Element attTrait = getDocument().createElement("trait");
	        attTrait.setAttribute("id", "tag-create");
	        Element value = getDocument().createElement("value");
	        value.setAttribute("xsi:type", "pi:TagCreationInfo");
	        attTrait.appendChild(value);
	        
	        Element templateElement = getDocument().createElement("template"); 
	        value.appendChild(templateElement);
	        
	        CDATASection cdata = getDocument().createCDATASection(content);
	        templateElement.appendChild(cdata);
	     return attTrait;
	}
	
	private Element addTraitElementValue (String id, String valueType,String typeValue) {
		 Element attTrait = getDocument().createElement("trait");
	        attTrait.setAttribute("id", id);
	        Element value = getDocument().createElement("value");
	        value.setAttribute("xsi:type",valueType);
	        attTrait.appendChild(value);
	        Text typeText = getDocument().createTextNode(typeValue);
	        value.appendChild(typeText);
	     return attTrait;
	}
	
}
