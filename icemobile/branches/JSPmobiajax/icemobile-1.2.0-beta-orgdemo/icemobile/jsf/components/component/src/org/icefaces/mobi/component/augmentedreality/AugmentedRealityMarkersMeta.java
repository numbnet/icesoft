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
        tagName = "augmentedRealityMarkers",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityMarkers",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityMarkersRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityMarkersBase",
        componentType = "org.icefaces.AugmentedRealityMarkers",
        rendererType = "org.icefaces.AugmentedRealityMarkersRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.AugmentedRealityMarkers",
        tlddoc = "This mobility component displays a set of three dimensional markers corresponding to the marker ID matching their index"
)

public class AugmentedRealityMarkersMeta extends UIComponentBaseMeta  {

    @Property(tlddoc="list iterator variable name")
    private String var;

    @Property(tlddoc="value containing marker items")
    private Object value;

    @Property(tlddoc="Marker String label")
    private String markerLabel;

    @Property(tlddoc="location marker URL")
    private String markerModel;

}
