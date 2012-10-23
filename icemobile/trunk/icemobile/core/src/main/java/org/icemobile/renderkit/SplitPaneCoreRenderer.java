package org.icemobile.renderkit;

import java.io.IOException;

import org.icemobile.component.IFragment;
import org.icemobile.component.ISplitPane;

import java.util.logging.Logger;
import static org.icemobile.util.HTML.*;

public class SplitPaneCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(SplitPaneCoreRenderer.class.toString());
    private String leftwidth;
    private String rightwidth;
    private String paneClass = ISplitPane.SPLITPANE_SCROLLABLE_CSS; //default
    private String spltClass = ISplitPane.SPLITPANE_DIVIDER_CSS;

    public void encodeBegin(ISplitPane component, IResponseWriter writer)
            throws IOException {
        StringBuilder baseClass = new StringBuilder(ISplitPane.SPLITPANE_BASE_CSS);
        StringBuilder panelClass = new StringBuilder(ISplitPane.SPLITPANE_SCROLLABLE_CSS) ;
        StringBuilder splitClass = new StringBuilder(ISplitPane.SPLITPANE_DIVIDER_CSS) ;
        if (!component.isScrollable()) {
            panelClass = new StringBuilder(ISplitPane.SPLITPANE_NONSCROLL_CSS) ;
        }
        int leftWidth = component.getColumnDivider();
        int rightWidth = 100- leftWidth;
        this.setLeftwidth(String.valueOf(leftWidth)+ "%;");
        this.setRightwidth(String.valueOf(rightWidth) + "%;");
        String userClass = component.getStyleClass();
        if (userClass!=null){
            baseClass.append(" ").append(userClass) ;
            this.setPaneClass(panelClass.append(" ").append(userClass).toString());
            this.setSpltClass(splitClass.append(" ").append(userClass).toString());
        }
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, component.getClientId()+"_wrp");
        writeStandardLayoutAttributes(writer, component, baseClass.toString() );
    }
    public void encodePane(IFragment component, IResponseWriter writer, String style)
        throws IOException {
        logger.info("printing pane with id="+component.getClientId()+" style="+style);
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, component.getClientId());
        writer.writeAttribute(CLASS_ATTR, this.getPaneClass());
        writer.writeAttribute(STYLE_ATTR, style);
    }
    public void encodePaneEnd(IResponseWriter writer)
        throws IOException{
        writer.endElement(DIV_ELEM);
    }
    public void encodePane(ISplitPane component, IResponseWriter writer, String side)
        throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(CLASS_ATTR, this.getPaneClass());
        writer.writeAttribute(ID_ATTR, component.getClientId()+"_"+side);
        String width = this.getLeftwidth();
        if (side.equals("right")){
           width = this.getRightwidth();
        }
        writer.writeAttribute(STYLE_ATTR, width);
    }

    public void encodeColumnDivider(ISplitPane component, IResponseWriter writer)
        throws IOException {
   //     writer.endElement(DIV_ELEM);  //end of left side
            /* column Divider */
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, component.getClientId()+"_splt");
        writer.writeAttribute(CLASS_ATTR, this.getSpltClass());
        writer.endElement(DIV_ELEM);
    }

    public void encodeEnd(ISplitPane pane, IResponseWriter writer)
            throws IOException{
        writer.startElement(SPAN_ELEM, pane);
        writer.startElement(SCRIPT_ELEM, pane);
        writer.writeAttribute("text", "text/javascript");
        StringBuilder sb = new StringBuilder("ice.mobi.splitpane.initClient('").append(pane.getClientId()).append("'");
        sb.append(",{ scrollable: '").append(pane.isScrollable()).append("'");
     //   sb.append(", resize: ").append(pane.isResizable());
        int width = pane.getColumnDivider();
        sb.append(",width: '").append(width).append("'");
        sb.append("});");
        writer.writeText(sb.toString());
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
        writer.endElement(DIV_ELEM);
    }

    public String getLeftwidth() {
        if (this.leftwidth != null){
            return leftwidth;}
        else {
            return "30%"; //default value
        }
    }

    public void setLeftwidth(String leftwidth) {
        this.leftwidth = leftwidth;
    }

    public String getRightwidth() {
        if (this.rightwidth !=null){
            return rightwidth;
        } else {
            return "65%";
        }
    }

    public void setRightwidth(String rightwidth) {
        this.rightwidth = rightwidth;
    }

    public String getPaneClass() {
        return paneClass;
    }

    public void setPaneClass(String paneClass) {
        this.paneClass = paneClass;
    }

    public String getSpltClass() {
        return spltClass;
    }

    public void setSpltClass(String spltClass) {
        this.spltClass = spltClass;
    }
}
