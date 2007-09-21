package com.icesoft.faces.component.portlet;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class PortletRenderer extends DomBasicRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        validateParameters(facesContext, uiComponent, UINamingContainer.class);
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (!domContext.isInitialized()) {
            String clientID = uiComponent.getClientId(facesContext);
            Element root = domContext.createElement(HTML.DIV_ELEM);

            domContext.setRootNode(root);
            root.setAttribute(HTML.ID_ATTR, clientID);
            System.out.println("PortletRenderer.encodeBegin:  id = " + clientID );
        }

        Element root = (Element) domContext.getRootNode();

        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }

        PassThruAttributeRenderer
                .renderAttributes(facesContext, uiComponent, null);
        facesContext.getApplication().getViewHandler().writeState(facesContext);

        try {
            domContext.startNode(facesContext, uiComponent, root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        domContext.stepInto(uiComponent);
    }



//    protected void renderEnd(FacesContext facesContext, UIComponent component, String currentValue) throws IOException {
//
//        validateParameters(facesContext, component, UINamingContainer.class);
//
//        DOMContext domContext = DOMContext.attachDOMContext(facesContext, component);
//
//        if (!domContext.isInitialized()) {
//            Element root = domContext.createElement(HTML.DIV_ELEM);
//            domContext.setRootNode(root);
//            setRootElementId(facesContext, root, component);
//            root.setAttribute("name", component.getClientId(facesContext));
//        }
//
//        Element root = (Element) domContext.getRootNode();
//
//        String styleClass = (String) component.getAttributes().get("styleClass");
//        if (styleClass != null) {
//            root.setAttribute("class", styleClass);
//        }
//
//        PassThruAttributeRenderer.renderAttributes(facesContext, component, null);
//
//        domContext.stepOver();
//        domContext.streamWrite(facesContext, component);
//    }
}
