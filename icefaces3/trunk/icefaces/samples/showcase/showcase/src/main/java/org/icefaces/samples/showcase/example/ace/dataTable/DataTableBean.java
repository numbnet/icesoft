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

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

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
	        @MenuLink(title = "menu.ace.dataTable.subMenu.main", isDefault = true, exampleBeanName = DataTableBean.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.columnReordering", exampleBeanName = DataTableColumnReordering.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.stackable", exampleBeanName = DataTableStackable.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataTable.subMenu.filtering", exampleBeanName = DataTableFiltering.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.lazyLoading", exampleBeanName = DataTableLazyLoading.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.paginator", exampleBeanName = DataTablePaginator.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.panelexpansion", exampleBeanName = DataTablePanelExpansion.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.rowexpansion", exampleBeanName = DataTableRowExpansion.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.listener", exampleBeanName = DataTableListener.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.selector", exampleBeanName = DataTableSelector.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.rowstate", exampleBeanName = DataTableRowState.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.scrolling", exampleBeanName = DataTableScrolling.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.sorting", exampleBeanName = DataTableSorting.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.configpanel", exampleBeanName = DataTableConfigPanel.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataTable.subMenu.rowEditing", exampleBeanName = DataTableRowEditing.BEAN_NAME)
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