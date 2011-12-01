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

package org.icefaces.samples.showcase.example.ace.dialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.dialog.title",
        description = "example.ace.dialog.description",
        example = "/resources/examples/ace/dialog/dialog.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dialog.xhtml",
                    resource = "/resources/examples/ace/dialog/dialog.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="Dialog.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dialog/DialogBean.java")
        }
)
@Menu(
	title = "menu.ace.dialog.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dialog.subMenu.main",
	                isDefault = true,
                    exampleBeanName = DialogBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dialog.subMenu.effectsAndSize",
                    exampleBeanName = DialogEffectsAndSizeBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dialog.subMenu.modalDialog",
                    exampleBeanName = ModalDialogBean.BEAN_NAME)
    }
)
@ManagedBean(name= DialogBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DialogBean extends ComponentExampleImpl<DialogBean> implements Serializable 
{
    public static final String BEAN_NAME = "dialogBean";
    private String firstName;
    private String lastName;
    
    
    public DialogBean() 
    {
        super(DialogBean.class);
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
