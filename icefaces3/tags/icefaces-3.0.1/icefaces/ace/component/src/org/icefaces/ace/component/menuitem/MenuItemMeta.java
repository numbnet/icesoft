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

package org.icefaces.ace.component.menuitem;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;

import javax.faces.application.ResourceDependencies;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName = "menuItem",
        componentClass = "org.icefaces.ace.component.menuitem.MenuItem",
        generatedClass = "org.icefaces.ace.component.menuitem.MenuItemBase",
        extendsClass = "javax.faces.component.UICommand",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.MenuItem",
        tlddoc = "MenuItem is used by various menu components" +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/MenuItem\">MenuItem Wiki Documentation</a>."
)
@ResourceDependencies({

})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="", tlddoc="Triggers when the menu item is clicked or selected by any other means.", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class MenuItemMeta extends UICommandMeta {
    @Property(tlddoc = "Url to be navigated when menuitem is clicked.")
    private String url;

    @Property(tlddoc = "Target type of url navigation.")
    private String target;

    @Property(tlddoc = "Text to display additional information.")
    private String helpText;

    @Property(tlddoc = "Style of the menuitem label.")
    private String style;

    @Property(tlddoc = "StyleClass of the menuitem label.")
    private String styleClass;

    @Property(tlddoc = "Javascript event handler for click event. If this function explicitly returns 'false', then the request to the server will be cancelled.")
    private String onclick;

    @Property(tlddoc = "Path of the menuitem image.")
    private String icon;
}
