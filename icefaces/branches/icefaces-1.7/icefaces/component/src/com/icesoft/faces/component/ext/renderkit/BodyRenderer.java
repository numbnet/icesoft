/*
 * 
 * Body element defines the contents of Html document.
 */

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class BodyRenderer extends Renderer{

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("body", component);
        PassThruAttributeWriter.renderAttributes(writer, component, new String[0]);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("body");
    }

    public boolean getRendersChildren() {
        return true;
    }

}
