/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.menu;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.model.MenuModel;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "menu",
        componentClass = "org.icefaces.ace.component.menu.Menu",
        rendererClass = "org.icefaces.ace.component.menu.MenuRenderer",
        generatedClass = "org.icefaces.ace.component.menu.MenuBase",
        extendsClass = "org.icefaces.ace.component.menu.AbstractMenu",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.Menu",
        rendererType = "org.icefaces.ace.component.MenuRenderer",
        tlddoc = "Menu is a navigation component with various customized modes like multi tiers, overlay " +
                "and nested menus."
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.ace", name = "jquery/ui/jquery-ui.css"),
        @ResourceDependency(library = "icefaces.ace", name = "wijmo/wijmo.css"),
        @ResourceDependency(library = "icefaces.ace", name = "jquery/jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "jquery/ui/jquery-ui.js"),
        @ResourceDependency(library = "icefaces.ace", name = "wijmo/wijmo.js"),
        @ResourceDependency(library = "icefaces.ace", name = "core/core.js"),
        @ResourceDependency(library = "icefaces.ace", name = "menu/menu.js")
})
public class MenuMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Javascript variable name of the wrapped widget.")
    private String widgetVar;

    @Property(tlddoc = "MenuModel instance to create menus programmatically. " +
            "For the menuitem and submenu components, use explicit ids, and " +
            "avoid long processing in the getter method for this property, " +
            "as it will be called multiple times, in every lifecycle.")
    private MenuModel model;

    @Property
    private String trigger;

    @Property
    private String my;

    @Property
    private String at;

    @Property(tlddoc = "Sets the way menu is placed on the page, when \"static\" menu is displayed in the normal flow," +
            " when set to \"dynamic\" menu is not on the normal flow allowing overlaying. Default value is \"static\".",
            defaultValue = "static")
    private String position;

    @Property(tlddoc = "Sets the tiered mode, when set to true menu will be rendered in different tiers.")
    private boolean tiered;

    @Property(defaultValue = "plain")
    private String type;

    @Property(tlddoc = "Sets the effect for the menu display, default value is FADE. Possible values are" +
            " \"FADE\", \"SLIDE\", \"NONE\". Use \"NONE\" to disable animation at all.", defaultValue = "fade")
    private String effect;

    @Property(tlddoc = "Sets the effect duration in seconds.", defaultValue = "400")
    private int effectDuration;

    @Property(tlddoc = "Style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    @Property(tlddoc = "zindex property to control overlapping with other elements.", defaultValue = "1")
    private int zindex;

    @Property(defaultValue = "Back")
    private String backLabel;

    @Property(defaultValue = "200")
    private int maxHeight;

    @Property(defaultValue = "click")
    private String triggerEvent;

    private AbstractMenu am; // need this for solving dependence on AbstractMenu when compiling MenuBase
}
