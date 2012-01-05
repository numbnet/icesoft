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
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.configpanel.title",
        description = "example.ace.dataTable.configpanel.description",
        example = "/resources/examples/ace/dataTable/dataTableConfigPanel.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableConfigPanel.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableConfigPanel.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="DataTableConfigPanel.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableConfigPanel.java")
        }
)
@ManagedBean(name= DataTableConfigPanel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableConfigPanel extends ComponentExampleImpl<DataTableConfigPanel> implements Serializable {
    public static final String BEAN_NAME = "dataTableConfigPanel";

    public DataTableConfigPanel() {
        super(DataTableConfigPanel.class);
    }
}
