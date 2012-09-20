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

package org.icefaces.ace.component.submitmonitor;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import java.lang.Boolean;
import java.lang.String;

@Component(
    tagName = "submitMonitor",
    componentClass  = "org.icefaces.ace.component.submitmonitor.SubmitMonitor",
    rendererClass   = "org.icefaces.ace.component.submitmonitor.SubmitMonitorRenderer",
    generatedClass  = "org.icefaces.ace.component.submitmonitor.SubmitMonitorBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.ace.component.SubmitMonitor",
    rendererType    = "org.icefaces.ace.component.SubmitMonitorRenderer",
    componentFamily = "org.icefaces.ace.SubmitMonitor",
    tlddoc = "Monitors submits to the server, and indicates the status of " +
        "the submits, server and network connection, and session validity. " +
        "Supports configurable text labels and image state indicators, or " +
        "facets for a fully configurable UI. Optionally uses an overlay for " +
        "UI blocking during submits."
)
@ResourceDependencies({
    @ResourceDependency(library = "icefaces.ace", name = "util/combined.css"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
})
public class SubmitMonitorMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Label to be displayed on the submitMonitor when no submit is in progress.")
    String idleLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor while a submit is in progress.")
    String activeLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor when there is a server error.")
    String serverErrorLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor when there is a network error.")
    String networkErrorLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor when the session is expired.")
    String sessionExpiredLabel;

    @Property(name="for", tlddoc = "Specify space separated list of " +
        "components, by their for style search strings, so that those " +
        "components and, recursively, all of their children, will be " +
        "monitored by this component when they act as the source for " +
        "submits. When this property is empty or unspecified, this " +
        "component will monitor all submits from all sources.")
    String For;

//    @Property(tlddoc = "")
//    Boolean showIcon;
//
//    @Property(tlddoc = "Enable to display an hourglass when hovering over the translucent plane displayed by " +
//            "the 'blockUI' feature.")
//    Boolean showHourglass;

    @Property(tlddoc = "When enabled, display a translucent overlay on a " +
        "portion of the window, and only show the submitMonitor UI when the " +
        "connection is not idle and a submit is underway. This property " +
        "specifies on what portion of the window to show the overlay: " +
        "\"@all\" means the whole document body, \"@source\" means only over " +
        "the component that originated the request, or a for style " +
        "component search string may be given to specify a component. " +
        "Finally, \"@none\" means to disable the overlay and have the " +
        "submitMonitor UI always present where it has been placed in the " +
        "page.",
        defaultValue = "@all",
        defaultValueType = DefaultValueType.STRING_LITERAL)
    String blockUI;

//    @Property(tlddoc = "Disabling displays the translucent plane of the 'blockUI' feature over the component region(s) " +
//            "defined by the 'for' attribute to rather than obscuring the whole page.")
//    Boolean blockWindow;
//
//    @Property(tlddoc = "Enable to prevent keyboard input when blockUI is enabled.",
//            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
//    Boolean blockKeyboard;

    @Property(tlddoc = "When blockUI is enabled, and this property is true, " +
        "the submitMonitor will display centered over the translucent overlay.",
        defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    Boolean autoCenter;

    @Facets
    class FacetsMeta {
        @Facet
        UIComponent idle;

        @Facet
        UIComponent active;

        @Facet
        UIComponent serverError;

        @Facet
        UIComponent networkError;

        @Facet
        UIComponent sessionExpired;
    }
}

