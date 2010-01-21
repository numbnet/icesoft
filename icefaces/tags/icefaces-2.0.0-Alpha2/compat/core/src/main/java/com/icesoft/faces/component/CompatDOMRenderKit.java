package com.icesoft.faces.component;

import org.icefaces.render.DOMRenderKit;

import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import java.io.Writer;

public class CompatDOMRenderKit extends DOMRenderKit {
    public CompatDOMRenderKit(RenderKit delegate) {
        super(delegate);
    }

    public CompatDOMRenderKit() {
        super();
    }

    public ResponseWriter createResponseWriter(final Writer writer, String contentTypeList, String encoding) {
        return new CompatDOMResponseWriter(writer);
    }
}
