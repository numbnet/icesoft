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

package org.icefaces.samples.showcase.example.ace.checkboxButton;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;

@ComponentExample(
        title = "example.ace.checkboxButton.title",
        description = "example.ace.checkboxButton.description",
        example = "/resources/examples/ace/checkboxButton/checkboxbutton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="checkboxbutton.xhtml",
                    resource = "/resources/examples/ace/checkboxButton/checkboxbutton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CheckboxButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/checkboxButton/CheckboxButtonBean.java")
        }
)

@Menu(
	title = "menu.ace.checkboxButton.subMenu.title",
	menuLinks = 
                {
                    @MenuLink(title = "menu.ace.checkboxButton.subMenu.main", isDefault = true,
                                     exampleBeanName = CheckboxButtonBean.BEAN_NAME)
                }
)

@ManagedBean(name= CheckboxButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonBean extends ComponentExampleImpl<CheckboxButtonBean> implements Serializable {

    public static final String BEAN_NAME = "checkboxButton";

    private boolean boxValue = true;

    public boolean isBoxValue() {
        return boxValue;
    }

    public void setBoxValue(boolean boxValue) {
        this.boxValue = boxValue;
    }

    public void boxValueListener(ActionEvent e) {
        boolean isCheckboxedChecked = boxValue; // or e.getComponent().getSelected()

        if (isCheckboxedChecked) {
            FacesUtils.addInfoMessage("The checkbox is selected!");
        } else {
            FacesUtils.addInfoMessage("The checkbox is not selected.");
        }
    }

    public CheckboxButtonBean() {
        super(CheckboxButtonBean.class);
    }
}
