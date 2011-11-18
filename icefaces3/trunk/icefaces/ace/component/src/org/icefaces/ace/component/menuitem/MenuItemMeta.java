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

package org.icefaces.ace.component.menuitem;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;

import javax.faces.application.ResourceDependencies;

@Component(
        tagName = "menuitem",
        componentClass = "org.icefaces.ace.component.menuitem.MenuItem",
        generatedClass = "org.icefaces.ace.component.menuitem.MenuItemBase",
        extendsClass = "javax.faces.component.UICommand",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.MenuItem",
        tlddoc = "MenuItem is used by various menu components"
)
@ResourceDependencies({

})
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

    @Property(tlddoc = "Javascript event handler for click event.")
    private String onclick;

    @Property(tlddoc = "Client side id of the component(s) to be updated after async partial submit request.")
    private String update;

    @Property(tlddoc = "Component id(s) to process partially instead of whole view.")
    private String process;

    @Property(tlddoc = "Javascript handler to execute before ajax request is begins.")
    private String onstart;

    @Property(tlddoc = "Javascript handler to execute when ajax request is completed.")
    private String oncomplete;

    @Property(tlddoc = "Javascript handler to execute when ajax request fails.")
    private String onerror;

    @Property(tlddoc = "Javascript handler to execute when ajax request succeeds.")
    private String onsuccess;

    @Property(tlddoc = "Global ajax requests are listened by ajaxStatus component," +
            " setting global to false will not trigger ajaxStatus.", defaultValue = "true")
    private boolean global;

    @Property(tlddoc = "Specifies async mode.")
    private boolean async;

    @Property(tlddoc = "Specifies submit mode.", defaultValue = "true")
    private boolean ajax;

    @Property(tlddoc = "Path of the menuitem image.")
    private String icon;
}
