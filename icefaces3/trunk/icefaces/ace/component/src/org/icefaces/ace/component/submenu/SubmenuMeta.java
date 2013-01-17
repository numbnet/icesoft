/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.component.submenu;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "submenu",
        componentClass = "org.icefaces.ace.component.submenu.Submenu",
        generatedClass = "org.icefaces.ace.component.submenu.SubmenuBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.Submenu",
        tlddoc = "Submenu is nested in a menu component and represents a navigation group." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Submenu\">Submenu Wiki Documentation</a>."
)
public class SubmenuMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Label of the submenu header.")
    private String label;

    @Property(tlddoc = "CSS class name(s) containing the styling to display an icon.")
    private String icon;

    @Property(tlddoc = "Style of the submenu label.")
    private String style;

    @Property(tlddoc = "StyleClass of the submenu label.")
    private String styleClass;
	
    @Property(tlddoc = "Boolean value to disable/enable the submenu. The submenu label will still be shown but with different styling, and clicking or hovering on it will not display its children submenus and menu items.")
    private boolean disabled;
}
