package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class HeadersTag extends TagSupport {

    private static final String TABSET_HEADERS_CLASS = "mobi-tabset-tabs";
    //    private static final String ACCORDIAN_HEADERS_CLASS = "mobi-accordion";
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
//            mHeadersClass = ACCORDIAN_HEADERS_CLASS;
            mAccParent = (AccordionTag) parent;
            mSelectedIndex = mAccParent.getSelectedId();

        } else {
            throw new IllegalArgumentException("Parent of HeadersTag must be TabSet or Accordian");
        }
    }


    public boolean isTabParent() {
        return mTabParent != null;
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

            tag.append(" class=\"").append(mHeadersClass);
            if (userStyle != null && !"".equals(userStyle)) {
                tag.append(" ").append(userStyle);
            }
            tag.append("\"");

            if (style != null && !"".equals(style)) {
                tag.append(" style=\"").append(style).append("\"");
            }


            tag.append(">");
            tag.append(TagUtil.UL_TAG);
            tag.append(" data-current=\"").append(mSelectedIndex).append("\">");

            out.write(tag.toString());

        } catch (IOException ioe) {
            LOG.severe("IOException writing HeadersTag: " + ioe);
        }
    }

    public String getIndex() {
        if (mTabParent != null) {
            return mTabParent.getIndex();
        } else {
            return mAccParent.getIndex();
        }
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

    private void doEndTabHeader(Writer out) {
        try {

            // close the tabs
            out.write(TagUtil.UL_TAG_END);
            out.write(TagUtil.DIV_TAG_END);

            if (mTabParent != null) {
                mTabParent.resetIndex();
            } else {
                mAccParent.resetIndex();
            }

        } catch (IOException ioe) {
            LOG.severe("IOException closing TabHeader tag: " + ioe);
        }

    }

    private String userStyle;
    private String style;


    public String getId() {
        if (mTabParent != null) {
            return mTabParent.getId();
        } else {
            return mAccParent.getId();
        }
    }


    public String getUserStyle() {
        return userStyle;
    }

    public void setUserStyle(String userStyle) {
        this.userStyle = userStyle;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
