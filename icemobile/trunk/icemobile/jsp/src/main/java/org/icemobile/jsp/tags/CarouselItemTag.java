package org.icemobile.jsp.tags;

import org.icemobile.jsp.tags.CarouselTag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Item tag provides iteration capacity within the Carousel tag.
 * Necessary since we don't have control over the children
 */
public class CarouselItemTag extends BodyTagSupport {

    private static Logger logger = Logger.getLogger(CarouselItemTag.class.getName());

    private String ref;
    private String type;


    public int doStartTag() throws JspTagException {

        // Get the enclosing parent tag.
        Tag parentTag = this.getParent();

        // If there is no parent tag or the enclosing parent is not foreach tag
        // , throw an exception.
        if (parentTag == null || !(parentTag instanceof CarouselTag)) {
            throw new JspTagException("Error: 'item' tag must be enclosed by 'Carousel' tag.");
        }

        Writer out = pageContext.getOut();
        Collection collection = ((CarouselTag) parentTag).getCollection();
        int iterationCount = ((CarouselTag) parentTag).getIterationCount();
        Iterator collectionItr = collection.iterator();

        try {


            for (int idx = 0; idx < iterationCount; idx++) {
                if (collectionItr.hasNext()) {
                    collectionItr.next();
                } else {
                    logger.warning("IterationTag - index mismatch with collection size");
                }
            }


            if (collectionItr.hasNext()) {

                out.write("<li>");
                // Pop the first item out of the collection.
                Object item = collectionItr.next();
                out.write(item.toString());
                pageContext.setAttribute("ref", item);
                out.write("</li>");

            }


        } catch (IOException ioe) {

        }
        return BodyTagSupport.EVAL_BODY_INCLUDE;
    }


    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
