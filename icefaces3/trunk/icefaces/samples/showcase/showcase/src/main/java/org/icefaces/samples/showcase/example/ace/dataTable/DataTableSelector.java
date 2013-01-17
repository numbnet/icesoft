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

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.ace.model.table.CellSelections;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;

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
                                                          new SelectItem("multiplecell", "Multiple Cell") };

    private RowStateMap stateMap = new RowStateMap();
    private ArrayList<Car> selectedRows;
    private CellSelections[] singleCell;
    private CellSelections[] multiCell;
    private String selectionMode = AVAILABLE_MODES[0].getValue().toString();
    private boolean dblClick = false;
    private boolean instantUpdate = true;
    private List<Car> carsData;
    
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableSelector() {
        super(DataTableSelector.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- VALUE CHANGE LISTENERS BEGIN
    public void changedMode(ValueChangeEvent event) {
        stateMap.setAllSelected(false);
        singleCell = null;
        multiCell = null;
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public RowStateMap getStateMap() { return stateMap; }
    public ArrayList<Car> getMultiRow() { return (ArrayList<Car>) stateMap.getSelected(); }
    public CellSelections[] getSingleCell() { return singleCell; }
    public CellSelections[] getMultiCell() { return multiCell; }
    public String getSelectionMode() { return selectionMode; }
    public boolean getDblClick() { return dblClick; }
    public boolean getInstantUpdate() { return instantUpdate; }
    public SelectItem[] getAvailableModes() { return AVAILABLE_MODES; }
    public List<Car> getCarsData() { return carsData; }
    public Object getSelectionObject() {
        if (SELECT_SINGLE_ROW.equals(selectionMode) || SELECT_MULTI_ROW.equals(selectionMode)) {
            return selectedRows;
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

    public void setStateMap(RowStateMap stateMap) { this.stateMap = stateMap; }
    public void setMultiRow(ArrayList<Car> multiRow) { }
    public void setSingleCell(CellSelections[] singleCell) { this.singleCell = singleCell; }
    public void setMultiCell(CellSelections[] multiCell) { this.multiCell = multiCell; }
    public void setSelectionMode(String selectionMode) { this.selectionMode = selectionMode; }
    public void setDblClick(boolean dblClick) { this.dblClick = dblClick; }
    public void setInstantUpdate(boolean instantUpdate) { this.instantUpdate = instantUpdate; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
}
