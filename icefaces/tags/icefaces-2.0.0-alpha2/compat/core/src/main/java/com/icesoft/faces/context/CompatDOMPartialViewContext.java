package com.icesoft.faces.context;

import com.icesoft.faces.component.CompatDOMResponseWriter;
import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.context.DOMPartialViewContext;
import org.icefaces.context.DOMResponseWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import java.io.IOException;
import java.io.Writer;

public class CompatDOMPartialViewContext extends DOMPartialViewContext {

    public CompatDOMPartialViewContext(PartialViewContext partialViewContext, FacesContext facesContext) {
        super(partialViewContext, facesContext);
    }

    protected void renderExtensions() {
        String javascriptCalls = JavascriptContext.getJavascriptCalls(facesContext);
        if (javascriptCalls != null && javascriptCalls.trim().length() > 0) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                partialWriter.startEval();
                partialWriter.write(javascriptCalls);
                partialWriter.endEval();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected DOMResponseWriter createDOMResponseWriter(Writer outputWriter) {
        return new CompatDOMResponseWriter(outputWriter);
    }
}
