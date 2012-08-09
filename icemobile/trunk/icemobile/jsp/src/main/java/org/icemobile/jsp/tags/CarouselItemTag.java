/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.jsp.tags;

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

    private static Logger LOG = Logger.getLogger(CarouselItemTag.class.getName());

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
                    LOG.warning("IterationTag - index mismatch with collection size");
                }
            }

            if (collectionItr.hasNext()) {
                out.write("<li>");
                Object item = collectionItr.next();
                out.write(item.toString());
                pageContext.setAttribute("ref", item);
                out.write("</li>");
            }
        } catch (IOException ioe) {
            LOG.severe("IOException encoding carouselItem: " + ioe);
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
