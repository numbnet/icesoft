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

package org.icefaces.samples.showcase.example.ace.contextMenu;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = ContextMenuBean.BEAN_NAME,
        title = "example.ace.contextMenu.component.title",
        description = "example.ace.contextMenu.component.description",
        example = "/resources/examples/ace/contextMenu/contextMenuComponent.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "contextMenuComponent.xhtml",
                        resource = "/resources/examples/ace/contextMenu/contextMenuComponent.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ContextMenuComponent.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/contextMenu/ContextMenuComponent.java")
        }
)
@ManagedBean(name = ContextMenuComponent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ContextMenuComponent extends ComponentExampleImpl<ContextMenuComponent> implements Serializable {
    public static final String BEAN_NAME = "contextMenuComponent";

    private String actionDescription;

    public ContextMenuComponent() {
        super(ContextMenuComponent.class);
        actionDescription = "";
    }

    public String performAction() {
        String actionType = FacesUtils.getRequestParameter("actionType");
        actionDescription = "Action performed: " + actionType;
        return null;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

}
