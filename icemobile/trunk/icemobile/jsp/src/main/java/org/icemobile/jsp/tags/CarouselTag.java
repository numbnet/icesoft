package org.icemobile.jsp.tags;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.logging.Logger;

/**
 *
 */
public class CarouselTag extends TagSupport {

    private static Logger LOG = Logger.getLogger(CarouselTag.class.getName());


    private static final String JS_ISCROLL = "iscroll.js";
    private static final String JS_ISCROLL_MIN = "iscroll-min.js";
    private static final String LIB_ISCROLL = "org.icefaces.component.util";

    private String id;
    private String name;
    private String styleClass;
    private String style;

    public static final String CAROUSEL_CLASS = "mobi-carousel ";
    public static final String CAROUSEL_SCROLLER = "mobi-carousel-scroller";
    public static final String CAROUSEL_ITEM_CLASS = "mobi-carousel-list";
    public static final String CAROUSEL_CURSOR_CLASS = "mobi-carousel-cursor ";
    public static final String CAROUSEL_CURSOR_LISTCLASS = "mobi-carousel-cursor-list";
    public static final String CAROUSEL_CURSOR_CURSOR_CENTER_CLASS = "mobi-carousel-cursor-center";


    private Collection collection;
    private int originalCollectionSize;
    private int iterationCount;
    private int selectedIndex;


    public int doStartTag() throws JspTagException {


        Writer out = pageContext.getOut();
        originalCollectionSize = collection.size();
        iterationCount = 0;

        try {

            out.write("<span id=\"" + getId() + "_jscript\">");
            out.write("<span id=\"" + getId() + "_libJS\">");  // nested span
            out.write("<script type=\"text/javascript\" src=\"resources/" +
                          LIB_ISCROLL + "/" + JS_ISCROLL + "\"></script>");
            out.write("</span>"); // end of iscroll script

            out.write("</span>"); // end of script span sections

            out.write("<span id=\"" + getId() + "\" >");

            out.write("<div id=\"" + getId() + "_carousel\"");
            String styleClass = CAROUSEL_CLASS;

            String userStyleClass = getStyleClass();
            if (userStyleClass != null && !"".equals(userStyleClass)) {
                styleClass += " " + userStyleClass;
            }

            out.write(" class=\"" + styleClass + "\">");
            out.write("<div class=\"" + CAROUSEL_SCROLLER + "\">");
            out.write("<ul class=\"" + CAROUSEL_ITEM_CLASS + "\">");

        } catch (IOException ioe) {
            LOG.severe("IOException creating carousel tag: " + ioe);
        }

        // If we're done, skip the content
        if (collection == null || collection.isEmpty() || iterationCount >= collection.size()) {
            return BodyTagSupport.SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doAfterBody() throws JspTagException {

        // The last test
        // below prevents infinite recursion if the carousel Tag has no item child
        // defined (which  you can't catch in the tld)
        iterationCount++;
        if (collection == null || collection.isEmpty() || iterationCount >= originalCollectionSize) {
            return BodyTagSupport.SKIP_BODY;
        }
        return EVAL_BODY_AGAIN;
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();
        try {

            out.write(TagUtil.UL_TAG_END);  // surrounding items
            out.write(TagUtil.DIV_TAG_END); // carousel-scroller div
            out.write(TagUtil.DIV_TAG_END);

            //now do the paginator for the carousel
            StringBuilder builder = new StringBuilder(TagUtil.DIV_TAG);
            builder.append(" id=\"").append(getId()).append("_list").append("\">");
            out.write(builder.toString());

            builder = new StringBuilder(TagUtil.DIV_TAG);
            builder.append(" class=\"").append(CAROUSEL_CURSOR_CLASS).append("\">");
            out.write(builder.toString());

            builder = new StringBuilder(TagUtil.DIV_TAG);
            builder.append(" class=\"").append(CAROUSEL_CURSOR_CURSOR_CENTER_CLASS).append("\">");
            out.write(builder.toString());

            builder = new StringBuilder(TagUtil.UL_TAG);
            builder.append(" class=\"").append(CAROUSEL_CURSOR_LISTCLASS).append("\">");
            out.write(builder.toString());

            int size = originalCollectionSize;
            if (selectedIndex > size - 1 || selectedIndex < 0) {
                selectedIndex = 0;
            }
            for (int i = 0; i < size; i++) {
                out.write(TagUtil.LI_TAG);

                if (selectedIndex == i) {
                    out.write(" class=\"active\"");
                }
                out.write(">");
                out.write(String.valueOf(i + 1));

                out.write(TagUtil.LI_TAG_END);
            }
            //do the list of dots for pagination
            out.write(TagUtil.UL_TAG_END);
            out.write(TagUtil.DIV_TAG_END);
            out.write(TagUtil.DIV_TAG_END);

            this.encodeHiddenSelected(out, getName());

            out.write(TagUtil.DIV_TAG_END);
            out.write(TagUtil.SPAN_TAG_END);

            renderScript(out, getName());

        } catch (IOException ioe) {
            LOG.severe("IOException closing Carousel Tag: " + ioe);
        }
        return SKIP_BODY;
    }


    private void encodeHiddenSelected(Writer out, String id) throws
        IOException {
        out.write("<input");
        out.write(" id = \"" + id + "_hidden\"");
        if (name != null && !"".equals(name)) {
            out.write(" name=\"" + name + "\"");
        } else {
            LOG.warning("CarouselTag (id: " + id + ") has no name attribute for value submission");
        }
        out.write(" type=\"hidden\"");
        out.write(" value=\"" + String.valueOf(selectedIndex) + "\">");
        out.write("</input>");
    }

    private void renderScript(Writer out, String clientId) throws IOException {

        out.write(TagUtil.SCRIPT_TAG);
        StringBuilder builder = new StringBuilder();
//
        builder.append(" id=\"").append(clientId).append("_script").append("\"");
        builder.append(" type=\"text/javascript\">");
        out.write(builder.toString());
        out.write("supportsOrientationChange = 'onorientationchange' in window," +
                      "orientationEvent = supportsOrientationChange ? 'orientationchange' : 'resize';\n");

        out.write("window.addEventListener(orientationEvent, function() {" +

                      "  setTimeout(function () { " +
                      "       ice.mobi.carousel.refresh('" + clientId + "');" +
                      "  \n}, 100); " +
                      " }, false);\n");

        out.write("ice.mobi.carousel.loaded('" + clientId + "');");
        out.write("</script>");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIdx) {
        this.selectedIndex = selectedIdx;
    }

    /**
     * Allow iterating item tag to fetch current rendering index
     *
     * @return
     */
    int getIterationCount() {
        return iterationCount;
    }
}
