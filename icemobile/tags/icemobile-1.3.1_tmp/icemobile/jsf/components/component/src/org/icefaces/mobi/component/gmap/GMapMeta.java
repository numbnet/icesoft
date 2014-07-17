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

package org.icefaces.mobi.component.gmap;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.mobi.utils.TLDConstants;

/** The gMap Component Meta Configuration Class.
 */
@Component(
        tagName = "gMap",
        componentClass = "org.icefaces.mobi.component.gmap.GMap",
        rendererClass = "org.icefaces.mobi.component.gmap.GMapRenderer",
        generatedClass = "org.icefaces.mobi.component.gmap.GMapBase",
        componentType = "org.icefaces.GMap",
        rendererType = "org.icefaces.GMapRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.GMap",
        tlddoc = "This component renders a Google Maps v3 control.")
@ResourceDependencies({
    @ResourceDependency(library = "org.icefaces.component.util",
            name = "component.js") })
public class GMapMeta extends UIPanelMeta {

    /** */
    @Property(tlddoc = "The starting longitude for the map. "
            + "This will be overridden if an address is provided.",
            defaultValue = "-114.08538937568665")
    private String longitude;

    /** */
    @Property(tlddoc = "The starting latitude for the map. This will be "
            + "overridden if an address is provided.",
            defaultValue = "51.06757388616548")
    private String latitude;

    /** */
    @Property(tlddoc = "The starting zoom of the map element.",
            defaultValue = "5")
    private int zoomLevel;

    /** */
    @Property(tlddoc = "Additional options to be sent to the map. Check "
            + "google maps API for more specifics. Form is attribute:'value'.")
    private String options;

    /** */
    @Property(tlddoc = "Whether the map should be locating the specified "
            + "address. Default is false.", defaultValue = "false")
    private boolean locateAddress;

    /** */
    @Property(tlddoc = "Specifies whether the map has been initialized. "
            + "or not.", defaultValue = "false")
    private boolean intialized;

    /** */
    @Property(tlddoc = "Address to locate.")
    private String address;

    /** */
    @Property(tlddoc = "Map type to display by default. Possible values "
            + "are HYBRID, ROADMAP, SATELLITE and TERRAIN, case insensitive. ",
            defaultValue = "ROADMAP")
    private String type;

    /** */
    @Property(defaultValue = "", tlddoc = TLDConstants.STYLE)
    private String style;

    /** */
    @Property(defaultValue = "", tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

}
