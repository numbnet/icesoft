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

package org.icefaces.samples.showcase.example.compat.dataTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.dynamic.title",
        description = "example.compat.dataTable.dynamic.description",
        example = "/resources/examples/compat/dataTable/dataTableDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableDynamic.java")
        }
)
@ManagedBean(name= DataTableDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableDynamic extends ComponentExampleImpl<DataTableDynamic> implements Serializable {
	
    public static final String BEAN_NAME = "dataTableDynamic";

    private static final int BULK_ADD_NUMBER = 5;
    private int bulkNumber = BULK_ADD_NUMBER;
    private List<Car> carsData = getDefaultCars();
    private Car toModify = new Car();
    private int toRemove;
    private int toEdit;
    private int addCalls = 0;

    public DataTableDynamic() {
            super(DataTableDynamic.class);
    }

    public List<Car> getCarsData() { return carsData; }
    public Car getToModify() { return toModify; }
    public int getToRemove() { return toRemove; }
    public int getToEdit() { return toEdit; }

    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
    public void setToModify(Car toModify) { this.toModify = toModify; }
    public void setToRemove(int toRemove) { this.toRemove = toRemove; }
    public void setToEdit(int toEdit) { this.toEdit = toEdit; }

    private List<Car> getDefaultCars() {
        // Return a smaller list if possible
        if (DataTableData.CARS.size() > DataTableData.DEFAULT_ROWS) {
            return new ArrayList<Car>(DataTableData.CARS.subList(0, DataTableData.DEFAULT_ROWS));
        }

        return new ArrayList<Car>(DataTableData.CARS);
    }

    private Car getCarById(int id) {
                for (Car currentItem : carsData) {
            if (id == currentItem.getId()) {
                return currentItem;
            }
        }

        return null;
    }

    public void bulkAdd(ActionEvent event) {
        DataTableData.addRandomCars((DataTableData.DEFAULT_ROWS + (addCalls * BULK_ADD_NUMBER) + 1),
                                    carsData,
                                    BULK_ADD_NUMBER);
        addCalls++;
    }

    public void submitItem(ActionEvent event) {
        // Edit use case if we already have an id
        if (toModify.getId() != -1) {
            Car oldEdit = getCarById(toModify.getId());

            // Only apply the changes if we can
            // There is a chance a user edits a car, then deletes it, then tries to submit the edit
            // This logic will prevent that from causing an error
            if (oldEdit != null) {
                oldEdit.applyValues(toModify);

                // Reset the values to blank
                toModify = new Car();

                return;
            }
        }
                // Ensure we use a valid ID
                toModify.setId(DataTableData.DEFAULT_ROWS + (addCalls * BULK_ADD_NUMBER) + 1);

                // Add our new car
                carsData.add(toModify);

                // Increase our add calls for ID tracking purposes
                addCalls++;

        // Reset the values to blank
        toModify = new Car();
    }

    public String removeItem() {
        Car removeCar = getCarById(toRemove);

        if (removeCar != null) {
            carsData.remove(removeCar);
        }

        return null;
    }

    public String editItem() {
        toModify = getCarById(toEdit);

        return null;
    }

    public void restoreDefault() {
        carsData = getDefaultCars();
    }

    public int getBulkNumber() {
        return bulkNumber;
    }

    public void setBulkNumber(int bulkNumber) {
        this.bulkNumber = bulkNumber;
    }
}
