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

package org.icefaces.samples.showcase.example.compat.confirmation;

import java.io.Serializable;
import java.util.Random;

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
        title = "example.compat.confirmation.title",
        description = "example.compat.confirmation.description",
        example = "/resources/examples/compat/confirmation/confirmation.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmation.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmation.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationBean.java")
        }
)
@Menu(
	title = "menu.compat.confirmation.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.confirmation.subMenu.main",
                    isDefault = true,
                    exampleBeanName = ConfirmationBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.label",
                    exampleBeanName = ConfirmationLabel.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.button",
                    exampleBeanName = ConfirmationButton.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.display",
                    exampleBeanName = ConfirmationDisplay.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.mouse",
                    exampleBeanName = ConfirmationMouse.BEAN_NAME)
})
@ManagedBean(name= ConfirmationBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationBean extends ComponentExampleImpl<ConfirmationBean> implements Serializable {
	
	public static final String BEAN_NAME = "confirmation";
	
	private Random randomizer = new Random(System.nanoTime());
	private int number = 0;
	
	public ConfirmationBean() {
		super(ConfirmationBean.class);
	}
	
	public int getNumber() { return number; }
	
	public void setNumber(int number) { this.number = number; }
	
	public void generateNumber(ActionEvent event) {
	    setNumber(1+randomizer.nextInt(1000));
	}
	
	public void clearNumber(ActionEvent event) {
	    setNumber(0);
	}
}
