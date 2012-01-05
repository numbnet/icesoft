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
        tlddoc = "Submenu is nested in a menu component and represents a navigation group."
)
public class SubmenuMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Label of the submenu header.")
    private String label;

    @Property(tlddoc = "Path of the submenu image.")
    private String icon;

    @Property(tlddoc = "Style of the submenu label.")
    private String style;

    @Property(tlddoc = "StyleClass of the submenu label.")
    private String styleClass;
}
