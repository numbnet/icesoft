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
public class PagePanelBodyTag extends TagSupport {

    public static final String BODY_CLASS = "mobi-pagePanel-body";
    public static final String BODY_NO_HEADER_CLASS = "mobi-pagePanel-body-noheader";
    public static final String BODY_NO_FOOTER_CLASS = "mobi-pagePanel-body-nofooter";

    private static Logger LOG = Logger.getLogger(PagePanelBodyTag.class.getName());


    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        tag.append(" class=\"").append(BODY_CLASS);
        if (noHeader) {
            tag.append(" ").append(BODY_NO_HEADER_CLASS);
        }
        if (noFooter) {
            tag.append(" ").append(BODY_NO_FOOTER_CLASS);
        }
        tag.append("\"");
        tag.append(">");
        tag.append(TagUtil.DIV_TAG).append(">");

        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException writing PagePanelBody: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();
        try {
            out.write(TagUtil.DIV_TAG_END);
            out.write(TagUtil.DIV_TAG_END);
        } catch (IOException ioe) {
            LOG.severe("IOException closing PagePanelBody: " + ioe);
        }
        return EVAL_PAGE;
    }

    private boolean noHeader;
    private boolean noFooter;

    public boolean isNoHeader() {
        return noHeader;
    }

    public void setNoHeader(boolean noHeader) {
        this.noHeader = noHeader;
    }

    public boolean isNoFooter() {
        return noFooter;
    }

    public void setNoFooter(boolean noFooter) {
        this.noFooter = noFooter;
    }
}
