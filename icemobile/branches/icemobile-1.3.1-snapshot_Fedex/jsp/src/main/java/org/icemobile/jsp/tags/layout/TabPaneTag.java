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

package org.icemobile.jsp.tags.layout;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.icemobile.component.ITabPane;
import org.icemobile.component.ITabSet;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.TabPaneCoreRenderer;
import org.icemobile.util.ClientDescriptor;

public class TabPaneTag extends BodyTagSupport implements ITabPane {

    private static Logger LOG = Logger.getLogger(TabPaneTag.class.getName());
    
    private String name;
    private TabPaneCoreRenderer renderer;
    private TagWriter writer;
    private String title;
    private String mActiveContentClass = ITabSet.TABSET_ACTIVE_CONTENT_CLASS;
    private String mPassiveContentClass = ITabSet.TABSET_HIDDEN_PAGECLASS;
    private TabSetTag mParent;
    private boolean amSelected = false;
    private int index;
    protected String id;
    private String parentId;
    protected boolean disabled;
    protected String style;
    protected String styleClass;
    // Values for tabset inherited content
    private String mMyIndex;
  //  private boolean client;

    public void setParent(Tag parent) {
        if (!(parent instanceof TabSetTag)) {
            throw new IllegalArgumentException("TabPane must be child of TabSetTag");
        }
        else {
          //  LOG.info("FOUND PARENT FOR TabPane");
        }
        mParent = (TabSetTag) parent;
    }

    public int doStartTag() throws JspException {
        int newIndex = mParent.getNumberChildren();
        this.parentId = mParent.getId();
        if (null==parentId){
            throw new JspTagException("This component requires an id to be set for the tabSet tag parent");
        }
        String selId = mParent.getSelectedId();
     //   LOG.info("doStart for pane id="+id+" number children="+newIndex);
        if (selId==null){
        //    LOG.info(" have no selectedId so just set first one to selected");
            if (newIndex==0){
                amSelected= true;
                this.setIndex(0);
                mParent.setSelectedIndex(0);
                mParent.setSelectedId(id);
            }
        }
        else if (selId.equals(id)){
            amSelected=true;
            this.setIndex(newIndex);
            mParent.setSelectedIndex(newIndex);
            mParent.setSelectedId(id);
        } else {
            amSelected=false;
            this.setIndex(newIndex);
        }
     //   LOG.info("id="+id+" selectedId="+ selId+" amSelected="+amSelected+" index ="+this.getIndex());
        renderer = new TabPaneCoreRenderer();
        mParent.incrementChildren();
            try {
                StringBuilder tabPaneHeader = renderer.encodeBegin(this, writer, true, mParent.getId());
      //     LOG.info(" tabPaneHeader markup="+tabPaneHeader.toString());
                mParent.addTabHeader(tabPaneHeader);
            } catch (Exception e) {
                throw new JspTagException("problem in doStart of TabPaneTag exception="+e) ;
            }
        return EVAL_BODY_BUFFERED;
        }

    public int doAfterBody() throws JspException {
            BodyContent body = this.getBodyContent();
            try {
               if (body!=null){
                   StringBuilder sb = new StringBuilder(body.getString());
                   mParent.addContents(getClientId(), sb);
               }
               bodyContent.clear();
            } catch (Exception e) {
               throw new JspException("ERROR in "
                   + this.getClass().getName()
                   + "for id="+this.getClientId()
                   + " getting tabSet content: (" + e.getClass()
                   + "): " + e);
            }
        return SKIP_BODY;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StringBuilder getContents() {
        return null;
    }
    public int getIndex(){
        return this.index;
    }
    public void setIndex(int index){
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isClient(){
        return false;
    }
    public ClientDescriptor getClient(){
       return ClientDescriptor.getInstance(getRequest());
    }
    public String getClientId(){
        return this.id;
    }
    protected HttpServletRequest getRequest(){
        return (HttpServletRequest)pageContext.getRequest();
    }
    public void release(){
        this.disabled = false;
        this.styleClass= null;
        this.style = null;
        this.id= null;
        this.renderer = null;
        this.writer = null;
    }

}