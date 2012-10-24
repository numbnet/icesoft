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
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName         = "gMapAutocomplete",
        componentClass  = "org.icefaces.ace.component.gmap.GMapAutocomplete",
        rendererClass   = "org.icefaces.ace.component.gmap.GMapAutocompleteRenderer",
        generatedClass  = "org.icefaces.ace.component.gmap.GMapAutocompleteBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.GMapAutocomplete",
        rendererType    = "org.icefaces.ace.component.GMapAutocompleteRenderer",
		componentFamily = "org.icefaces.ace.component",
        tlddoc = "An Icesoft implementation of Google’s Places autocomplete tool. " +
                "Ace:gMapAutocomplete will create a text box which will provide locations that match the currently typed string, " +
                "then return information about the selected location such as the types Google has assigned to it, or the url to Google’s " +
                "information page on it."
)

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})

public class GMapAutocompleteMeta extends UIPanelMeta {
    @Property(tlddoc="Desired size of the input box",defaultValue="30")
    private String size;
    @Property(tlddoc="Styling options to be sent to the autocomplete box")
    private String style;
    @Property(tlddoc="Additional options to be sent to the info window displayed. " +
            "Leave blank for default, and set to 'off' to turn marker/window off. " +
            "Check google maps API for more specifics at https://developers.google.com/maps/documentation/javascript/reference#AutocompleteOptions." +
            " Form is attribute:'value'", defaultValue="none")
    private String windowOptions;
    @Property(tlddoc="The location to send the text value of the address selected by gMapAutocomplete")
    private String address;
    @Property(tlddoc="The location to send the lat/lng coordinates of the address selected by gMapAutocomplete")
    private String latLng;
    @Property(tlddoc="The location to send the array of typles that Google determines match the address selected by gMapAutocomplete")
    private String types;
    @Property(tlddoc="The location to send the url attributed to the address selected by gMapAutocomplete")
    private String url;
    @Property(tlddoc="Value to shift the map after finding new location, in pixels. (useful for mobile devices) Form is x,y.", defaultValue="0,0")
    private String offset;

}
