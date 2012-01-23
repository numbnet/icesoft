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
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.columnReordering.title",
        description = "example.ace.dataTable.columnReordering.description",
        example = "/resources/examples/ace/dataTable/dataTableColumnReordering.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="columnReordering.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableColumnReordering.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="ColumnReordering.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableColumnReordering.java")
        }
)
@ManagedBean(name= DataTableColumnReordering.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableColumnReordering extends ComponentExampleImpl<DataTableColumnReordering> implements Serializable
{
    public static final String BEAN_NAME = "dataTableColumnReordering";
    private List<Car> cars;
    private List<Integer> ordering = new ArrayList<Integer>();

    public DataTableColumnReordering() 
    {
        super(DataTableColumnReordering.class);
        cars = generateCars(8);
    }
    
    
    
    private List<Car> generateCars(int count) 
    {
        List<Car> listWithCars = new ArrayList<Car>(count);
        // Generate some cars
        listWithCars.add(new Car(1, "Yellowjacket", "Subcompact", 2400, 5, 32.0 , 4498.00));
        listWithCars.add(new Car(2, "Iron Horse", "Mid-Size", 5760, 5, 31.4, 14216.00));
        listWithCars.add(new Car(3, "Hotshot", "Luxury", 6600, 5, 28.6, 14600.00));
        listWithCars.add(new Car(4, "Rockwell", "Station Wagon", 4575, 10, 29.7, 10150.00));
        listWithCars.add(new Car(5, "Hauler", "Pickup",  5405, 5, 27.6, 14110.00));
        listWithCars.add(new Car(6, "Vacationer", "Van", 5280, 5, 29.3, 12100.00));
        listWithCars.add(new Car(7, "Baron", "Bus", 19025, 5, 25.4, 104250.00));
        listWithCars.add(new Car(8, "Wolverine", "Semi-Truck", 16190, 5, 29.1, 98350.00));
        //shuffle list
        Collections.shuffle(listWithCars);
        
        return listWithCars;
        
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Integer> getOrdering() {
        return ordering;
    }
    public void setOrdering(List<Integer> ordering) {
        this.ordering = ordering;
    }
}