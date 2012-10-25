package org.icemobile.renderkit;

import java.io.IOException;
import java.util.logging.Logger;

import org.icemobile.component.IAccordion;
import org.icemobile.component.IMobiComponent;
import static org.icemobile.util.HTML.*;
import org.icemobile.util.Utils;

public class AccordionCoreRenderer extends BaseCoreRenderer implements IRenderer{
    
    private static final Logger logger =
            Logger.getLogger(AccordionCoreRenderer.class.toString());
    
    public void encodeBegin(IMobiComponent component, IResponseWriter writer, boolean isJSP)
            throws IOException {
        
        IAccordion accordion = (IAccordion)component;
        
        /* JSF comp writes script into 1st span */
        if (!isJSP && !accordion.isScriptLoaded()) {
            writer.startElement(SPAN_ELEM, accordion);
            writer.writeAttribute(ID_ATTR, accordion.getClientId()+"_libJS");
            writer.startElement("script", accordion);
            writer.writeAttribute("text", "text/javascript");
            writer.writeAttribute("src", accordion.getJavascriptFileRequestPath());
            writer.endElement("script");
            accordion.setScriptLoaded();
            writer.endElement(SPAN_ELEM);
        } 
        
        /* write out root tag.  For current incarnation html5 semantic markup is ignored */
        writer.startElement(DIV_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, accordion.getClientId());
        if (accordion.getSelectedId() != null && !"".equals(accordion.getSelectedId())) {
            writer.writeAttribute(" data-opened", accordion.getSelectedId());
        }
        
        StringBuilder styleClass = new StringBuilder(IAccordion.ACCORDION_CLASS);
        String userDefinedClass = accordion.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
                styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute(CLASS_ATTR, styleClass.toString());
        writer.writeAttribute(STYLE_ATTR, accordion.getStyle());
    }
    
    public void encodeEnd(IMobiComponent component, IResponseWriter writer, boolean isJSP)
            throws IOException {
        
        IAccordion accordion = (IAccordion)component;
        
        super.writeHiddenInput(writer, accordion);

        writer.endElement(DIV_ELEM);
        
        writer.startElement(SPAN_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, accordion.getClientId() + "_script");
        writer.startElement(SCRIPT_ELEM, null);
        writer.writeAttribute(INPUT_TYPE_TEXT, "text/javascript");
        StringBuilder cfg = new StringBuilder("{singleSubmit: false");
        
        String openedPaneId = accordion.getOpenedPaneClientId();
        if (null!=openedPaneId){
            cfg.append(", opened: '").append(openedPaneId).append("'");
        }
        boolean autoheight = accordion.isAutoHeight();
        int hashcode = Utils.generateHashCode(openedPaneId);
        cfg.append(", hash: ").append(hashcode);
        cfg.append(", autoHeight: ").append(autoheight);
        cfg.append(", fixedHeight: '").append(accordion.getFixedHeight()).append("'");
        cfg.append("}");
         //just have to add behaviors if we are going to use them.
        
        writer.writeText("ice.mobi.accordionController.initClient('" 
                + accordion.getClientId() + "'," +cfg.toString()+");");
        
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
    }

}
