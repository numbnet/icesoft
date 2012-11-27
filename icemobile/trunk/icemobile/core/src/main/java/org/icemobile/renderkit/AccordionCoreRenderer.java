package org.icemobile.renderkit;

import java.io.IOException;
import java.util.logging.Level;
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
        String clientId = accordion.getClientId();
        /* JSF comp writes script into 1st span */
        if (!accordion.isScriptLoaded() && !isJSP)  {
            writer.startElement(DIV_ELEM);
            writer.writeAttribute(ID_ATTR, clientId +"_libJSDiv");
            writer.startElement(SPAN_ELEM, accordion);
            writer.writeAttribute(ID_ATTR, clientId +"_libJS");
            writer.startElement("script", accordion);
            writer.writeAttribute("text", "text/javascript");
            writer.writeAttribute("src", accordion.getJavascriptFileRequestPath());
            writer.endElement("script");
            accordion.setScriptLoaded();
            writer.endElement(SPAN_ELEM);
            writer.endElement(DIV_ELEM);
        } 
        
        /* write out root tag.  For current incarnation html5 semantic markup is ignored */
        writer.startElement(DIV_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, clientId);
        writer.startElement(DIV_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, clientId+"_acc");
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
        String clientId=accordion.getClientId();
        String openedPaneId = accordion.getOpenedPaneClientId();
        writer.endElement(DIV_ELEM);  //end of _acc div
        writer.startElement(DIV_ELEM, accordion); //start of div for hidden and script
        writer.writeAttribute(ID_ATTR, clientId+"_hid");
        if (null != openedPaneId){
            super.writeHiddenInput(writer, accordion, openedPaneId);
        }else{
             super.writeHiddenInput(writer,accordion);
        }
        writer.startElement(SPAN_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, clientId + "_script");
        writer.startElement(SCRIPT_ELEM, null);
        writer.writeAttribute(INPUT_TYPE_TEXT, "text/javascript");
        StringBuilder cfg = new StringBuilder("{singleSubmit: true");

        if (null!=openedPaneId){
            cfg.append(", opened: '").append(openedPaneId).append("'");
        }
        boolean autoheight = accordion.isAutoHeight();
        int hashcode = Utils.generateHashCode(openedPaneId);
        if (null != accordion.getHashVal()){
            hashcode = Utils.generateHashCode(accordion.getHashVal());
        }
        cfg.append(", hash: ").append(hashcode);
        cfg.append(", autoHeight: ").append(autoheight);
        String fixedHeight = accordion.getFixedHeight();
        if (fixedHeight!=null && !autoheight && fixedHeight.length()>0){
            int fixedHtVal = -1;
            StringBuffer numbers = new StringBuffer();
            for(char c : fixedHeight.toCharArray()){
               if(Character.isDigit(c)) {
                   numbers.append(c);
               }
            }
            try {
               fixedHtVal = Integer.valueOf(numbers.toString());
            }   catch (NumberFormatException nfe){
                if (logger.isLoggable(Level.WARNING)){
                    logger.warning(" could not parse int value of fixedHeight="+fixedHeight);
                }
            }
            cfg.append(", fixedHeight: '").append(fixedHeight).append("'");
            if (fixedHtVal > 30) {
               cfg.append(", fHtVal: ").append(fixedHtVal);
            }
        }
        cfg.append("}");
         //just have to add behaviors if we are going to use them.
        writer.writeText("ice.mobi.accordionController.initClient('"
                + accordion.getClientId() + "'," +cfg.toString()+");");
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }

}
