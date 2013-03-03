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
package org.icemobile.component;

import java.util.List;

/**
 * The interfaces for the Mobile JSF and JSP DataTable
 *
 */
public interface IDataTable extends IMobiComponent{
    
    /** A top orientation of the Detail Section */
    public static final String TOP = "top";
    /** A bottom orientation of the Detail Section */
    public static final String BOTTOM = "bottom";
    /** A left orientation of the Detail Section for a large view */
    public static final String LEFT = "left";
    /** A right orientation of the Detail Section for a large view */
    public static final String RIGHT = "right";
    
    /** Set the var property for the row-level variable */
    public void setVar(String var);
    
    /** Get the var property for the row-level variable */
    public String getVar();
    
    /** Set the tabIndex for the DataTable */
    public void setTabIndex(java.lang.Integer tabIndex);
    
    /** Get the tabIndex for the DataTable */
    public java.lang.Integer getTabIndex();
    
    /** Get the current row data object from the current DataModel */
    public Object getRowData();
    
    /** Set the total count of rows from the current DataModel */
    public void setRowCount(int rowCount);
    
    /** Get the total count of rows from the current DataModel */
    public int getRowCount();
    
    /** Set the number of rows to be displayed for the DataTable */
    public void setRows(int rows);
    
    /** Get the number of rows to be displayed for the DataTable */
    public int getRows();
    
    /** Set whether to show the paginator for the DataTable */
    public void setPaginator(boolean paginator);
    
    /** Get whether the paginator is to be displayed for the DataTable */
    public boolean isPaginator();
    
    /** Set the message to be shown when no data is available */
    public void setEmptyMessage(String emptyMessage);
    
    /** Get the message to be shown when no data is available */
    public String getEmptyMessage();
    
    /** Set the current page to display on a paginated DataTable */
    public void setPage(int page);
    
    /** Get the current page to display on a paginated DataTable */
    public int getPage();
    
    /** Set the current row index of the underlying DataModel */
    public void setRowIndex(int rowIndex);
    
    /** Get the current row index of the underlying DataModel */
    public int getRowIndex();
    
    /** Set the List, array or DataModel to be iterated for the DataTable */
    public void setValue(Object value);
    
    /** Get the List, array or DataModel to be iterated for the DataTable */
    public Object getValue();
    
    /** 
     * Set whether the DataTable is to use lazy data loading. This 
     * property may be true when the DataTable is used in 'client' mode. 
     */
    public void setLazy(boolean lazy);
    
    /** Get whether the DataTable is to use lazy data loading. */
    public boolean isLazy();
    
    /** Set the first row to be displayed in the DataTable */
    public void setFirst(int first);
    
    /** Get the first row to be displayed in the DataTable */
    public int getFirst();
    
    /** Get whether the current index of the DataTable is available */
    public boolean isRowAvailable();
    
    /** Get the column structure for the DataTable */
    public List<IColumn> getColumns();
    
    /** Get the large view orientation of the Detail Section of 
     * the DataTable, which may be different than 
     * the small view orientation 
     * @return 'top', 'bottom', 'left' or 'right'
     */
    public String getLargeViewDetailOrientation();
    
    /** Set the large view orientation of the Detail Section of 
     * the DataTable, which may be different than 
     * the small view orientation 
     * @param 'top', 'bottom', 'left' or 'right'
     */
    public void setLargeViewDetailOrientation(String orientation);
    
    /** Get the orientation of the Detail Section of the DataTable. 
     * The orientation, if set, will apply to both the small 
     * and large views, but can be overridden by the 
     * largeViewDetailOrientation property
     * @return 'top' or 'bottom' 
     */
    public String getDetailOrientation();
    
    /** Set the orientation of the Detail Section of the DataTable. 
     * The orientation, if set, will apply to both the small 
     * and large views, but can be overridden by the 
     * largeViewDetailOrientation property
     * @param orientation 'top' or 'bottom'
     */
    public void setDetailOrientation(String orientation);
    
    /**
     * Get the client-side mode setting. The client-side mode 
     * will render the DataTable with client-side functionality.
     * @return true if the DataTable is in client-side mode
     */
    public boolean isClient();
    
    /**
     * Set the client-side mode setting. The client-side mode 
     * will render the DataTable with client-side functionality.
     * @param client true if the DataTable should render in client-side mode
     */
    public void setClient(boolean client);


}
