package com.icesoft.faces.component;

import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.context.BasicResponseWriter;
import org.icefaces.context.DOMResponseWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

public class CompatDOMResponseWriter extends DOMResponseWriter {
    public CompatDOMResponseWriter(Writer writer) {
        super(writer);
    }

    public CompatDOMResponseWriter(Document document, Writer writer) {
        super(document, writer);
    }

    public void endDocument() throws IOException {
        boolean isPartialRequest = FacesContext.getCurrentInstance().getPartialViewContext().isPartialRequest();
        Document document = getDocument();
        //full-page requests write directly to the response
        Node body = document.getElementsByTagName("body").item(0);
        Element div = (Element) body.appendChild(document.createElement("div"));
        div.setAttribute("id", "dynamic-code");
        div.setAttribute("style", "visibility: hidden; display: none;");
        Element script = (Element) div.appendChild(document.createElement("script"));
        script.setAttribute("type", "text/javascript");
        if (!isPartialRequest) {
            script.appendChild(document.createTextNode(JavascriptContext.getJavascriptCalls(FacesContext.getCurrentInstance())));
        }
        super.endDocument();
    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        if (writer.getClass().getName().endsWith("FastStringWriter")) {
            return new BasicResponseWriter(writer, getContentType(), getCharacterEncoding());
        }
        return new CompatDOMResponseWriter(null, writer);
    }
}
