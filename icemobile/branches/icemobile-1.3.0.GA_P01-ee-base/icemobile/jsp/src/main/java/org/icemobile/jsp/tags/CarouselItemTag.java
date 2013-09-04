/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * Item tag provides iteration capacity within the Carousel tag.
 * Necessary since we don't have control over the children
 */
public class CarouselItemTag extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(CarouselItemTag.class.getName());

    public int doStartTag() throws JspTagException {
         // Get the enclosing parent tag.
         CarouselTag parent = (CarouselTag)findAncestorWithClass(this, CarouselTag.class);
         int count;
         // If there is no parent tag or the enclosing parent is not foreach tag
         // , throw an exception.
         if (parent == null) {
             throw new JspTagException("Error: 'item' tag must be enclosed by 'Carousel' tag.");
         }else {
             count = parent.getRowCount();
             parent.setRowCount(++count);
         }
         Writer out = pageContext.getOut();

         try {
             out.write("<li>");
         } catch (IOException ioe) {
             throw new JspTagException("IOException encoding carouselItem: " + ioe);
         }
         return BodyTagSupport.EVAL_BODY_INCLUDE;
     }

      public int doEndTag() throws JspTagException {
         Writer out = pageContext.getOut();
         try {
             out.write("</li>");
         } catch (IOException ioe) {
             throw new JspTagException("error writing end of carousel item tag: "+ioe);
         }
         return EVAL_PAGE;
      }


 }
