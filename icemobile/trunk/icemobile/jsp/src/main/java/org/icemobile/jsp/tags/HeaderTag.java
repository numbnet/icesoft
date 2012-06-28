package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * The header tag writes the
 */
public class HeaderTag extends TagSupport {

    private final String ACTIVE_TAB_CLASS = "activeTab";

    private HeadersTag mParent;
    private String height;
    private static Logger LOG = Logger.getLogger(HeaderTag.class.getName());
    private String mMyIndex;

    public void setParent(Tag parent) {
        if (!(parent instanceof HeadersTag)) {
            throw new IllegalArgumentException("Header must be child of HeadersTag");
        }
        mParent = (HeadersTag) parent;
        mMyIndex = mParent.getIndex();
    }


    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();
        if (mParent.isTabParent()) {
            writeTabHeader(out);
        } else {
            throw new JspTagException("AccordionTag doesn't use HeaderTag");
        }
        return EVAL_BODY_INCLUDE;
    }


    private void writeTabHeader(Writer out) {
        StringBuilder tag = new StringBuilder(TagUtil.LI_TAG);
        tag.append(" id=\"").append(mParent.getId()).append("tab_").append(mMyIndex).append("\"");

        String selectedItem = mParent.getSelectedItem();
        if (mMyIndex.equals(selectedItem)) {
            tag.append(" class=\"").append(ACTIVE_TAB_CLASS).append("\"");
        }

        tag.append(" onclick=\"").append(getTabsetJavascriptForItem(mMyIndex)).append("\"");
        tag.append(">");
        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException writing header");
        }
    }

    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();

        try {
            if (mParent.isTabParent()) {
                out.write(TagUtil.LI_TAG_END);
            }

        } catch (IOException ioe) {
            LOG.severe("IOException writing header end");
        }
        return EVAL_PAGE;
    }

    /**
     * This is from the encode tabs method showing various tabs given a click on
     * a tab.
     */
    public String getTabsetJavascriptForItem(String tabIndex) {

        StringBuilder sb = new StringBuilder("ice.mobi.tabsetController.showContent('").append(mParent.getId());
        sb.append("', this, ").append("{");
        sb.append("tIndex: ").append(tabIndex);
        if (null != height) {
            sb.append(",height: ").append(height);
        }
        sb.append("} );");
        return sb.toString();
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
