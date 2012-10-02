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

package org.icefaces.ace.component.tableconfigpanel;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
    tagName = "tableConfigPanel",
    extendsClass = "javax.faces.component.UIComponentBase",
    rendererClass = "org.icefaces.ace.component.tableconfigpanel.TableConfigPanelRenderer",
    rendererType = "org.icefaces.ace.component.TableConfigPanelRenderer",
	generatedClass = "org.icefaces.ace.component.tableconfigpanel.TableConfigPanelBase",
    componentType = "org.icefaces.ace.component.TableConfigPanel",
    componentClass = "org.icefaces.ace.component.tableconfigpanel.TableConfigPanel",
    componentFamily = "org.icefaces.ace.TableConfigPanel",
    tlddoc = "<p>Renders a hidden panel with controls to configure column features. The table whose columns are to be configured is targeted with the 'for' attribute. The location of the button to reveal this panel is configured via the 'type' attribute.</p>" +
             "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/TableConfigPanel\">TableConfigPanel Wiki Documentation</a>.</p>")
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="util/combined.css"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-datatable.js")
})

@ClientBehaviorHolder(events = {
    @ClientEvent(name="open", javadoc="Fired when the TableConfigPanel is shown.",
            tlddoc="Fired when the TableConfigPanel is shown.", defaultRender="@all", defaultExecute="@all"),
    @ClientEvent(name="submit", javadoc="Fired when the TableConfigPanel submits its changes and closes.",
            tlddoc="Fired when the TableConfigPanel submits its changes and closes.", defaultRender="@all", defaultExecute="@all"),
    @ClientEvent(name="cancel", javadoc="Fired when the TableConfigPanel cancel its changes and closes.",
            tlddoc="Fired when the TableConfigPanel cancel its changes and closes.", defaultRender="@all", defaultExecute="@all")},
    defaultEvent = "submit"
)
public class TableConfigPanelMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Enable the configuration of column order." )
    boolean columnOrderingConfigurable;

//    @Property(tlddoc = "Allow the configuration of column size.")
//    boolean columnSizingConfigurable;

    @Property(tlddoc = "Enable the configuration of column visibility.")
    boolean columnVisibilityConfigurable;

    @Property(tlddoc = "Enable the configuration of column headerText properties.")
    boolean columnNameConfigurable;

    @Property(tlddoc = "Enable the configuration of column sorting priority and directions.")
    boolean columnSortingConfigurable;

    @Property(name = "for", tlddoc="Defines the component ID of the DataTable this ConfigPanel manipulates.")
    String forTarget;

    @Property(tlddoc = "Enable to hide columns with configurable property set to false, rather than render them with a disabled style.",
              defaultValue = "false",
              defaultValueType = DefaultValueType.EXPRESSION)
    boolean hideDisabledRows;

    @Property(tlddoc = "Defines the display mode for the 'open' control panel button. Available options: first-col, last-col, in-col-right, in-col-left",
              defaultValue = "first-col")
    String type;

    @Property(tlddoc = "Defines the column to render the 'launch' panel button in when using the 'in-col-left' or 'in-col-right' type options.")
    String inColumnId;
}
