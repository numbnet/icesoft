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

package org.icefaces.mobi.component.gmap;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

/** The gMapMarker component configuration meta class. */
@Component(
        tagName         = "gMapMarker",
        componentClass  = "org.icefaces.mobi.component.gmap.GMapMarker",
        rendererClass   = "org.icefaces.mobi.component.gmap.GMapMarkerRenderer",
        generatedClass  = "org.icefaces.mobi.component.gmap.GMapMarkerBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.mobi.component.GMapMarker",
        rendererType    = "org.icefaces.mobi.component.GMapMarkerRenderer",
        componentFamily = "org.icefaces.mobi.component"
        )

@ResourceDependencies({
    @ResourceDependency(library = "org.icefaces.component.util",
            name = "component.js")
})

public class GMapMarkerMeta extends UIPanelMeta {

    /** */
    @Property(tlddoc = "The longitude for the marker.")
    private String longitude;

    /** */
    @Property(tlddoc = "The latitude for the marker.")
    private String latitude;
}
