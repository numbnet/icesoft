package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 */
public class ListItemTag extends TagSupport {

    public static final String OUTPUTLISTITEM_CLASS = "mobi-list-item";
    public static final String OUTPUTLISTGROUP_CLASS = "mobi-list-item mobi-list-item-group";
    public static final String OUTPUTLISTITEMDEFAULT_CLASS = "mobi-list-item-default ";

    private static Logger LOG = Logger.getLogger(OutputListTag.class.getName());


    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();
        StringBuilder tag = new StringBuilder(TagUtil.LI_TAG);

        String builtinStyle = OUTPUTLISTITEM_CLASS;

        if (isGroup()) {
            builtinStyle = OUTPUTLISTGROUP_CLASS;
        }
        tag.append(" class=\"").append(builtinStyle);
        if (styleClass != null && !"".equals(styleClass)) {
            tag.append(" ").append(styleClass);
        }
        tag.append("\">");

        tag.append(TagUtil.DIV_TAG);
        tag.append(" class=\"").append(OUTPUTLISTITEMDEFAULT_CLASS).append("\">");
        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException starting ListItemTag: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();

        try {
            out.write(TagUtil.DIV_TAG_END);
            out.write(TagUtil.LI_TAG_END);
        } catch (IOException ioe) {
            LOG.severe("IOException closing ListItemTag: " + ioe);
        }
        return EVAL_PAGE;
    }

    private String styleClass;
    private String style;
    private boolean group;

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }
}
