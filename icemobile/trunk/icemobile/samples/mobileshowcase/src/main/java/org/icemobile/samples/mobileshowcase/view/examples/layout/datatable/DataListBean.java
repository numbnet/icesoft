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
package org.icemobile.samples.mobileshowcase.view.examples.layout.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Example;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResource;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResources;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ResourceType;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

@Destination(
        title = "example.layout.datalist.destination.title.short",
        titleExt = "example.layout.datalist.destination.title.long",
        titleBack = "example.layout.datalist.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/datatable-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/datalist-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "datalist-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/datalist-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "DataListBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/datatable/DataListBean.java")
        }
)
@ManagedBean(name = DataListBean.BEAN_NAME)
@SessionScoped
public class DataListBean extends ExampleImpl<DataListBean> implements
    Serializable {
    
    public static final String BEAN_NAME = "dataListBean";
    
    public DataListBean() {
        super(DataListBean.class);
        dataList = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    
    private List<Car> dataList;

    public List<Car> getDataList() {
        return dataList;
    }

}
