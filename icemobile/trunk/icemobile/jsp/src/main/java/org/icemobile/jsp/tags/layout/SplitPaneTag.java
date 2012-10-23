package org.icemobile.jsp.tags.layout;

import org.icemobile.component.ISplitPane;
import org.icemobile.renderkit.SplitPaneCoreRenderer;
import org.icemobile.jsp.tags.BaseBodyTag;
import org.icemobile.jsp.tags.TagWriter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

/**
     *
     */
public class SplitPaneTag extends BaseBodyTag implements ISplitPane {

    private static final Logger logger = Logger.getLogger(SplitPaneTag.class.getName());

    private int columnDivider;
    private boolean scrollable;
    private TagWriter writer;
    private SplitPaneCoreRenderer renderer;
    private String leftStyle;
    private String rightStyle;
    private String paneClass; //for the child fragments


    public void doInitBody() throws JspException {
        //prepare for evaulation of the body just before the first body evaulation - no return action
            /* check for attributes set */
        //init default values?
        logger.info("doInitBody SplitPane");
    }

    private void calculatePanelWidths() {
        leftStyle = "'width:" + String.valueOf(columnDivider)+"%'";
        rightStyle = "'width:"+String.valueOf(100-columnDivider) + "%'";
        //  this.setLeftStyle("style='width:" + String.valueOf(columnDivider)+"%'");
        paneClass = ISplitPane.SPLITPANE_SCROLLABLE_CSS;
        if (!scrollable){
            paneClass = ISplitPane.SPLITPANE_NONSCROLL_CSS;
        }
        if (getStyleClass()!=null){
            paneClass += " "+getStyleClass();
        }
        if (getStyle()!=null){
            leftStyle+= ", "+getStyle();
            rightStyle+= ", "+getStyle();
        }
    }

    public int doStartTag() {
        /* put class here */
        logger.info("doStartTag spiltPane leftstyle="+this.getLeftStyle()+" rightstyle="+this.getRightStyle()) ;
        calculatePanelWidths();
	    renderer= getRenderer();
        try {
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer);
            writer.closeOffTag();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
    * @return
    */
    public int doEndTag() {
        renderer= getRenderer();
        try {
            this.renderer.encodeEnd(this, writer);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return EVAL_PAGE;
    }
    public void setColumnDivider(int columnDivider){
        this.columnDivider = columnDivider;
    }
    public int getColumnDivider(){
        if (columnDivider == Integer.MIN_VALUE){
            return 30;
        } else {
            return columnDivider;
        }
    }
    public void setScrollable(boolean scrollable){
        this.scrollable = scrollable;
    }
    public boolean isScrollable(){
        if (this.scrollable){
            return true;
        } else {
            return false;
        }
    }
    public String getClientId(){
                return this.id;
    }

    public SplitPaneCoreRenderer getRenderer() {
        return new SplitPaneCoreRenderer();
    }
    public void setLeftStyle(String leftstyle){
        this.leftStyle = leftstyle;
    }

    public void setRightStyle(String rightstyle) {
        this.rightStyle = rightstyle;
    }

    public String getLeftStyle(){
        return this.leftStyle;
    }

    public String getRightStyle(){
        return this.rightStyle;
    }

    public String getPaneClass() {
        return paneClass;
    }

    public void release() {
		super.release();
		id = null;
		style = null;
		styleClass = null;
        renderer = null;
		//scrollable = null;
		//columnDivider = null;
	}
}