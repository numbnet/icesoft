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
package org.icefaces.mobi.component.contentstack;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;

import javax.faces.component.UIComponent;


@Component(
        tagName = "contentStack",
        componentClass = "org.icefaces.mobi.component.contentstack.ContentStack",
        rendererClass = "org.icefaces.mobi.component.contentstack.ContentStackRenderer",
        generatedClass = "org.icefaces.mobi.component.contentstack.ContentStackBase",
        componentType = "org.icefaces.ContentStack",
        rendererType = "org.icefaces.ContentStackRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentStack",
        tlddoc = "This mobility component controls which of it's child panels is rendered. Child Panels must be " +
                " contentPane components.  "
)
public class ContentStackMeta extends UIPanelMeta {

     @Property( tlddoc="id of the panel that is visible in this stack of panels.  If one is selected, then a single" +
             " column view is shown.")
     private String currentId;

     @Property(tlddoc = "style will be rendered on the root element of this " +
     "component.")
     private String style;

     @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
     private String styleClass;

     @Property(tlddoc=" id of contentStackMenu used with this stack")
     private String contentMenuId;

}
