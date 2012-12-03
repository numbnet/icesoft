package org.icemobile.renderkit;

import java.io.IOException;

import org.icemobile.component.IMobiComponent;

public interface IRenderer {
    
    public void encodeBegin(IMobiComponent component, IResponseWriter writer, boolean isJSP)
            throws IOException;
    public void encodeEnd(IMobiComponent component, IResponseWriter writer, boolean isJSP)
            throws IOException;

}
