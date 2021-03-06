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
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.confirmationdialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
		parent = ConfirmationDialogBean.BEAN_NAME,
        title = "example.ace.confirmationdialog.modal.title",
        description = "example.ace.confirmationdialog.modal.description",
        example = "/resources/examples/ace/confirmationdialog/confirmationDialogModal.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDialogModal.xhtml",
                    resource = "/resources/examples/ace/confirmationdialog/confirmationDialogModal.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDialogModal.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/confirmationdialog/ConfirmationDialogModalBean.java")
        }
)
@ManagedBean(name= ConfirmationDialogModalBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDialogModalBean extends ComponentExampleImpl<ConfirmationDialogModalBean> implements Serializable {
    public static final String BEAN_NAME = "confirmationDialogModalBean";
	
    public ConfirmationDialogModalBean() {
        super(ConfirmationDialogModalBean.class);
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
}