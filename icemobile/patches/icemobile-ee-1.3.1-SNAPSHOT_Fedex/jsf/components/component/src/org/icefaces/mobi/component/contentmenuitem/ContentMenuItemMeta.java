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
package org.icefaces.mobi.component.contentmenuitem;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.mobi.utils.TLDConstants;


@Component(
        tagName = "contentMenuItem",
        componentClass = "org.icefaces.mobi.component.contentmenuitem.ContentMenuItem",
        rendererClass = "org.icefaces.mobi.component.contentmenuitem.ContentMenuItemRenderer",
        generatedClass = "org.icefaces.mobi.component.contentmenuitem.ContentMenuItemBase",
        extendsClass = "javax.faces.component.UICommand",
        componentType = "org.icefaces.ContentMenuItem",
        rendererType = "org.icefaces.ContentMenuItemRenderer",
        componentFamily = "org.icefaces.ContentMenuItem",
        tlddoc = "This component fires an actionListener from a menu item.  The value can be a url, " +
                " or, more typically, an id to a panel in the contentStack that is controlled by its" +
                " parent, which must be either a ContentStackMenu or a ContentNavBar. If it's a child of" +
                " the ContentStackMenu, then it's rendered as a list item.  If a child of the  " +
                " ContentNavBar, it is rendered as a button on a tool bar.  If the value attribute "+
                " is null, then it becomes a menu heading and no selection of the contentStack is done. A" +
                " non-null value must represent the id of the contentPane in the contentStack to make " +
                " current." +
                " If the contentStackMenu has attribute accordion equal to true, then a null value attribute " +
                " will render an accordion handle with accordion functionality.  The following contentMenuItems " +
                " with non-null values will be children of that accordion pane, to the next, etc. Beta feature"
)

public class ContentMenuItemMeta extends UICommandMeta {
     @Property(defaultValue = "true",
            tlddoc = TLDConstants.SINGLESUBMIT)
     private boolean singleSubmit;

     @Property(tlddoc = "The URL to be navigated to when then menuitem is clicked. ")
     private String url;

     @Property(defaultValue = "false",
                tlddoc = TLDConstants.DISABLED)
     private boolean disabled;

     @Property(tlddoc = "The menu item label. ", required = Required.yes)
     private String label;

     @Property(tlddoc = "The contentPane id that will be displayed when selecting this button." +
             "  This will default to the first contentPane if the \"url\" attribute is null. ")
     private String value;

     @Property(tlddoc = TLDConstants.STYLECLASS)
     private String styleClass;

     @Property(tlddoc = TLDConstants.STYLE)
     private String style;
     
     @Property(tlddoc = "Font Awesome icon")
     private String icon;
     
     @Property(tlddoc = "Icon placement, left or right", defaultValue = "left")
     private String iconPlacement;



}
