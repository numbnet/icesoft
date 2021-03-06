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

package org.icefaces.samples.showcase.example.compat.menuBar;

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
        parent = MenuBarBean.BEAN_NAME,
        title = "example.compat.menuBar.separator.title",
        description = "example.compat.menuBar.separator.description",
        example = "/resources/examples/compat/menuBar/menuBarSeparator.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuBarSeparator.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuBar/menuBarSeparator.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuBarSeparator.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuBar/MenuBarSeparator.java")
        }
)
@ManagedBean(name= MenuBarSeparator.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuBarSeparator extends ComponentExampleImpl<MenuBarSeparator> implements Serializable {
	
	public static final String BEAN_NAME = "menuBarSeparator";
	
	private boolean hideSeparators = false;
	
	public MenuBarSeparator() {
		super(MenuBarSeparator.class);
	}
	
	public boolean getHideSeparators() { return hideSeparators; }
	
	public void setHideSeparators(boolean hideSeparators) { this.hideSeparators = hideSeparators; }
}
