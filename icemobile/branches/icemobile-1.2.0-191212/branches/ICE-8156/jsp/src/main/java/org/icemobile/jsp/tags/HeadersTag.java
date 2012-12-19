package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * HeadersTag opens/closes the orderedList for TabSet Headers. We can't do this
 * with a single TabSetTag. Header structure is not used in Accordion structure
 */
public class HeadersTag extends TagSupport {

    private static final String TABSET_HEADERS_CLASS = "mobi-tabset-tabs";
    private static Logger LOG = Logger.getLogger(HeadersTag.class.getName());

    private TabSetTag mTabParent;
    private AccordionTag mAccParent;

    private String mHeadersClass;
    private String mSelectedIndex;

    public void setParent(Tag parent) {

        if (parent instanceof TabSetTag) {
            mHeadersClass = TABSET_HEADERS_CLASS;
            mTabParent = (TabSetTag) parent;
            mSelectedIndex = mTabParent.getSelectedTab();

        } else if (parent instanceof AccordionTag) {
            throw new UnsupportedOperationException("AccordionTag doesn't support(require) HeadersTag");
        } else {
            throw new IllegalArgumentException("Parent of HeadersTag must be TabSet or Accordian");
        }
    }


    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();
        if (mTabParent != null) {
            writeTabHeaders(out);
        }
        return EVAL_BODY_INCLUDE;
    }

    private void writeTabHeaders(Writer out) {

        try {

            StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
            if (id != null && !"".equals(id)) {
                tag.append(" id=\"").append(getId()).append("_tabs\"");
            }

            tag.append(" class=\"").append(mHeadersClass).append("\">");
            tag.append(TagUtil.UL_TAG);
            tag.append(" data-current=\"").append(mSelectedIndex).append("\">");

            out.write(tag.toString());

        } catch (IOException ioe) {
            LOG.severe("IOException writing HeadersTag: " + ioe);
        }
    }

    private void doEndTabHeader(Writer out) {

        try {
            out.write(TagUtil.UL_TAG_END);
            out.write(TagUtil.DIV_TAG_END);

            if (mTabParent != null) {
                mTabParent.resetIndex();
            }

        } catch (IOException ioe) {
            LOG.severe("IOException closing TabHeader tag: " + ioe);
        }

    }

    public String getIndex() {
        if (mTabParent != null) {
            return mTabParent.getIndex();
        }
        throw new UnsupportedOperationException("getIndex() should not be called from Accordion");
    }

    public String getSelectedItem() {

        if (isTabParent()) {
            return mTabParent.getSelectedTab();
        } else {
            return mAccParent.getSelectedId();
        }
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();
        if (mTabParent != null) {
            doEndTabHeader(out);
        }
        return EVAL_PAGE;

    }

    public boolean isTabParent() {
        return mTabParent != null;
    }

    public String getId() {
        if (mTabParent != null) {
            return mTabParent.getId();
        } else {
            return mAccParent.getId();
        }
    }
}
