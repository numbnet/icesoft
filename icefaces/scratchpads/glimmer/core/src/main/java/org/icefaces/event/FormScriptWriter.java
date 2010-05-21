package org.icefaces.event;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import java.io.IOException;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * Writer to add common javascript blurb to form as a &lt;script&gt; element
 */
public class FormScriptWriter extends UIOutput {

    private String javascriptMessageFormat;
    private String disablingAttribute;
    private String id;

    private static final Logger Log = Logger.getLogger(FormScriptWriter.class.getName());

    /**
     * Write an adhoc snippet of un-escaped javascript. It is apparently unnecessary for
     * javascript elements to have id's, but if one is passed, it's used.
     *
     * @param javascriptMessageFormat The contents intended to go between
     *        the <script></script> tags, used as a MessageFormat pattern,
     *        taking the HtmlForm's clientId as the parameter.
     * @param disablingAttribute If the form has this attribute set, don't
     *        render the javascript.
     * @param id An id for the script element.
     */
    public FormScriptWriter(String javascriptMessageFormat, String disablingAttribute, String id) {
        this.javascriptMessageFormat = javascriptMessageFormat;
        this.disablingAttribute = disablingAttribute;
        this.id = id;
        // Always give the component an id, even if the markup doesn't get one
        if (id != null) {
            setId(id);
        }
        else {
            setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
        }
        setTransient(true);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        HtmlForm form = findParentForm(this);
        if (form == null) {
            return;
        }
        String formClientId = form.getClientId(context);
        if (disablingAttribute != null && disablingAttribute.length() > 0) {
            if (form.getAttributes().get(disablingAttribute) != null) {
//System.out.println("FormScriptWriter  "+disablingAttribute+"  " + formClientId);
                return;
            }
        }
        
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", "type");
        if (id != null) {
            String scriptId =
                formClientId+UINamingContainer.getSeparatorChar(context)+id;
            writer.writeAttribute("id", scriptId, "id");
        }
        String script = MessageFormat.format(javascriptMessageFormat, formClientId);
//System.out.println("FormScriptWriter  script: " + script);
        writer.write(script);
        writer.endElement("script");
    }
    
    protected HtmlForm findParentForm(UIComponent comp) {
        if (comp == null) {
            return null;
        }
        if (comp instanceof HtmlForm) {
            return (HtmlForm) comp;
        }
        return findParentForm(comp.getParent());
    }
}
