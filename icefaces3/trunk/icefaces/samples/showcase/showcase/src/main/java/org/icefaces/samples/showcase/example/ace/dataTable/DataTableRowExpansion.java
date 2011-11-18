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

import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.example.compat.dataTable.DataTableData;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.*;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.rowexpansion.title",
        description = "example.ace.dataTable.rowexpansion.description",
        example = "/resources/examples/ace/dataTable/dataTableRowExpansion.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableRowExpansion.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableRowExpansion.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="DataTableRowExpansion.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableRowExpansion.java")
        }
)
@ManagedBean(name= DataTableRowExpansion.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableRowExpansion extends ComponentExampleImpl<DataTableRowExpansion> implements Serializable {
    public static final String BEAN_NAME = "dataTableRowExpansion";

    ArrayList<Map.Entry<Car,List>> carsData = null;

    public DataTableRowExpansion() {
        super(DataTableRowExpansion.class);

        generateCarsData();
    }

    private void generateCarsData() {
        carsData = new ArrayList<Map.Entry<Car, List>>();
        for (Car c : DataTableData.CARS) {
            ArrayList<Map.Entry<Car, List>> detailData = new ArrayList<Map.Entry<Car, List>>();
            detailData.add(new AbstractMap.SimpleEntry(new Car(c.getId()+1000, c.getName()+" Custom Spec", c.getChassis(), c.getWeight(), c.getAcceleration()*2, c.getMpg()/2, c.getCost()*3), null));
            carsData.add(new AbstractMap.SimpleEntry(c, detailData));
        }
    }

    public ArrayList<Map.Entry<Car,List>> getCarsData() {
        return carsData;
    }

    public void setCarsData(ArrayList<Map.Entry<Car,List>> carsData) {
        this.carsData = carsData;
    }
}
