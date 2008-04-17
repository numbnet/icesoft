/*
 * 
 * render a head element contains meta information about html document
 */

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class HeadRenderer extends Renderer{

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("head", component);
        PassThruAttributeWriter.renderAttributes(writer, component, new String[0]);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("head");
    }

    public boolean getRendersChildren() {
        return true;
    }

}
