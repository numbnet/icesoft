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

package org.icefaces.samples.showcase.example.compat.dataTable;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.group.title",
        description = "example.compat.dataTable.group.description",
        example = "/resources/examples/compat/dataTable/dataTableGroup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableGroup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableGroup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableGroup.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableGroup.java")
        }
)
@ManagedBean(name= DataTableGroup.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableGroup extends ComponentExampleImpl<DataTableGroup> implements Serializable {

    public static final String BEAN_NAME = "dataTableGroup";

    private List<Car> carsData;

    public DataTableGroup() 
    {
        super(DataTableGroup.class);
        init();
    }

    private void init() {
        carsData = new ArrayList<Car>(DataTableData.CARS.subList(0, 20));
        DataTableSort.sort(DataTableSort.SORT_COLUMN_CHASSIS, carsData);
    }

    public List<Car> getCarsData() { return carsData; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
}
