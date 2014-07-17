/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import org.icefaces.mobi.utils.TLDConstants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "submitNotification",
        componentClass = "org.icefaces.mobi.component.submitnotification.SubmitNotification",
        rendererClass = "org.icefaces.mobi.component.submitnotification.SubmitNotificationRenderer",
        generatedClass = "org.icefaces.mobi.component.submitnotification.SubmitNotificationBase",
        componentType = "org.icefaces.SubmitNotification",
        rendererType = "org.icefaces.SubmitNotificationRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.SubmitNotification",
        tlddoc = "submitNoftification " +
                "renders a panel, to be used with any mobi commandButton, which blocks any other submission until a response is recieved and the update completes.")

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class SubmitNotificationMeta extends UIPanelMeta {

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

}
