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
package org.icefaces.mobi.component.layoutmenu;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ClientBehaviorHolder(events = {
	@ClientEvent(name="click", javadoc="Fired when a layoutMenuItem is selected or clicked",
            tlddoc="Fired when layoutMenuItem is clicked", defaultRender="@all",
            defaultExecute="@all")}, defaultEvent="click")
@Component(
        tagName = "layoutMenuItem",
        componentClass = "org.icefaces.mobi.component.layoutmenu.LayoutMenuItem",
        rendererClass = "org.icefaces.mobi.component.layoutmenu.LayoutMenuItemRenderer",
        generatedClass = "org.icefaces.mobi.component.layoutmenu.LayoutMenuItemBase",
        extendsClass = "javax.faces.component.UICommand",
        componentType = "org.icefaces.component.LayoutMenuItem",
        rendererType = "org.icefaces.component.LayoutMenuItemRenderer",
        componentFamily = "org.icefaces.LayoutMenuItem",
        tlddoc = "This component fires an actionListener from a menu item.  the value can be a url, " +
                " an id to a panel in the contentStack that is an attribute in the LayoutMenu tag, or can " +
                " be a heading in a group of menu items. " +
                "It has the same functionality of a regular jsf command  "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class LayoutMenuItemMeta extends UICommandMeta {
     @Property(defaultValue = "true",
            tlddoc = "When singleSubmit is true, triggering an action on this component will submit" +
                    " and execute this component only. Equivalent to <f:ajax execute='@this' render='@all'>." +
                    " When singleSubmit is false, triggering an action on this component will submit and execute " +
                    " the full form that this component is contained within." +
                    " The default value is false.")
     private boolean singleSubmit;

     @Property(tlddoc = "Url to be navigated when menuitem is clicked. Outside of the LayoutMenu contentStack.")
     private String url;

     @Property(defaultValue = "false",
                tlddoc = "disabled property. If true no input may be submitted via this" +
                        "component.  ")
     private boolean disabled;

     @Property(tlddoc="label of layoutMenuItem", required=Required.yes)
     private String label;

     @Property(defaultValue="false",
             tlddoc="true if the item is not a link but a header for a group of menu items")
     private boolean menuHeader;

     @Property(tlddoc="id of panel in contentStack that will be displayed when selecting this item." +
             "  default value will be first contentPane in the stack as long as url is blank as well")
     private String value;

     @Property(tlddoc = "style class of the component, rendered on the div root of the component")
     private String styleClass;

     @Property(tlddoc = "style of the component, rendered on the div root of the component")
     private String style;

     /*  Do we want to support this..perhaps future enhancement?
     @Property(tlddoc = "Path of the menuitem image.")
     private String icon;        */

 /*    @Property(defaultValue = "false", tlddoc = "immediate as per jsf specs")
     private boolean immediate;  */

  /* TODO implement panelConf and submitNotif....
    @Property(tlddoc="id of panelConfirmation to be used with this component")
     private String panelConfirmation;

     @Property(tlddoc="id of blocking submitNotification panel which blocks any further access to page until process is complete")
     private String submitNotification;   */

}
