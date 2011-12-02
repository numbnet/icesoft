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

package org.icefaces.samples.showcase.example.compat.positioned;

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
        title = "example.compat.positioned.title",
        description = "example.compat.positioned.description",
        example = "/resources/examples/compat/positioned/positioned.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="positioned.xhtml",
                    resource = "/resources/examples/compat/"+
                               "positioned/positioned.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PositionedBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/positioned/PositionedBean.java")
        }
)
@Menu(
	title = "menu.compat.positioned.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.positioned.subMenu.main",
                    isDefault = true,
                    exampleBeanName = PositionedBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.positioned.subMenu.constraint",
                    exampleBeanName = PositionedConstraint.BEAN_NAME),
            @MenuLink(title = "menu.compat.positioned.subMenu.listener",
                    exampleBeanName = PositionedListener.BEAN_NAME),
            @MenuLink(title = "menu.compat.positioned.subMenu.dynamic",
                    exampleBeanName = PositionedDynamic.BEAN_NAME),
            @MenuLink(title = "menu.compat.positioned.subMenu.sort",
                    exampleBeanName = PositionedSort.BEAN_NAME),
            @MenuLink(title = "menu.compat.positioned.subMenu.style",
                    exampleBeanName = PositionedStyle.BEAN_NAME)
})
@ManagedBean(name= PositionedBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PositionedBean extends ComponentExampleImpl<PositionedBean> implements Serializable {
	
	public static final String BEAN_NAME = "positioned";
	
	public PositionedBean() {
		super(PositionedBean.class);
	}
}
