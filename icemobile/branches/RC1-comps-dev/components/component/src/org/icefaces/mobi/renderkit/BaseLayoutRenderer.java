package org.icefaces.mobi.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class BaseLayoutRenderer extends CoreRenderer {
    protected static Map<String, StringBuilder> menuItemCfg = new HashMap<String, StringBuilder>() ;
    /*
        in order to use these must have empty encodeChildren method in Renderer.
     */
    protected void renderChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        for (UIComponent child : uiComponent.getChildren()) {
            renderChild(facesContext, child);
        }
    }

    protected void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }
        //do we have to worry about encodeAll method???
        child.encodeBegin(facesContext);
        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }

    protected static void addMenuItemCfg(String clientId,StringBuilder sb){
        BaseLayoutRenderer.menuItemCfg.put(clientId, sb);
    }

}
