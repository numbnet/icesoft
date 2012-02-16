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
package org.icefaces.mobi.component.submitnotification;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

@Component(
        tagName = "submitNotification",
        componentClass = "org.icefaces.mobi.component.submitnotification.SubmitNotification",
        rendererClass = "org.icefaces.mobi.component.submitnotification.SubmitNotificationRenderer",
        generatedClass = "org.icefaces.mobi.component.submitnotification.SubmitNotificationBase",
        componentType = "org.icefaces.SubmitNotification",
        rendererType = "org.icefaces.SubmitNotificationRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.SubmitNotification",
        tlddoc = "This mobility component " +
                "renders a panel to be used with any mobi commandButton which blocks any other submission for the duration "+
                " the process triggered by the button until the update is complete")
public class SubmitNotificationMeta extends UIPanelMeta {

    @Property(tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

}
