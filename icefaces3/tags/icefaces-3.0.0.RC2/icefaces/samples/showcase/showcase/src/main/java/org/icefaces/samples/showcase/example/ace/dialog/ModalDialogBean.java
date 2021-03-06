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
        parent = DialogBean.BEAN_NAME,
        title = "example.ace.dialog.modalDialog.title",
        description = "example.ace.dialog.modalDialog.description",
        example = "/resources/examples/ace/dialog/modalDialog.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="modalDialog.xhtml",
                    resource = "/resources/examples/ace/dialog/modalDialog.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ModalDialog.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dialog/ModalDialogBean.java")
        }
)
@ManagedBean(name= ModalDialogBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ModalDialogBean extends ComponentExampleImpl<ModalDialogBean> implements Serializable
{
    public static final String BEAN_NAME = "modalDialogBean";
    private boolean draggable;
    private boolean modal;

    public ModalDialogBean() 
    {
        super(ModalDialogBean.class);
        
        draggable = false;
        modal = false;
    }
    
    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }
}