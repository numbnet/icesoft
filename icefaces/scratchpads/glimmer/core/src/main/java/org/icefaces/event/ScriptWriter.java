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

    private String javascriptContents;
    private String id;
    private HtmlForm form;

    private static final Logger Log = Logger.getLogger(ScriptWriter.class.getName());

    /**
     * Write an adhoc snippet of un-escaped javascript. It is apparently unnecessary for
     * javascript elements to have id's, but if one is passed, it's used. A side effect
     * of having an id is to cause the script tag to be enclosed in a <span> tag
     * that itself has an id.
     *
     * @param javascriptContents The intended contents intended for between the <script></script> tags
     * @param optionalId An id for the script element.
     */
    public ScriptWriter(HtmlForm form, String javascriptContents, String optionalId) {
        this.form = form;
        this.javascriptContents = javascriptContents;
        this.id = optionalId; 
    }

    @Override
    public void encodeBegin(FacesContext context) {
        ResponseWriter writer = context.getResponseWriter();
        try {
            writer.startElement("script", form);
            writer.writeAttribute("type", "text/javascript", "type");
            writer.write(javascriptContents);
            writer.endElement("script");
            if (id != null) {
                this.setId( id);
            } 
        } catch (IOException ioe) {
            Log.severe("Exception encoding script tag: " +  ioe);
        }
    }
}
