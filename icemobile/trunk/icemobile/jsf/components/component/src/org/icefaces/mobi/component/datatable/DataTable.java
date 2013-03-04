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
package org.icefaces.mobi.component.datatable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IColumn;
import org.icemobile.component.IDataTable;
import org.icemobile.model.ArrayDataModel;
import org.icemobile.model.DataModel;
import org.icemobile.model.ListDataModel;
import org.icemobile.util.ClientDescriptor;

/**
 * The JSF Mobile DataTable component
 *
 */
@SuppressWarnings("rawtypes")
public class DataTable extends DataTableBase implements IDataTable{
    
    private static final Logger LOG =
            Logger.getLogger(DataTable.class.toString());
    
    private DataModel model = null;
    private Object oldVar;
    
    /**
     * Return a flag indicating whether there is rowData available at the current rowIndex.
     * 
     * @return true if data for the current row is available
     */
    public boolean isRowAvailable(){
        return this.getDataModel().isRowAvailable();
    }

    /** @see org.icemobile.component.IMobiComponent#getClient() */
    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

    /** @see org.icemobile.component.IDataTable#getColumns() */
    public List<IColumn> getColumns() {
        List<UIComponent> children = getChildren();
        List<IColumn> columns = new ArrayList<IColumn>();
        if( children != null && !children.isEmpty() ){
            for( UIComponent child : children ){
                if( child instanceof IColumn ){
                    columns.add((IColumn)child);
                }
            }
        }
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
    @SuppressWarnings("unchecked")
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
    
    /**
     * Sets the value attribute and resets the underlying DataModel
     * @see org.icemobile.component.IDataTable#setValue(Object)
     */
    @Override
    public void setValue(Object value) {
        this.model = null;
        super.setValue(value);

    }
    
    /**
     * Set the zero-relative index of the currently selected row.
     * @see org.icemobile.component.IDataTable#setRowIndex(int)
     */
    public void setRowIndex(int index){
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
        
        Map<String,Object> requestMap = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestMap();
        String var = getVar();
        if (var != null) {
            if (getDataModel().getRowIndex() == -1) {
                oldVar = (requestMap.remove(var));
            } else if (isRowAvailable()) {
                Object rowData = getRowData();
                //LOG.info("setting row " + getDataModel().getRowIndex() 
                  //      + " data '" + var + "'=" + rowData);
                requestMap.put(var, rowData);
            } else {
                requestMap.remove(var);
                if (null != oldVar) {
                    requestMap.put(var, oldVar);
                    oldVar = null;
                }
            }
        }
    }
    
    /**
     * Return the zero-relative index of the currently selected row.
     * @see org.icemobile.component.IDataTable#getRowIndex()
     */
    public int getRowIndex(){
        return getDataModel().getRowIndex();
    }

    
    /**
     * Return the data object representing the data for the currently 
     * selected row index, if any.
     * @see org.icemobile.component.IDataTable#getRowData()
     */
    public Object getRowData(){
        return (getDataModel().getRowData());
    }

    /**
     * @see org.icemobile.component.IDataTable#renderDetailView()
     */
    public void renderDetailView() throws IOException{
        UIComponent detail = getFacet("detail");
        String output = "";
        if( detail != null ){
            detail.encodeAll(FacesContext.getCurrentInstance());
        }
    }

}
