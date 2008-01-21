package com.icesoft.faces.component.paneldivider;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class PanelDividerRenderer extends DomBasicRenderer{
    public boolean getRendersChildren() {
        return true;
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        validateParameters(facesContext, uiComponent, PanelDivider.class);
        PanelDivider panelDivider = (PanelDivider) uiComponent;
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element container = domContext.createRootElement(HTML.DIV_ELEM);
            setRootElementId(facesContext, container, uiComponent);
        }

        Element container = (Element) domContext.getRootNode();        
        Element dividerContainer = (Element) domContext.createElement(HTML.DIV_ELEM);
        container.setAttribute(HTML.CLASS_ATTR, panelDivider.getStyleClass());
        dividerContainer.setAttribute(HTML.CLASS_ATTR, panelDivider.getSplitterClass());
        if(panelDivider.isHorizontal()) {
            dividerContainer.setAttribute(HTML.ONMOUSEDOWN_ATTR, "new Ice.PanelDivider(event, true);");
        } else {
            dividerContainer.setAttribute(HTML.ONMOUSEDOWN_ATTR, "new Ice.PanelDivider(event);");      
        }
        
        dividerContainer.appendChild(domContext.createTextNode("&nbsp;"));
        renderPane(facesContext, uiComponent, true);
        container.appendChild(dividerContainer);
        dividerContainer.setAttribute(HTML.ID_ATTR, "divider");
        renderPane(facesContext, uiComponent, false);
        domContext.stepOver();
    }
    
    
    void renderPane(FacesContext facesContext,
                    UIComponent uiComponent,
                    boolean isFrist) throws IOException{
        DOMContext domContext = DOMContext.getDOMContext(facesContext, uiComponent);
        PanelDivider panelDivider = (PanelDivider) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        Element pane = (Element) domContext.createElement(HTML.DIV_ELEM);
        UIComponent facet= null;
        if (isFrist) {
            facet = panelDivider.getFirstFacet();
            pane.setAttribute(HTML.ID_ATTR, clientId+= "First");
            pane.setAttribute(HTML.CLASS_ATTR, panelDivider.getFirstPaneClass());
        } else {
            facet = panelDivider.getSecondFacet();
            pane.setAttribute(HTML.ID_ATTR, clientId+= "Second");    
            pane.setAttribute(HTML.CLASS_ATTR, panelDivider.getSecondPaneClass());            
        }
        if (facet == null ) {
            //log message facet required
            return;
        }
        Element container = (Element) domContext.getRootNode();  
        container.appendChild(pane);
        domContext.setCursorParent(pane);
        CustomComponentUtils.renderChild(facesContext, facet);
    }
}
