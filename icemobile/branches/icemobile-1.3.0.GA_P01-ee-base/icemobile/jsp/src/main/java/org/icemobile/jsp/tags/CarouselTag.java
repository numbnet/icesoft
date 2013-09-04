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
import org.icemobile.component.ICarousel;
import org.icemobile.jsp.tags.BaseBodyTag;
import org.icemobile.jsp.util.MobiJspConstants;
import org.icemobile.jsp.util.Util;
import org.icemobile.renderkit.CarouselCoreRenderer;


import javax.servlet.jsp.JspTagException;
import java.io.IOException;

import java.util.logging.Logger;


/**
 *   This component tag renders a collection of items within a carousel which is created
 *   with use of iScroll javascript libary.
 *
 */
public class CarouselTag extends BaseBodyTag implements ICarousel {

     private static final Logger logger = Logger.getLogger(CarouselTag.class.getName());

     private int selectedItem;
     private boolean disabled;
     private String nextLabel;
     private String previousLabel;
     private TagWriter writer;
     private CarouselCoreRenderer renderer;
     private int rowCount = 0;
     private String name;

     public int doStartTag() throws JspTagException {
         renderer= new CarouselCoreRenderer();
         rowCount = 0;
         try {
             writer = new TagWriter(pageContext);
             renderer.encodeIScrollLib(this, writer);
             renderer.encodeBegin(this, writer);
             writer.closeOffTag();
         } catch (IOException e) {
             throw new JspTagException("problem in doStart of CarouselTag exception="+e) ;
         }
         return EVAL_BODY_INCLUDE;
     }


     public int doEndTag() throws JspTagException {
         try {
             renderer.encodeEnd(this, writer);

         } catch (IOException ioe) {
             logger.severe("IOException closing Carousel Tag: " + ioe);
         }
         return EVAL_PAGE;
     }

     public StringBuilder getJSConfigOptions(){
         StringBuilder sb = new StringBuilder(",{key: ").append(getSelectedItem()).append("}");
         return sb;
     }


     public int getSelectedItem() {
         return selectedItem;
     }

     public void setSelectedItem(int selectedIdx) {
         this.selectedItem = selectedIdx;
     }

     /**
      *  responsible for providing the src attribute to load the iscroll library
      * @return
      */
     public String getIScrollSrc() {
         String separator = "/";
         StringBuilder sb = new StringBuilder();
         sb.append(Util.getContextRoot(pageContext.getRequest()));
         sb.append(MobiJspConstants.RESOURCE_BASE_URL).append(separator);
         sb.append(ICarousel.LIB_ISCROLL_JSP).append(separator).append(ICarousel.JS_ISCROLL);
         return  sb.toString()  ;
     }



     public String getPreviousLabel() {
         return previousLabel;
     }

     public void setPreviousLabel(String prevLabel) {
         this.previousLabel = prevLabel;
     }

     public String getClientId() {
         return id;
     }

     public String getNextLabel() {
         return nextLabel;
     }

     public void setNextLabel(String nextLabel) {
         this.nextLabel = nextLabel;
     }

     public int getRowCount() {
         return rowCount;
     }

     public void setRowCount(int rowCount) {
         this.rowCount = rowCount;
     }

     public String getName(){
         return this.name;
     }
     public void setName(String name){
         this.name=name;
     }

     public void release(){
    //     logger.info("release carousel tag");
         this.renderer= null;
         this.writer = null;
         this.id=null;
     }
 }
