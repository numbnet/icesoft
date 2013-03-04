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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import org.icemobile.component.IColumn;
import org.icemobile.component.IDataTable;
import org.icemobile.model.ArrayDataModel;
import org.icemobile.model.DataModel;
import org.icemobile.model.ListDataModel;
import org.icemobile.renderkit.DataTableCoreRenderer;
import org.icemobile.util.ClientDescriptor;


/**
 * Tag class for the JSP Mobile DataTable
 *
 */
public class DataTableTag extends BaseBodyTag implements IDataTable, IterationTag {
    
    private static final long serialVersionUID = 4097584394168049435L;

    private static Logger LOG = Logger.getLogger(DataTableTag.class.getName());
    
    private String var;
    private Integer tabIndex;
    private int rowCount;
    private int rows;
    private String emptyMessage;
    private boolean paginator;
    private int page;
    private Object value;
    private boolean lazy;
    private int first;
    private String detailOrientation;
    private String largeViewDetailOrientation;
    @SuppressWarnings("rawtypes")
    private DataModel model;
    private List<IColumn> columns = new ArrayList<IColumn>();
    private Object oldVar;
    private DataTableCoreRenderer renderer;
    private boolean headersRendered = false;
    private String orientation = null;
    private boolean clientSide = true;
    private String renderedDetailView = null;
    
    public DataTableTag(){
        init();
    }
    
    private void init(){
        LOG.warning("init");
        this.columns.clear();
        this.detailOrientation = null;
        this.emptyMessage = null;
        this.first = -1;
        this.largeViewDetailOrientation = null;
        this.lazy = false;
        this.model = null;
        this.page = -1;
        this.paginator = false;
        this.rowCount = 0;
        this.rows = 0;
        this.tabIndex = null;
        this.value = null;
        this.var = null;
        this.headersRendered = false;
        this.renderer = null;
        this.oldVar = null;
        this.clientSide = true;
        this.renderedDetailView = null;
    }
    
    /** @see org.icemobile.component.IDataTable#setVar(String) */
    public void setVar(String var) {
        this.var = var;
    }

    /** @see org.icemobile.component.IDataTable#getVar() */
    public String getVar() {
        return var;
    }

    /** @see org.icemobile.component.IDataTable#setStyleClass(String) */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /** @see org.icemobile.component.IDataTable#getStyleClass() */
    public String getStyleClass() {
        return styleClass;
    }

    /** @see org.icemobile.component.IDataTable#setTabIndex(Integer) */
    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    /** @see org.icemobile.component.IDataTable#getTabIndex() */
    public Integer getTabIndex() {
        return tabIndex;
    }

    /** @see org.icemobile.component.IDataTable#setRowCount(int) */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /** @see org.icemobile.component.IDataTable#getRowCount() */
    public int getRowCount() {
        return this.rowCount;
    }

    /** @see org.icemobile.component.IDataTable#setRows(int) */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /** @see org.icemobile.component.IDataTable#getRows() */
    public int getRows() {
        return rows;
    }

    /** @see org.icemobile.component.IDataTable#setEmptyMessage(String) */
    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    /** @see org.icemobile.component.IDataTable#setEmptyMessage(String) */
    public String getEmptyMessage() {
        return emptyMessage;
    }

    /** @see org.icemobile.component.IDataTable#isPaginator() */
    public boolean isPaginator() {
        return paginator;
    }

    /** @see org.icemobile.component.IDataTable#setPaginator(boolean) */
    public void setPaginator(boolean paginator) {
        this.paginator = paginator;
    }

    /** @see org.icemobile.component.IDataTable#getPage() */
    public int getPage() {
        return page;
    }

    /** @see org.icemobile.component.IDataTable#setPage(int) */
    public void setPage(int page) {
        this.page = page;
    }

    /** @see org.icemobile.component.IDataTable#getRowIndex() */
    public int getRowIndex() {
        return getDataModel().getRowIndex();
    }

    /**
     * Set the zero-relative index of the currently selected row.
     */
    public void setRowIndex(int index){
        LOG.warning("setRowIndex("+index+")");
        getDataModel().setRowIndex(index);
        
        // if rowIndex is -1, clear the cache
        if (index == -1) {
            model = null;
        }
        else if (isRowAvailable()){
            exposeRowVariable();
        }
    }
    
    protected void exposeRowVariable(){
        
        String var = getVar();
        if (var != null) {
            if (getDataModel().getRowIndex() == -1) {
                oldVar = pageContext.getAttribute(var);
                pageContext.removeAttribute(var);
            } else if (isRowAvailable()) {
                Object rowData = getRowData();
                LOG.info("setting row " + getDataModel().getRowIndex()  + " data '" + var + "'=" + rowData);
                pageContext.setAttribute(var, rowData);
            } else {
                pageContext.removeAttribute(var);
                if (null != oldVar) {
                    pageContext.setAttribute(var, oldVar);
                    oldVar = null;
                }
            }
        }
    }
    
    /** @see org.icemobile.component.IDataTable#getValue() */
    public Object getValue() {
        return value;
    }

    /** @see org.icemobile.component.IDataTable#setValue(Object) */
    public void setValue(Object value) {
        this.model = null;
        this.value = value;
    }

    /** @see org.icemobile.component.IDataTable#isLazy() */
    public boolean isLazy() {
        return lazy;
    }

    /** @see org.icemobile.component.IDataTable#setLazy(boolean) */
    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    /** @see org.icemobile.component.IDataTable#getFirst() */
    public int getFirst() {
        return first;
    }

    /** @see org.icemobile.component.IDataTable#setFirst(int) */
    public void setFirst(int first) {
        this.first = first;
    }

    /** @see org.icemobile.component.IDataTable#getDetailOrientation() */
    public String getDetailOrientation() {
        return detailOrientation;
    }

    /** @see org.icemobile.component.IDataTable#setDetailOrientation(String) */
    public void setDetailOrientation(String detailOrientation) {
        this.detailOrientation = detailOrientation;
    }

    /** @see org.icemobile.component.IDataTable#getLargeViewDetailOrientation() */
    public String getLargeViewDetailOrientation() {
        return largeViewDetailOrientation;
    }

    /** @see org.icemobile.component.IDataTable#setLargeViewDetailOrientation(String) */
    public void setLargeViewDetailOrientation(String largeViewDetailOrientation) {
        this.largeViewDetailOrientation = largeViewDetailOrientation;
    }

    /** @see org.icemobile.component.IDataTable#getRowData() */
    public Object getRowData() {
        return (getDataModel().getRowData());
    }

    /** @see org.icemobile.component.IDataTable#isRowAvailable() */
    public boolean isRowAvailable() {
        boolean avail = this.getDataModel().isRowAvailable();
        LOG.warning("" + avail);
        return avail;
    }

    /** @see org.icemobile.component.IDataTable#getColumns() */
    public List<IColumn> getColumns() {
        return columns;
    }
    
    /**
     * <p>Return the internal {@link DataModel} object representing the data
     * objects that we will iterate over in this component's rendering.</p>
     * <p/>
     * <p>If the model has been cached by a previous call to {@link
     * #setDataModel}, return it.  Otherwise call {@link #getValue}.  If the
     * result is null, create an empty {@link ListDataModel} and return it.  If
     * the result is an instance of {@link DataModel}, return it.  Otherwise,
     * adapt the result as described in {@link #getValue} and return it.</p>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected DataModel getDataModel() {

        // Return any previously cached DataModel instance
        if (this.model != null) {
            return (model);
        }

        // Synthesize a DataModel around our current value if possible
        Object current = getValue();
        if (current == null) {
            this.model = new ListDataModel<Object>(Collections.emptyList());
        } else if (current instanceof DataModel) {
            this.model = ((DataModel) current);
        } else if (current instanceof List) {
            this.model = (new ListDataModel((List) current));
        } else if (Object[].class.isAssignableFrom(current.getClass())) {
            this.model = new ArrayDataModel((Object[]) current);
        } else {
            throw new IllegalStateException("value of DataTable must be either" +
                    " a List, array, or org.icemobile.model.DataModel not " 
                    + current.getClass());
        }
        return (model);

    }

    /** @see org.icemobile.component.IDataTable#setStyle(String) */
    public void setStyle(String style) {
        this.style = style;        
    }

    /** @see org.icemobile.component.IDataTable#getStyle() */
    public String getStyle() {
        return this.style;
    }

    /** @see javax.servlet.jsp.tagext.Tag#release() */
    @Override
    public void release() {
        LOG.warning("release");
        super.release();
        init();
    }
    
    /** @see javax.servlet.jsp.tagext.Tag#doStartTag() */
    @Override
    public int doStartTag() throws JspException {
       
        LOG.warning("start");
        writer = new TagWriter(pageContext);
        renderer = new DataTableCoreRenderer();
        try {
            renderer.renderRootStart(this, writer);
        } catch (IOException e) {
            throw new JspException(e);
        }
        //initialize model
        setRowCount(-1);
        
        return EVAL_BODY_INCLUDE;
    }
    
    /** @see javax.servlet.jsp.tagext.Tag#doAfterBody() */
    @Override
    public int doAfterBody() throws JspException{
        LOG.warning("start");
        try {
            if( !headersRendered ){
                
                renderer.renderResponsiveColumnStyles(this, writer);
                
                ClientDescriptor client = getClient();
                String largeViewOrientation = getLargeViewDetailOrientation();
                orientation = getDetailOrientation();
                if( ( client.isDesktopBrowser() || client.isTabletBrowser())
                        && largeViewOrientation != null && largeViewOrientation.length() > 0){
                   orientation = getLargeViewDetailOrientation();
                }
                if( IDataTable.TOP.equals(orientation)){
                    renderer.renderDetailView(this,writer);
                    renderer.renderMasterViewStart(this,writer);
                }
                else if( IDataTable.BOTTOM.equals(orientation)){
                    renderer.renderMasterViewStart(this,writer);
                }
                else if( IDataTable.LEFT.equals(orientation)){
                    renderer.renderDetailView(this,writer);
                    renderer.renderMasterViewStart(this,writer);
                }
                else if( IDataTable.RIGHT.equals(orientation)){
                    renderer.renderMasterViewStart(this,writer);
                }
                
                LOG.warning("headers not yet rendered");
                LOG.warning("bodyContent: " + super.getBodyContent());
                if( super.getBodyContent() != null ){
                    super.getBodyContent().clearBuffer();
                }
                renderer.renderHeaders(this, writer);
                renderer.renderDataRegionStart(this, writer);
                if( getFirst() > -1 ){
                    setRowIndex(getFirst());
                }
                else{
                    setRowIndex(0);
                }
                
                headersRendered = true;
            }
            else{
                setRowIndex(getRowIndex()+1);
            }
            
            if( renderer.hasNext(this) ){
                renderer.renderNextRow(this, writer);
                LOG.warning("EVAL_BODY_AGAIN");
                return EVAL_BODY_AGAIN;
            }
            else{
                LOG.warning("SKIP_BODY");
                return SKIP_BODY;
            }
        }catch (IOException e) {
            throw new JspException(e);
        }
    }
    
    /** @see javax.servlet.jsp.tagext.Tag#doEndTag() */
    @Override
    public int doEndTag() throws JspException{
        LOG.warning("start");
        try {
            renderer.renderDataRegionEnd(this,writer);
            renderer.renderMasterViewEnd(this,writer);   
            
            if( IDataTable.BOTTOM.equals(orientation) 
                 || IDataTable.RIGHT.equals(orientation)){
                renderer.renderDetailView(this,writer);
            }
            renderer.renderJavaScript(this, writer);        
            renderer.renderRootEnd(this, writer);  
            init();//reset
        }catch (IOException e) {
            throw new JspException(e);
        }
        LOG.warning("end");
        return EVAL_PAGE;
    }
    
    public void doFinally(){
        LOG.warning("start");
    }
    
    public void doCatch(Throwable t)
            throws Throwable{
        t.printStackTrace();
    }

    @Override
    public void doInitBody() throws JspException {
        LOG.warning("start");
        super.doInitBody();
    }

    public boolean readyToRenderChildren(){
        return headersRendered;
    }

    /** @see org.icemobile.component.IDataTable#isClientSide() */
    public boolean isClientSide() {
        return clientSide;
    }

    /** @see org.icemobile.component.IDataTable#setClientSide(boolean) */
    public void setClientSide(boolean client) {
        this.clientSide = client;        
    }

    /** @see org.icemobile.component.IDataTable#renderDetailView() */
    public void renderDetailView() throws IOException {
        writer.write(renderedDetailView);
    }
    
    /* package private methods for ColumnTag */
    void setRenderedDetailView(String str){
        this.renderedDetailView = str;
    }
    
    String getRenderedDetailView(){
        return this.renderedDetailView;
    }

}
