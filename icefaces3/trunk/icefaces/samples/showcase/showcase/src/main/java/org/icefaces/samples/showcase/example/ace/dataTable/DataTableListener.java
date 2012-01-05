/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.ace.model.table.RowStateMap;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.listener.title",
        description = "example.ace.dataTable.listener.description",
        example = "/resources/examples/ace/dataTable/dataTableListener.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableListener.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableListener.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableListener.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableListener.java")
        }
)
@ManagedBean(name= DataTableListener.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableListener extends ComponentExampleImpl<DataTableListener> implements Serializable {
    public static final String BEAN_NAME = "dataTableListener";
    
    private static final int EVENT_SIZE = 5;
    
    private List<String> eventLog = new ArrayList<String>(EVENT_SIZE);
    private List selectedRows;
    private RowStateMap stateMap = new RowStateMap();

    public DataTableListener() {
        super(DataTableListener.class);
    }
    
    public int getEventSize() { return EVENT_SIZE; }
    public List<String> getEventLog() { return eventLog; }
    public List getSelectedRows() { return stateMap.getSelected(); }
    public RowStateMap getStateMap() { return stateMap; }

    public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
    public void setSelectedRows(List selectedRows) { }
    public void setStateMap(RowStateMap stateMap) { this.stateMap = stateMap; }

    public void selectListener(SelectEvent event) {
        addEvent((Car)event.getObject(), "selected");
    }

    public void deselectListener(UnselectEvent event) {
        addEvent((Car)event.getObject(), "deselected");
    }
    
	private void addEvent(Car car, String action) {
	    StringBuilder sb = new StringBuilder(40);
	    sb.append("Car \"");
	    sb.append(car.getName());
	    sb.append("\" (id ");
	    sb.append(car.getId());
	    sb.append(") was ");
	    sb.append(action);
	    sb.append(".");
	    eventLog.add(0, sb.toString());
	    
	    // Cap the list at the displayed row size
	    if (eventLog.size() > EVENT_SIZE) {
	        eventLog = eventLog.subList(0, EVENT_SIZE);
	    }
	}
}
