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