package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class PagePanelFooterTag extends TagSupport {

    public static final String FOOTER_CLASS = "mobi-pagePanel-footer";

    private static Logger LOG = Logger.getLogger(PagePanelFooterTag.class.getName());

    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        tag.append(" id=\"").append(getId()).append("_pgPnlFtr\"");
        tag.append(" class=\"").append(FOOTER_CLASS).append("\"");
        tag.append(">");

        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException writing PagePanelFooter: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();
        try {
            out.write(TagUtil.DIV_TAG_END);
        } catch (IOException ioe) {
            LOG.severe("IOException closing PagePanelFooter: " + ioe);
        }
        return EVAL_PAGE;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
