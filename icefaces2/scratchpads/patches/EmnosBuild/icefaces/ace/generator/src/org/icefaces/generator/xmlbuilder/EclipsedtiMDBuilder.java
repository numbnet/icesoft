package org.icefaces.generator.xmlbuilder;

import org.icefaces.component.annotation.Component;
import org.w3c.dom.Element;

public class EclipsedtiMDBuilder extends XMLBuilder{
 
	public EclipsedtiMDBuilder() {
        super("icefaces_ace_dti.xml");
        Element root = getDocument().createElement("md:metadatamodel");
        root.setAttribute("xmlns:md", "http://org.eclipse.jst.jsf.common.metadata/metadata.ecore");;
        root.setAttribute("xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
        root.setAttribute("xmlns:dti", "http://org.eclipse.jsf.pagedesigner/dtinfo.ecore");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("id","http://www.icefaces.org/icefaces/components");
        root.setAttribute("type","tagFile");
        getDocument().appendChild(root);
    }
    public void addTagInfo(Component component) {
        Element root = (Element)getDocument().getDocumentElement();
        Element entity = getDocument().createElement("entity");
        entity.setAttribute("id", component.tagName());
        entity.setAttribute("type", "tag");
        root.appendChild(entity);
        
		Element attTrait = getDocument().createElement("trait");
	    attTrait.setAttribute("id", "dt-info");
	    entity.appendChild(attTrait);
	    
	    Element value = getDocument().createElement("value");
	    value.setAttribute("xsi:type","dti:DTInfo");
	    attTrait.appendChild(value);

        String name = component.tagName();
        String firstLetter = name.substring(0,1);
        String remainder = name.substring(1);
 
	    Element convInfo = getDocument().createElement("tag-convert-info");
	    value.appendChild(convInfo);
	    Element operation = getDocument().createElement("operation");
	    operation.setAttribute("id", "org.icefaces.eclipse.jst.pagedesigner."+firstLetter.toUpperCase()+remainder+"Operation");
	    convInfo.appendChild(operation);
	    
	    Element decorInfo = getDocument().createElement("tag-decorate-info");
	    decorInfo.setAttribute("id","vpd-decorate-design");
	    decorInfo.setAttribute("needBorderDecorator","true");
	    decorInfo.setAttribute("multiLevel","true");
	    value.appendChild(decorInfo);

    }
    	
}
