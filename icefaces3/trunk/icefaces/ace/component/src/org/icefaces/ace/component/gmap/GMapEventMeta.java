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

package org.icefaces.ace.component.gmap;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "gMapEvent",
        componentClass = "org.icefaces.ace.component.gmap.GMapEvent",
        rendererClass = "org.icefaces.ace.component.gmap.GMapEventRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapEventBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapEvent",
        rendererType = "org.icefaces.ace.component.GMapEventRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapLayer component creates one of a variety of layers on top of the parent ace:gMap, to enhance or display new information. " +
                "Google provides Bicycling, Traffic and Transit layers, which display bike paths, traffic flow and public transportations routes respectively. " +
                "Alternatively, a user defined kml file can also be used." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ResourceDependencies({
        @ResourceDependency(library = "icefaces.ace", name = "jquery/ui/jquery-ui.css"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
})

public class GMapEventMeta extends UIPanelMeta {

    @Property(tlddoc = "The type of event that you want the script to execute on. The script will execute when the parent component of this tag fires the chosen event. Valid types vary based on parent tag, but can be found under the parent's mention in the google API.")
    private String eventType;

    @Property(tlddoc = "The script to be executed when the chosen event is fired. Use the variable 'map' to refer to the parent gMap and 'component' to refer to the parent tag. ")
    private String script;

    @Property(tlddoc = "The handler type that is to be created for this event. Valid types are (case insensitive) 'DomListener', 'DomListenerOnce','Listener', 'ListenerOnce' and 'Trigger'. The explanation for these listeners can be found at the Google API here: https://developers.google.com/maps/documentation/javascript/reference#MapsEventListener.", defaultValue = "DomListener")
    private String handlerType;

}
