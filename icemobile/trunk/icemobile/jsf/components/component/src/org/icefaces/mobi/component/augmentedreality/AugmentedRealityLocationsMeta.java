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

package org.icefaces.mobi.component.augmentedreality;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import javax.faces.model.SelectItem;
import javax.el.MethodExpression;
import java.util.List;

@Component(
        tagName = "augmentedRealityLocations",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocations",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocationsRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocationsBase",
        componentType = "org.icefaces.AugmentedRealityLocations",
        rendererType = "org.icefaces.AugmentedRealityLocationsRenderer",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentFamily = "org.icefaces.AugmentedRealityLocations",
        tlddoc = "This mobility component ....."
)

public class AugmentedRealityLocationsMeta extends UISeriesBaseMeta{

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(defaultValue = "false",
            tlddoc = "disabled property. If true no input may be submitted via this" +
                    "component.  Is required by aria specs")
    private boolean disabled;

    @Property(tlddoc="selected location item")
    private String selectedLocationItem;

}
