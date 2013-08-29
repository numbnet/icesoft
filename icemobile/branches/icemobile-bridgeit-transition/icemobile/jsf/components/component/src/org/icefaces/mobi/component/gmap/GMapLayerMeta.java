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

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

/** The gMapLayer component configuration meta class. */
@Component(
        tagName         = "gMapLayer",
        componentClass  = "org.icefaces.mobi.component.gmap.GMapLayer",
        rendererClass   = "org.icefaces.mobi.component.gmap.GMapLayerRenderer",
        generatedClass  = "org.icefaces.mobi.component.gmap.GMapLayerBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.mobi.component.GMapLayer",
        rendererType    = "org.icefaces.mobi.component.GMapLayerRenderer",
        componentFamily = "org.icefaces.mobi.component",
        tlddoc = "A version 3.0 API google map interface."
        )

@ResourceDependencies({
    @ResourceDependency(library = "org.icefaces.component.util",
            name = "component.js")
})

public class GMapLayerMeta extends UIPanelMeta {

    /** */
    @Property(tlddoc = "The type of layer that you wish to create. Options "
            + "are Bicycling, Traffic, Transit, Fusion and Kml. "
            + "(Case insensitive.)")
    private String layerType;

    /** */
    @Property(tlddoc = "The URL from which to draw KML data.")
    private String url;

    /** */
    @Property(tlddoc = "Additional options to be sent to the layer. "
            + "Check google maps API for more specifics. "
            + "Form is attribute:'value'. ",
            defaultValue = "Skip")
    private String options;

    /** */
    @Property(tlddoc = "Whether to make the chosen layer visible or not. ")
    private boolean visible;

}
