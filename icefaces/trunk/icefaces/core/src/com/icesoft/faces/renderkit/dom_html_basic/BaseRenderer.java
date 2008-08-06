package com.icesoft.faces.renderkit.dom_html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.util.CoreUtils;

public class BaseRenderer extends Renderer{
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);

    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        super.encodeBegin(facesContext, uiComponent);
        
    }
    
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        super.encodeChildren(facesContext, uiComponent);
        
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        super.encodeEnd(facesContext, uiComponent);    
        CoreUtils.recoverFacesMessages(facesContext, uiComponent);
        JavascriptContext.fireEffect(uiComponent, facesContext);
    }
    
    public String getResourceURL(FacesContext context, String path) {
        return DomBasicRenderer.getResourceURL(context, path);
    }    
    
}
