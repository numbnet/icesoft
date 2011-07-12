package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.icefaces.ace.model.Cell;

/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 */

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.selector.title",
        description = "example.ace.dataTable.selector.description",
        example = "/resources/examples/ace/dataTable/dataTableSelector.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableSelector.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableSelector.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableSelector.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableSelector.java")
        }
)
@ManagedBean(name= DataTableSelector.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableSelector extends ComponentExampleImpl<DataTableSelector> implements Serializable {
    public static final String BEAN_NAME = "dataTableSelector";
    
    private static final String SELECT_SINGLE_ROW = "single";
    private static final String SELECT_MULTI_ROW = "multiple";
    private static final String SELECT_SINGLE_CELL = "singlecell";
    private static final String SELECT_RANGE_CELL = "cellrange";
    private static final String SELECT_BLOCK_CELL = "cellblock";
    private static final SelectItem[] AVAILABLE_MODES = { new SelectItem("single", "Single Row"),
                                                          new SelectItem("multiple", "Multiple Rows"),
                                                          new SelectItem("singlecell", "Single Cell"),
                                                          new SelectItem("cellrange", "Cell Range"),
                                                          new SelectItem("cellblock", "Cell Block") };
    
    private Car singleRow;
    private Car[] multiRow;
    private Cell singleCell;
    private Cell[] multiCell;
    private String selectionMode = AVAILABLE_MODES[0].getValue().toString();
    private boolean dblClick = false;
    private boolean instantUpdate = true;
                                                          
    public DataTableSelector() {
        super(DataTableSelector.class);
    }
    
    
    public Car getSingleRow() { return singleRow; }
    public Car[] getMultiRow() { return multiRow; }
    public Cell getSingleCell() { return singleCell; }
    public Cell[] getMultiCell() { return multiCell; }
    public String getSelectionMode() { return selectionMode; }
    public boolean getDblClick() { return dblClick; }
    public boolean getInstantUpdate() { return instantUpdate; }
    public SelectItem[] getAvailableModes() { return AVAILABLE_MODES; }
    public Object getSelectionObject() {
        if (SELECT_SINGLE_ROW.equals(selectionMode)) {
            return singleRow;
        }
        else if (SELECT_MULTI_ROW.equals(selectionMode)) {
            return multiRow;
        }
        else if (SELECT_SINGLE_CELL.equals(selectionMode)) {
            return singleCell;
        }
        else if ((SELECT_RANGE_CELL.equals(selectionMode)) ||
                 (SELECT_BLOCK_CELL.equals(selectionMode))) {
            return multiCell;
        }
        
        return null;
    }
    
    public void setSingleRow(Car singleRow) { this.singleRow = singleRow; }
    public void setMultiRow(Car[] multiRow) { this.multiRow = multiRow; }
    public void setSingleCell(Cell singleCell) { this.singleCell = singleCell; }
    public void setMultiCell(Cell[] multiCell) { this.multiCell = multiCell; }
    public void setSelectionMode(String selectionMode) { this.selectionMode = selectionMode; }
    public void setDblClick(boolean dblClick) { this.dblClick = dblClick; }
    public void setInstantUpdate(boolean instantUpdate) { this.instantUpdate = instantUpdate; }
    
    public void changedMode(ValueChangeEvent event) {
        singleRow = null;
        multiRow = null;
        singleCell = null;
        multiCell = null;
    }
}
