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
        tagName         = "gMapAutocomplete",
        componentClass  = "org.icefaces.ace.component.gmap.GMapAutocomplete",
        rendererClass   = "org.icefaces.ace.component.gmap.GMapAutocompleteRenderer",
        generatedClass  = "org.icefaces.ace.component.gmap.GMapAutocompleteBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.GMapAutocomplete",
        rendererType    = "org.icefaces.ace.component.GMapAutocompleteRenderer",
		componentFamily = "org.icefaces.ace.component"
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
    @Property(tlddoc="The id of an input style component used to store the recieved value", defaultValue="none")
    private String input;
    @Property(tlddoc="The id of a component used to submit the recieved value to the bean", defaultValue="none")
    private String submit;
    @Property(tlddoc="Additional options to be sent to the marker. Check google maps API for more specifics. Form is attribute:'value'", defaultValue="none")
    private String options;
}
