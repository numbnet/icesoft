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
package org.icefaces.mobi.component.contentpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Required;

import javax.faces.component.UIComponent;

@Component(
        tagName = "contentPane",
        componentClass = "org.icefaces.mobi.component.contentpane.ContentPane",
        rendererClass = "org.icefaces.mobi.component.contentpane.ContentPaneRenderer",
        generatedClass = "org.icefaces.mobi.component.contentpane.ContentPaneBase",
        componentType = "org.icefaces.ContentPane",
        rendererType = "org.icefaces.ContentPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentPane",
        tlddoc = "This mobility component is a child of several different layout containers.  No client behavior" +
                " goes with this component.  In order to have any client-side behavior, it must be used with one" +
                " of the other layout components.  Children of contentStack, accordion or tabSet, or any other mobility" +
                " component which implements ContentPaneController.  The cacheType determines whether the children" +
                " of this component/panel  are " +
                " a) tobeconstructed = constructed in the server side component tree" +
                " b) constructed = children are constructed and present in server side component tree" +
                " c) client = children of this component are not only constructed but also are rendered" +
                "    on the client --best for static dataa  "
)

public class ContentPaneMeta extends UIPanelMeta{

     @Property(defaultValue="false", tlddoc=" makes sense to have the scrollable here as it will need certain style attributes for " +
            " and child panel to scroll within it. If accordionTitle is not null, then this should default to false")
     private boolean scrollable;

     @Property(defaultValue=" ", tlddoc="used when ContentPane is child of Accordion or tabSet")
     private String title;

     @Property(tlddoc = "style will be rendered on the root element of this " +
     "component.")
     private String style;

     @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
     private String styleClass;

     @Property(defaultValue="constructed",
             tlddoc = "cache type.  Options currently include client, constructed and tobeconstructed")
     private String cacheType;

}
