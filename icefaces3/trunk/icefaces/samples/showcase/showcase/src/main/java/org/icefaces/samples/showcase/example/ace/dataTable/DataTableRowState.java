/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.rowstate.title",
        description = "example.ace.dataTable.rowstate.description",
        example = "/resources/examples/ace/dataTable/dataTableRowState.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableRowState.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableRowState.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="DataTableRowState.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableRowState.java")
        }
)
@ManagedBean(name= DataTableRowState.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableRowState extends ComponentExampleImpl<DataTableRowState> implements Serializable {
    public static final String BEAN_NAME = "dataTableRowState";

    private RowStateMap stateMap = new RowStateMap();
    private List<Car> selection;

    public DataTableRowState() {
        super(DataTableRowState.class);
    }

    public RowStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(RowStateMap stateMap) {
        this.stateMap = stateMap;
    }

    public List<Car> getSelection() {
        return stateMap.getSelected();
    }

    public void setSelection(List<Car> selection) {}

    public void enableAllSelection(ActionEvent e) {
        stateMap.setAllSelectable(true);
    }
    public void disableSelection(ActionEvent e) {
        for (Object rowData : stateMap.getSelected()) {
            RowState s = stateMap.get(rowData);
            s.setSelectable(false);
            s.setSelected(false);
        }
    }
    public void disableAllSelection(ActionEvent e) {
        stateMap.setAllSelectable(false);
    }

    public void enableAllVisibility(ActionEvent e) {
        stateMap.setAllVisible(true);
    }
    public void disableVisibility(ActionEvent e) {
        for (Object rowData : stateMap.getSelected()) {
            RowState s = stateMap.get(rowData);
            s.setVisible(false);
            s.setSelected(false);
        }
    }
    public void disableAllVisibility(ActionEvent e) {
        stateMap.setAllVisible(false);
    }

    public void enableAllEditing(ActionEvent e) {
        stateMap.setAllEditable(true);
    }
    public void enableEditing(ActionEvent e) {
        for (Object rowData : stateMap.getSelected()) {
            RowState s = stateMap.get(rowData);
            s.setEditable(true);
            s.setSelected(false);
        }
    }
    public void disableEditing(ActionEvent e) {
        for (Object rowData : stateMap.getSelected()) {
            RowState s = stateMap.get(rowData);
            s.setEditable(false);
            s.setSelected(false);
        }
    }
    public void disableAllEditing(ActionEvent e) {
        stateMap.setAllEditable(false);
    }
}
