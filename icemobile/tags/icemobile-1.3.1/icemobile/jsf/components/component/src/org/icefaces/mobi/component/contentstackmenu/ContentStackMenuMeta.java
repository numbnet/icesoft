/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.contentstackmenu;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIDataMeta;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "contentStackMenu",
        componentClass = "org.icefaces.mobi.component.contentstackmenu.ContentStackMenu",
        rendererClass = "org.icefaces.mobi.component.contentstackmenu.ContentStackMenuRenderer",
        generatedClass = "org.icefaces.mobi.component.contentstackmenu.ContentStackMenuBase",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentType = "org.icefaces.ContentStackMenu",
        rendererType = "org.icefaces.ContentStackMenuRenderer",
        componentFamily = "org.icefaces.ContentStackMenu",
        tlddoc = "contentStackMenu renders a menu represented by a collection of contentMenuItems " +
                "and is meant to be used with a contentStack component for layout and navigation " +
                "through the designated contentStack.  When in a small view, the contentStackMenu is " +
                "meant to be within one of the contentPane children of the contentStack.  When in " +
                "a large view with splitPane, it is meant to be a sibling to the contentStack and not within" +
                " a contentPane itself.  In this way the component knows how to behave regarding transisitons." +
                " Currently defined transitions are sliding panes within small view only." +
                " Related components for navigation through contentStack are contentMenuItem, which is" +
                " only legitimate child component of this one, and contentNavBar, which also takes " +
                "contentMenuItem as children. This component may take a list of contentMenuItems as children, " +
                "or a Collection can be value bound to the value attribute with a var attribute to a single" +
                " contentMenuItem child to be iterated over. "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ContentStackMenuMeta extends UISeriesBaseMeta {

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
     private String styleClass;

    @Property(tlddoc = "The id of contentStack this menu will be responsible for manipulating."+
           " The contentStack either needs to be a sibling within the same form, or it "+
            " must use the exact jsf clientId (ie: myform:mystack).",
             required=Required.yes)
    private String contentStackId;

    @Property(tlddoc = "The id of contentPane that is currentId in contentStack. ContentMenuItem will " +
            "modify this value, keeping the state of the contentStack for this component." +
            " It also allows value bindings in a bean so contentStack and this component to can stay in sync.")
    private String selectedPane;

    @Property(defaultValue="false", tlddoc="Determines if menu acts as an accordion.")
    private boolean accordion;

}
