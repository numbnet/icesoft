package org.icefaces.event;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import java.util.logging.Logger;

/**
 * Writer to add common javascript blurb to form as a &lt;script&gt; element
 */
public class ScriptWriter extends UIOutput {

    private String javascriptContents;
    private UIComponent parent;

    private static final Logger Log = Logger.getLogger(ScriptWriter.class.getName());

    /**
     * Write an adhoc snippet of un-escaped javascript. It is apparently unnecessary for
     * javascript elements to have id's, but if one is passed, it's used. A side effect
     * of having an id is to cause the script tag to be enclosed in a <span> tag
     * that itself has an id.
     *
     * @param parent the UIComponent to add the javascript node to
     * @param javascriptContents The intended contents intended for between the <script></script> tags
     * @param optionalId An id for the script element.
     */
    public ScriptWriter(UIComponent parent, String javascriptContents, String optionalId) {
        this.parent = parent; 
        this.javascriptContents = javascriptContents;
        // prevent the rendering of id's on these scripts
        setRendererType("javax.faces.resource.Script");
        this.setTransient(true);
        // Script Renderer expects a name attribute and renders a warning
        // without them
        getAttributes().put("name", "noname");
        if (optionalId != null) {
            this.setId(parent.getId() + optionalId);
        }
    }

    @Override
    public void encodeBegin(FacesContext context) {
        ResponseWriter writer = context.getResponseWriter();
        try {
            writer.startElement("script", parent);
            writer.writeAttribute("type", "text/javascript", "type");
            writer.write(javascriptContents);
            writer.endElement("script");
        } catch (Exception ioe) {
            Log.severe("Exception encoding script tag: " +  ioe);
        }
    }
}
