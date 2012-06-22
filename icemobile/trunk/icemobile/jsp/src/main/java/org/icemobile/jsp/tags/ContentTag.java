package org.icemobile.jsp.tags;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class ContentTag extends TagSupport {

    private boolean isActive;

    private static final String MOBI_TABSET_CONTENT_CLASS = "mobi-tabset-content";
    private static final String CONTENT_WRAPPER_CLASS = ".mobi-panel-stack";


    private static Logger LOG = Logger.getLogger(ContentTag.class.getName());

    private TabSetTag mTabParent;
    private AccordianTag mAccParent;

    private String mContentClass;
    private String mSelectedItem;


    public void setParent(Tag parent) {

        if (parent instanceof TabSetTag) {
            mContentClass = MOBI_TABSET_CONTENT_CLASS;
            mTabParent = (TabSetTag) parent;
            mSelectedItem = mTabParent.getSelectedTab();
        } else if (parent instanceof AccordianTag) {
            mContentClass = CONTENT_WRAPPER_CLASS;
            mAccParent = (AccordianTag) parent;
        } else {
            throw new IllegalArgumentException("ContentTag must have TabSet or Accordian parent");
        }
    }


    public boolean isTabParent() {
        return mTabParent != null;
    }

    public int doStartTag() {

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        Writer out = pageContext.getOut();

        if (isTabParent()) {
            tag.append(" id=\"").append( mTabParent.getId() ).append("_tabContent").append("\"");
        } else {
            // write out accordian id modifier
        }
        tag.append(" class=\"").append(mContentClass).append("\"");


        tag.append(">");

        try {

            out.write( tag.toString() );

        } catch (IOException ioe) {
            LOG.severe("IOException starting ContentTag: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() {

        Writer out = pageContext.getOut();
        try {
            out.write(TagUtil.DIV_TAG_END);
        } catch (IOException ioe) {
            LOG.severe("IOException closing content tag: " + ioe);
        }
        return EVAL_PAGE;
    }

    public String getSelectedItem() {
        return mSelectedItem;
    }

    public String getIndex() {
        if (mTabParent != null) {
            return mTabParent.getIndex();
        } else {
            return mAccParent.getSelectedId();
        }
    }

    public String getId() {
        if (mTabParent != null) {
            return mTabParent.getId();
        } else {
            return mAccParent.getId();
        }
    }
}
