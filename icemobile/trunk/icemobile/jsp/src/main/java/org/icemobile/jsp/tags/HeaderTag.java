package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *  The header tag writes the
 */
public class HeaderTag extends TagSupport {

    private final String ACTIVE_TAB_CLASS = "activeTab";

    private HeadersTag mParent;
    private String height;
    private static Logger LOG = Logger.getLogger(HeaderTag.class.getName());
    private String mMyIndex;

    public void setParent(Tag parent) {
        if (! (parent instanceof HeadersTag)) {
            throw new IllegalArgumentException("Header must be child of HeadersTag");
        }
        mParent = (HeadersTag) parent;
        mMyIndex = mParent.getIndex();
    }


    public int doStartTag() {

        Writer out = pageContext.getOut();
        StringBuilder tag = new StringBuilder(TagUtil.LI_TAG);
        tag.append(" id=\"").append(mParent.getId()).append("tab_").append( mMyIndex ).append("\"");

        String selectedItem = mParent.getSelectedItem();
        if (mMyIndex.equals(selectedItem)) {
            tag.append(" class=\"").append(ACTIVE_TAB_CLASS).append("\"");
        }

        if (mParent.isTabParent()) {
            tag.append(" onclick=\"").append( getTabsetJavascriptForItem( mMyIndex )).append("\"");
        } else {
            tag.append(" onclick='").append( getAccordianJavascriptForItem( mMyIndex )).append("'");
        }

        tag.append(">");
        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException writing header");
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();

        try {
            out.write(TagUtil.LI_TAG_END);

        } catch (IOException ioe) {
            LOG.severe("IOException writing header end");
        }
        return EVAL_PAGE;
    }

    /**
     * This is from the encode tabs method showing various tabs given a click on
     * a tab.
     */
    public String getTabsetJavascriptForItem( String tabIndex ) {

        StringBuilder sb = new StringBuilder("ice.mobi.tabsetController.showContent('").append( mParent.getId() );
        sb.append("', this, ").append("{");
        sb.append("tIndex: ").append(tabIndex);
        if (null != height){
            sb.append(",height: ").append(height);
        }
        sb.append("} );");
        return sb.toString();
    }

    public String getAccordianJavascriptForItem (String  accordianId) {

//        logger.info("looking for index="+activeIndex+" selectedPane to open ="+openPane.getId());
//        selectedPanel is now set
//        if (openPane != null) {
//            writer.writeAttribute("data-opened", openPane.getClientId(facesContext), null);
//        }

        return "";
    }


    private String getIndex() {
        return mParent.getIndex();
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
