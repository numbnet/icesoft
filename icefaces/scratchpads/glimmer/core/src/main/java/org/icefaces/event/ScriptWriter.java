package org.icefaces.event;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlForm;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Writer to add common javascript blurb to form as a &lt;script&gt; element
 */
public class ScriptWriter extends UIOutput {

    private HtmlForm form;

    private static final Logger Log = Logger.getLogger(ScriptWriter.class.getName());

    public ScriptWriter(HtmlForm form) {
        this.form = form; 
    }

    @Override
    public void encodeBegin(FacesContext context) {
        ResponseWriter writer = context.getResponseWriter();
        try {
            writer.startElement("script", form);
            writer.writeAttribute("type", "text/javascript", "type");
            writer.write("ice.captureEnterKey('" + form.getClientId(context) + "')");
            writer.endElement("script");
        } catch (IOException ioe) {
            Log.severe("Exception encoding script tag: " +  ioe);
        }
    }
}
