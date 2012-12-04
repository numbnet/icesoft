package org.icemobile.renderkit;

import org.icemobile.component.IAccordion;
import org.icemobile.component.IContentPane;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Utils;


import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class ContentPaneCoreRenderer extends BaseCoreRenderer {
    /*so far takes care of accordion, will add tabSet and contentPane as they are
      brought into core rendering strategy */
    private static final Logger logger =
            Logger.getLogger(ContentPaneCoreRenderer.class.toString());
    
    public void encodeBegin(IContentPane pane, IResponseWriter writer,
                            boolean isJsp, boolean selected)
            throws IOException{
        if (pane.isAccordionPane()){
            IAccordion accordion = pane.getAccordionParent();
            encodeAccordionHandle(accordion, pane, writer, isJsp, selected);
        }
    }

    public void encodeEnd(IContentPane component, IResponseWriter writer, boolean isJsp)
             throws IOException {
    //    if (component.isAccordionPane()){  uncomment when tabSet and contentStack use core rendering
            writer.endElement(DIV_ELEM);
            writer.endElement(DIV_ELEM);

            writer.endElement(SECTION_ELEM);
   /*     } else {
            writer.endElement(DIV_ELEM);
            writer.endElement(DIV_ELEM);
        } */
    }
    public void encodeAccordionHandle(IAccordion accordion, IContentPane pane,
                  IResponseWriter writer, boolean isJsp, boolean selected )
         throws IOException{
          String accordionId = accordion.getClientId();
          String clientId = pane.getClientId();
          boolean client = pane.isClient();
          boolean autoheight = accordion.isAutoHeight();
          String myId = pane.getId();
          String handleClass = "handle";
          String pointerClass = "pointer";
          writer.startElement(SECTION_ELEM, pane);
          writer.writeAttribute(ID_ATTR, clientId+"_sect");
             // apply default style class for panel-stack
          StringBuilder styleClass = new StringBuilder("closed");
          if (!isJsp && selected){
              styleClass = new StringBuilder("open") ;
          }
          String userDefinedClass = pane.getStyleClass();
          if (userDefinedClass != null && userDefinedClass.length() > 0){
               handleClass+= " " + userDefinedClass;
               pointerClass+=" " + userDefinedClass;
          }
          writer.writeAttribute(CLASS_ATTR, styleClass);
          writer.writeAttribute(STYLE_ATTR, pane.getStyle());
          writer.startElement(DIV_ELEM, pane);
          writer.writeAttribute(ID_ATTR, clientId+"_hndl");
          writer.writeAttribute(CLASS_ATTR, handleClass);
        StringBuilder args = new StringBuilder("ice.mobi.accordionController.toggleClient('");
        args.append(accordionId).append("', this, '").append(client).append("'");
        ClientDescriptor cd = accordion.getClient();
        if (Utils.isTransformerHack(cd)) {
            args.append(", 'true'");
        }
        args.append(");");
        writer.writeAttribute("onclick", args.toString() );

          writer.startElement(SPAN_ELEM, pane);
          writer.writeAttribute("class", pointerClass);
       /*   if (!isJsp){
              writer.write(IAccordion.ACCORDION_RIGHT_POINTING_POINTER);
          } else {
              writer.writeText(IAccordion.ACCORDION_RIGHT_POINTING_POINTER);
          }*/
          writer.endElement(SPAN_ELEM);
          String title = pane.getTitle();
          writer.writeText(title);
          writer.endElement(DIV_ELEM);
          writer.startElement(DIV_ELEM, pane);
          writer.writeAttribute(ID_ATTR, clientId+"wrp");
          String htString = accordion.getHeight();
          StringBuilder style = new StringBuilder(256);
          if (!autoheight && (null != htString) && !htString.equals("")) {
            //  style.append( "height: ").append(htString).append("; max-height: ").append(htString);
             // style.append("; overflow-y: auto;");
              style.append("overflow-y: auto; ");
          }
          if (pane.getStyle() !=null){
               style.append(pane.getStyle());
          }
   /*       if (style.length()>0){
              writer.writeAttribute(STYLE_ATTR, pane.getStyle());
          } */
          writer.startElement(DIV_ELEM, pane);
          writer.writeAttribute(ID_ATTR, clientId);
     }
}
