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
        title = "example.compat.dataTable.title",
        description = "example.compat.dataTable.description",
        example = "/resources/examples/compat/dataTable/dataTable.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTable.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTable.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableBean.java")
        }
)
@Menu(
	title = "menu.compat.dataTable.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.dataTable.subMenu.main",
                    isDefault = true,
                    exampleBeanName = DataTableBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.rows",
                    exampleBeanName = DataTableRows.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.group",
                    exampleBeanName = DataTableGroup.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.hide",
                    exampleBeanName = DataTableHide.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.resize",
                    exampleBeanName = DataTableResize.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.widths",
                    exampleBeanName = DataTableWidths.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.client",
                    exampleBeanName = DataTableClient.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.sort",
                    exampleBeanName = DataTableSort.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.scroll",
                    exampleBeanName = DataTableScroll.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.subMenu.dynamic",
                    exampleBeanName = DataTableDynamic.BEAN_NAME)
})
@ManagedBean(name= DataTableBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableBean extends ComponentExampleImpl<DataTableBean> implements Serializable {
	
	public static final String BEAN_NAME = "dataTable";
	
	public DataTableBean() {
		super(DataTableBean.class);
	}
}
