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

package org.icefaces.samples.showcase.example.ace.confirmationdialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.confirmationdialog.title",
        description = "example.ace.confirmationdialog.description",
        example = "/resources/examples/ace/confirmationdialog/confirmationDialog.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDialog.xhtml",
                    resource = "/resources/examples/ace/confirmationdialog/confirmationDialog.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDialog.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/confirmationdialog/ConfirmationDialogBean.java")
        }
)
@Menu(
	title = "menu.ace.confirmationdialog.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.confirmationdialog.subMenu.main",
	                isDefault = true,
                    exampleBeanName = ConfirmationDialogBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.confirmationdialog.subMenu.modal",
                    exampleBeanName = ConfirmationDialogModalBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.confirmationdialog.subMenu.effect",
                    exampleBeanName = ConfirmationDialogEffectBean.BEAN_NAME)
    }
)
@ManagedBean(name= ConfirmationDialogBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDialogBean extends ComponentExampleImpl<ConfirmationDialogBean> implements Serializable {
    public static final String BEAN_NAME = "confirmationDialogBean";
    
	private String message = "Message";
	private String header = "Header";
	private int width = 300;
	private int height = 200;
    private boolean closable = false;

    public ConfirmationDialogBean() {
        super(ConfirmationDialogBean.class);
    }
	
	private String outcome = null;
	
	public void yes(ActionEvent actionEvent) { 
		outcome = "You clicked 'yes'";
	}
	
	public void no(ActionEvent actionEvent) { 
		outcome = "You clicked 'no'";
	}
	
	public String getOutcome() {
		return outcome;
	}
    
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean getClosable() {
		return closable;
	}
	
	public void setClosable(boolean closable) {
		this.closable = closable;
	}
}