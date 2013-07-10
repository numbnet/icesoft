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

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final SelectItem[] AVAILABLE_MODES = { new SelectItem("single", "Single Row"),
                                                          new SelectItem("multiple", "Multiple Rows"),
                                                          new SelectItem("singlecell", "Single Cell"),
                                                          new SelectItem("multiplecell", "Multiple Cell") };

    private RowStateMap stateMap = new RowStateMap();
    private String selectionMode = AVAILABLE_MODES[0].getValue().toString();
    private boolean dblClick = false;
    private boolean instantUpdate = true;
    private List<Car> carsData;
    
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableSelector() {
        super(DataTableSelector.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    public String getColVal(Object rowObject, String columnId) {
        DataTable table = ((DataTableBindings)(FacesUtils.getManagedBean("dataTableBindings"))).getTable(this.getClass());
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        ELContext el = context.getELContext();
        String key = table.getVar();
        String ret = null;

        Object oldVal = requestMap.get(key);

        requestMap.put(key, rowObject);

        for (Column c : table.getColumns()) {
            if (c.getId().equals(columnId)) {
                ret = ((UIOutput)c.getChildren().get(0)).getValueExpression("value").getValue(el).toString();
                break;
            }
        }

        requestMap.put(key, oldVal);
        return ret;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- VALUE CHANGE LISTENERS BEGIN
    public void changedMode(ValueChangeEvent event) {
        stateMap.clear();
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public Class getClazz() {
        return getClass();
    }
    public RowStateMap getStateMap() { return stateMap; }
    public ArrayList<Car> getMultiRow() { return (ArrayList<Car>) stateMap.getSelected(); }
    public Map<Car, List<String>> getMultiCell() {
        Map<Car, List<String>> selectedCells = new HashMap<Car, List<String>>();

        for (Object o : stateMap.getRowsWithSelectedCells()) {
            Car c = (Car)o;
            selectedCells.put(c, stateMap.get(c).getSelectedColumnIds());
        }

        return selectedCells;
    }
    public String getSelectionMode() { return selectionMode; }
    public boolean getDblClick() { return dblClick; }
    public boolean getInstantUpdate() { return instantUpdate; }
    public SelectItem[] getAvailableModes() { return AVAILABLE_MODES; }
    public List<Car> getCarsData() { return carsData; }


    public void setStateMap(RowStateMap stateMap) { this.stateMap = stateMap; }
    public void setMultiRow(ArrayList<Car> multiRow) { }
    public void setSelectionMode(String selectionMode) { this.selectionMode = selectionMode; }
    public void setDblClick(boolean dblClick) { this.dblClick = dblClick; }
    public void setInstantUpdate(boolean instantUpdate) { this.instantUpdate = instantUpdate; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
}
