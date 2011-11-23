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

package org.icefaces.samples.showcase.example.compat.tab;

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
        title = "example.compat.tab.title",
        description = "example.compat.tab.description",
        example = "/resources/examples/compat/tab/tab.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tab.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tab.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabBean.java")
        }
)
@Menu(
	title = "menu.compat.tab.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.tab.subMenu.main",
                    isDefault = true,
                    exampleBeanName = TabBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.placement",
                    exampleBeanName = TabPlacement.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.icon",
                    exampleBeanName = TabIcon.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.label",
                    exampleBeanName = TabLabel.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.wrapping",
                    exampleBeanName = TabWrapping.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.visibility",
                    exampleBeanName = TabVisibility.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.control",
                    exampleBeanName = TabControl.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.events",
                    exampleBeanName = TabEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.subMenu.dynamic",
                    exampleBeanName = TabDynamic.BEAN_NAME)
})
@ManagedBean(name= TabBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabBean extends ComponentExampleImpl<TabBean> implements Serializable {
	
	public static final String BEAN_NAME = "tab";
	
	public TabBean() {
		super(TabBean.class);
	}
}
