package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 */

@ComponentExample(
        title = "example.ace.dataTable.title",
        description = "example.ace.dataTable.description",
        example = "/resources/examples/ace/dataTable/dataTable.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTable.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTable.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableBean.java")
        }
)
@Menu(
	title = "menu.ace.dataTable.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dataTable.subMenu.main",
	                isDefault = true,
                    exampleBeanName = DataTableBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.filtering",
                    exampleBeanName = DataTableFiltering.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.sorting",
                    exampleBeanName = DataTableSorting.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.paginator",
                    exampleBeanName = DataTablePaginator.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.scrolling",
                    exampleBeanName = DataTableScrolling.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.selector",
                    exampleBeanName = DataTableSelector.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.listener",
                    exampleBeanName = DataTableListener.BEAN_NAME)
    }
)
@ManagedBean(name= DataTableBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableBean extends ComponentExampleImpl<DataTableBean> implements Serializable {
    public static final String BEAN_NAME = "dataTableBean";

    public DataTableBean() {
        super(DataTableBean.class);
    }
}