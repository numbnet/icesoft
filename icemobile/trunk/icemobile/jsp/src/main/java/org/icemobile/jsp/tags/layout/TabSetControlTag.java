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

import org.icemobile.jsp.tags.*;

import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import org.icemobile.component.ITabSet;
import org.icemobile.renderkit.TabPaneCoreRenderer;
import org.icemobile.renderkit.TabSetCoreRenderer;

/**
 * This component controlls the tabPanes and ensures that only a single tab pane shown by the
 * highlighted tab is showing at any give time
 * if no id is present on the markup for this component, it will be given a default name of tabset1
 * If more than one tabset is present on a page, it is up to the developer to specify distinct id for
 * each of the tabset components on the page.
 */
public class TabSetControlTag extends BaseBodyTag implements ITabSet{
    private static final String DEFAULT_ID = "tabset1";
    private static final String DEFAULT_ORIENTATION = "top";
    private TabSetCoreRenderer renderer = new TabSetCoreRenderer();
    private TabPaneCoreRenderer paneRenderer = new TabPaneCoreRenderer();
    private static Logger LOG = Logger.getLogger(TabSetControlTag.class.getName());
    private TagWriter writer;
    private List<StringBuilder> tabLabels;
    private Map<String, StringBuilder> contents = new LinkedHashMap<String, StringBuilder>();
    private boolean parentHeader=false;
    private boolean parentFooter;
    private boolean isTop = true;
    private int index;

    public int doStartTag() throws JspTagException {
        // default orientation
        this.tabLabels = new Vector<StringBuilder>();
        if (null==this.id){
            id=DEFAULT_ID;
        }
   //     LOG.info("doStart for id="+this.id);
        if (null == orientation){
            this.orientation = DEFAULT_ORIENTATION;
        }
        numberChildren = 0;
        PagePanelTag pptag = (PagePanelTag)findAncestorWithClass(this, PagePanelTag.class);
        if (pptag!=null){
            parentHeader = pptag.isHasHeader();
    //        LOG.info("after setting parentHeader value ="+parentHeader);
        }
        try {
            if (orientation.trim().toLowerCase().equals("bottom")){
              this.isTop = false;
            }else {
                this.isTop = true;
            }
         //   LOG.info("isTop="+isTop+"  orientation="+orientation);
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer, true, isTop);
            writer.closeOffTag();
            if (isTop){
                renderer.encodeTabSetHeaders(this, writer);
            }
        } catch (IOException e) {
            throw new JspTagException("problem in doStart of TabSetTag exception="+e) ;
        }
        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws JspTagException {
        try {
            /* may not have hidden field? */
            if (isTop){
          //      LOG.info("render tabs first");
                encodeTabs();
           //     LOG.info("before end tab set header encoding");
                renderer.encodeEndTabSetHeaders(this, writer);
          //      LOG.info("after end tab set header encoding");
                encodeContents();
            }else {
        //        LOG.info("render contents first");
                encodeContents();
                renderer.encodeTabSetHeaders(this,writer);
                encodeTabs();
                renderer.encodeEndTabSetHeaders(this,writer);
            }
            renderer.encodeEnd(this, writer, true);
        } catch (IOException ioe) {
            LOG.severe("IOException closing Tabset: " + ioe);
        }
        return EVAL_PAGE;
    }
    private void encodeTabs() throws JspTagException{
     //   LOG.info("encodeTab labels");
        if (this.tabLabels.isEmpty()){
            throw new JspTagException("Cannot have empty tab labels");
        } else {
            try {
                for (StringBuilder tab: tabLabels){
             //       LOG.info("tab markup = "+tab.toString());
                    writer.write(tab.toString());
                }
            }catch(IOException ioe){
                LOG.severe(" problem writing tabLabels from vector"+ ioe.toString());
            }
        }
    }
    private void encodeContents() throws JspTagException{
        if (this.contents.isEmpty()){
           throw new JspTagException("Cannot have empty tab contents");
        }else {
            try {
              //  LOG.info("write start of contents");
                renderer.writeStartOfTabContents(this, writer, id);
          //      LOG.info("after start of Tab Contents");
                for (Map.Entry<String, StringBuilder> entry : contents.entrySet()) {
                    String key = entry.getKey();
                    StringBuilder value = entry.getValue();
                    boolean selected = false;
         //           LOG.info("***SELECTEDID="+selectedId+" key="+key);
                    if (key.equals(selectedId)){
                        selected=true;
          //              LOG.info("id="+key+" is selected");
                    }
                    paneRenderer.encodeContentWrapperBegin(this, writer, selectedId, key );
                    writer.write(value.toString());
                    renderer.writeEndOfTabContents(this, writer);
                }
                renderer.encodeEndContents(this, writer);
            } catch (Exception ioe){
                LOG.severe("IOException content wrapping of panel ");
            }
        }
    }

    public void addTabHeader(StringBuilder sb) {
        tabLabels.add(sb);
    }

    public void addContents(String paneId, StringBuilder sb){
        this.contents.put(paneId, sb);
    }

    // tag properties
    private String id;
    private String name;
    private String style;
    private String styleClass;
    private String selectedId;
    private boolean autoWidth;
    private boolean autoHeight=false;
    private String height;
    private int numberChildren;
    private String orientation;


    public int getNumberChildren() {
        return numberChildren;
    }

    public void setNumberChildren(int numberChildren) {
        this.numberChildren = numberChildren;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getClientId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setHeight(String fixedHeight) {
        this.height = fixedHeight;
    }

    public String getHeight() {
        return height;
    }

    public void incrementChildren(){
        this.numberChildren++;
    }
    public int getIndex() {
        return index;
    }
    public void setSelectedIndex(int selected){
        this.index=selected;
      //  LOG.info("set index to ="+index);
    }
    public void resetIndex() {
        index = 0;
    }
    public boolean isAutoWidth(){
        return this.autoWidth;
    }
    public void setAutoWidth(boolean autowidth){
        this.autoWidth=autowidth;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedId() {
        return this.selectedId;
    }

    /**
     * not used for jsp at this time
     * @return
     */
    public String getJavascriptFileRequestPath() {
        return null;
    }

    /**
     * not used at this time for jsp
     * @return
     */
    public boolean isScriptLoaded() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setScriptLoaded() {
        //currently not used for jsp
    }

    /**
     * not required for JSP
     * @return
     */
    public String getHashVal() {
        return "1";
    }

    public String getOrientation() {
        return orientation;
    }
    public void setOrientation(String orientation){
        this.orientation = orientation.toLowerCase().trim();
    }
    public boolean getIsTop() {
        return isTop;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean setIsTop(String orientation) {
        if (orientation.toLowerCase().trim().equals("top")){
            return false;
        }else{
            return true;
        }
    }

    public Boolean isParentFooter() {
        return this.parentFooter;
    }
    public void setParentFooter(boolean pfooter) {
        this.parentFooter = pfooter;
    }

    public Boolean isParentHeader() {
        return this.parentHeader;
    }

    /**
     * not used for JSP
     */
    public void setParentHeaderFooter() {
         //not used. only for JSF
    }

    public boolean isSingleSubmit() {
        return false;  //used to differentiate between JSP and JSF
    }

    public boolean isAutoHeight() {
        return autoHeight;
    }
    public void setAutoHeight(boolean autoHeight){
        this.autoHeight = autoHeight;
    }

    public void release(){
        this.renderer = null;
        this.paneRenderer = null;
        this.contents= null;
        this.numberChildren = 0;
        this.writer = null;
        this.tabLabels = null;
        this.orientation=null;
        this.isTop = true;
        this.styleClass = null;
        this.style=null;
    }
}