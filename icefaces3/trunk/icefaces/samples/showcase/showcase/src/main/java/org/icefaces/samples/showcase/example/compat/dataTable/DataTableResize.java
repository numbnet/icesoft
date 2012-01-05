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

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.resize.title",
        description = "example.compat.dataTable.resize.description",
        example = "/resources/examples/compat/dataTable/dataTableResize.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableResize.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableResize.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableResize.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableResize.java")
        }
)
@ManagedBean(name= DataTableResize.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableResize extends ComponentExampleImpl<DataTableResize> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableResize";
	
	private boolean resizable = true;
	
	public DataTableResize() {
		super(DataTableResize.class);
	}
	
	public boolean getResizable() { return resizable; }
	
	public void setResizable(boolean resizable) { this.resizable = resizable; }
}
