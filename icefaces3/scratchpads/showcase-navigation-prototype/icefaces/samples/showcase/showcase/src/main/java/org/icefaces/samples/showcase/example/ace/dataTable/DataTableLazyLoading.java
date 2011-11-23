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

import java.util.Map;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.samples.showcase.example.ace.dataTable.utilityClasses.VehicleGenerator;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.lazyLoading.title",
        description = "example.ace.dataTable.lazyLoading.description",
        example = "/resources/examples/ace/dataTable/dataTableLazyLoading.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableLazyLoading.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableLazyLoading.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableLazyLoading.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableLazyLoading.java")
        }
)
@ManagedBean(name= DataTableLazyLoading.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableLazyLoading extends ComponentExampleImpl<DataTableLazyLoading> implements Serializable 
{
    public static final String BEAN_NAME = "dataTableLazyLoading";
    
    private LazyDataModel<Car> carsData;

    public DataTableLazyLoading() 
    {
        super(DataTableLazyLoading.class);
        carsData = new LazyDataModel<Car>() 
        {
            @Override
            public List<Car> load(int first, int pageSize, SortCriteria[] criteria, Map<String, String> filters) 
            {
                List<Car> randomCars;
                randomCars = generateRandomCars(pageSize);
                return randomCars;
            }
        };
        
        carsData.setRowCount(3000000);
    }
    /**@param carsToGenerate - this number is passed from the data Table on each navigation via paginator and represent amount of rows per page
     */
    private ArrayList<Car> generateRandomCars(int carsToGenerate) 
    {
        Random rand = new Random();
        ArrayList<Car> listWithRandomCars = new ArrayList<Car>();
        VehicleGenerator randomGenerator = new VehicleGenerator();
        for (int i = 0; i < carsToGenerate; i++) 
        {
            Car randomCar = randomGenerator.generateCar();
            listWithRandomCars.add(randomCar);
        }
        return listWithRandomCars;
    }

    public LazyDataModel<Car> getCarsData() {
        return carsData;
    }

    public void setCarsData(LazyDataModel<Car> carsData) {
        this.carsData = carsData;
    }
    
    

}