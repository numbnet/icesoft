package org.icefaces.mobi.component.util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.icefaces.mobi.utils.JSFUtils;

public class ViewRenderer extends Renderer {

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        Iterator<UIComponent> iter = component.getChildren().iterator();
        while( iter.hasNext() ){
            UIComponent child = iter.next();
            JSFUtils.renderChild(facesContext, child);
        }
        
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

