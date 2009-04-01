package com.icesoft.faces.component.panelcollapsible;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.FormRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;

public class PanelCollapsibleRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_PANELCOLLAPSIBLE);

    private static Log log = LogFactory.getLog(PanelCollapsibleRenderer.class);


    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        PanelCollapsible panelCollapsible = (PanelCollapsible) uiComponent;
        DOMContext domContext = DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element rootSpan = domContext.createElement(HTML.DIV_ELEM);
            domContext.setRootNode(rootSpan);
            setRootElementId(facesContext, rootSpan, uiComponent);
        }
        Element root = (Element) domContext.getRootNode();
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        root.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getStyleClass());
        
        //create "header" div and append to the parent, don't render any children yet
        Element header = (Element) domContext.createElement(HTML.DIV_ELEM);
        header.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getHeaderClass());
        root.appendChild(header);
        
        //create "contents" div and append to the parent, don't render any children yet        
        Element contents = (Element) domContext.createElement(HTML.DIV_ELEM);
        contents.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getContentClass());
        root.appendChild(contents);
        
        //add click handler if not disabled and toggleOnClick is set to true
        if (panelCollapsible.isToggleOnClick() && 
        		!panelCollapsible.isDisabled()) {
            Element hiddenField = domContext.createElement(HTML.INPUT_ELEM);
            hiddenField.setAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext)+ "Expanded");
            hiddenField.setAttribute(HTML.TYPE_ATTR, "hidden");
            root.appendChild(hiddenField);
            UIComponent form = findForm(uiComponent);
            if(form == null) {
                throw new FacesException("PanelCollapsible must be contained within a form");                
            }
            header.setAttribute(HTML.ONCLICK_ATTR, 
                "document.forms['"+ form.getClientId(facesContext) +"']" +
                      "['"+ uiComponent.getClientId(facesContext)+ "Expanded"+"'].value='"+ 
                      panelCollapsible.isExpanded()+"'; " +
                              "iceSubmit(document.forms['"+ form.getClientId(facesContext) +"'],this,event); return false;");
        }

    }
    
    
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
    	validateParameters(facesContext, uiComponent, null);
    	PanelCollapsible panelCollapsible = (PanelCollapsible) uiComponent;
    	DOMContext domContext = DOMContext.getDOMContext(facesContext, uiComponent);
    	
    	//if headerfacet found, get the header div and render all its children
        UIComponent headerFacet = uiComponent.getFacet("header");
        if(headerFacet != null){
        	Element header = (Element)domContext.getRootNode().getFirstChild();
        	domContext.setCursorParent(header);
        	CustomComponentUtils.renderChild(facesContext,headerFacet);
        }

        //if expanded get the content div and render all its children 
        if (panelCollapsible.isExpanded()) {
        	Element contents = (Element)domContext.getRootNode().getFirstChild().getNextSibling();
        	domContext.setCursorParent(contents);    
        	if (uiComponent.getChildCount() > 0){
    	        Iterator children = uiComponent.getChildren().iterator();
    	        while (children.hasNext()) {
    	            UIComponent nextChild = (UIComponent) children.next();
    	            if (nextChild.isRendered()) {
    	                encodeParentAndChildren(facesContext, nextChild);
    	            }
    	        }
        	}
        }
        domContext.stepOver();
    }
}

