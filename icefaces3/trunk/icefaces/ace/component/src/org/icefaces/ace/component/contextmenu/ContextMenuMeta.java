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

package org.icefaces.ace.component.contextmenu;

import org.icefaces.ace.component.menu.AbstractMenu;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.model.MenuModel;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "contextMenu",
        componentClass = "org.icefaces.ace.component.contextmenu.ContextMenu",
        rendererClass = "org.icefaces.ace.component.contextmenu.ContextMenuRenderer",
        generatedClass = "org.icefaces.ace.component.contextmenu.ContextMenuBase",
        extendsClass = "org.icefaces.ace.component.menu.AbstractMenu",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.ContextMenu",
        rendererType = "org.icefaces.ace.component.ContextMenuRenderer",
        tlddoc = "ContextMenu provides a popup menu that is displayed on mouse right-click event." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/ContextMenu\">ContextMenu Wiki Documentation</a>."
)
@ResourceDependencies({
		@ResourceDependency(library="icefaces.ace", name="util/combined.css"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-menu.js")
})
public class ContextMenuMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Javascript variable name of the wrapped widget.")
    private String widgetVar;

    @Property(name = "for", tlddoc = "Server side id of the component to attach to.")
    private String forValue;

    @Property(tlddoc = "Style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    @Property(tlddoc = "zindex property to control overlapping with other elements.", defaultValue = "1")
    private int zindex;

    @Property(tlddoc = "Sets the effect for the menu display. Standard jQuery effects are supported (see wiki page for exceptions).", defaultValue = "fade")
    private String effect;

    @Property(tlddoc = "Sets the effect duration in milliseconds.", defaultValue = "400")
    private int effectDuration;

    @Property(tlddoc = "MenuModel instance to create menus programmatically. " +
            "For the menuitem and submenu components, use explicit ids, and " +
            "avoid long processing in the getter method for this property, " +
            "as it will be called multiple times, in every lifecycle.")
    private MenuModel model;

    private AbstractMenu am; // need this to resolve dependence on AbstractMenu when compiling Base class
}
