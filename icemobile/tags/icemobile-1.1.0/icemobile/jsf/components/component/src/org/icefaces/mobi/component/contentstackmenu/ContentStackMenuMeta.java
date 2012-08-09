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
        tlddoc = "This component renders a menu represented by a collection of contentMenuItem " +
                " and is meant to be used with a contentStack component for layout "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ContentStackMenuMeta extends UISeriesBaseMeta {
    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(tlddoc = "id of contentStack this menu will be responsible for manipulating",
             required=Required.yes)
    private String contentStackId;

    @Property(tlddoc = "id of either menuItem or contentPane that menuItem reflects..not sure which yet")
    private String selectedPane;

}
