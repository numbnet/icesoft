package org.icemobile.renderkit;

import java.io.IOException;

import org.icemobile.component.IMobiComponent;

import java.lang.StringBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.icemobile.util.HTML.*;

public class BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(BaseCoreRenderer.class.toString());

    public void writeStandardAttributes(IResponseWriter writer,
                    IMobiComponent component, String baseClass,
                    String disabledClass) throws IOException  {
        StringBuilder inputStyle = new StringBuilder(baseClass);
        if (component.isDisabled()){
            inputStyle.append(" ").append(disabledClass);
        }
        if (null != component.getStyleClass())  {
            inputStyle.append(" ").append(component.getStyleClass());
        }
        if( inputStyle.length() > 0 ){
            writer.writeAttribute(CLASS_ATTR, inputStyle);
        }
        if (null != component.getStyle())  {
            writer.writeAttribute(STYLE_ATTR, component.getStyle());
        }
        if (component.isDisabled())  {
            writer.writeAttribute(DISABLED_ATTR, "disabled");
        }
    }

    public void writeStandardLayoutAttributes(IResponseWriter writer,
                    IMobiComponent component, String baseClass) throws IOException  {
        StringBuilder inputStyle = new StringBuilder(baseClass);
        if (null != component.getStyleClass())  {
            inputStyle.append(" ").append(component.getStyleClass());
        }
        if( inputStyle.length() > 0 ){
            writer.writeAttribute(CLASS_ATTR, inputStyle);
        }
        if (null != component.getStyle())  {
            writer.writeAttribute(STYLE_ATTR, component.getStyle());
        }
    }
}
