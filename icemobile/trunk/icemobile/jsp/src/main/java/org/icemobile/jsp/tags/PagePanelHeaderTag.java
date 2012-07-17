package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class PagePanelHeaderTag extends TagSupport {

    public static final String HEADER_CLASS = "mobi-pagePanel-header";

    private static Logger LOG = Logger.getLogger(PagePanelHeaderTag.class.getName());

    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        tag.append(" class=\"").append(HEADER_CLASS).append("\"");
        tag.append(">");

        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException writing PagePanelHeader: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();
        try {
            out.write(TagUtil.DIV_TAG_END);
        } catch (IOException ioe) {
            LOG.severe("IOException closing PagePanelHeader: " + ioe);
        }
        return EVAL_PAGE;
    }
}
