/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.tableconfigpanel;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.component.UIComponent;

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
    tlddoc = "")
@ResourceDependencies({
    @ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
    @ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
    @ResourceDependency(library="icefaces.ace", name="core/core.js"),
    @ResourceDependency(library="icefaces.ace", name="tableconfigpanel/tableconfigpanel.js"),
    @ResourceDependency(library="icefaces.ace", name="tableconfigpanel/tableconfigpanel.css")
})

@ClientBehaviorHolder(events = {
    @ClientEvent(name="open", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
    @ClientEvent(name="submit", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
    @ClientEvent(name="cancel", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")},
    defaultEvent = "submit"
)
public class TableConfigPanelMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Allow the configuration of column order." )
    boolean columnOrderingConfigurable;
//    @Property(tlddoc = "Allow the configuration of column size.")
//    boolean columnSizingConfigurable;
    @Property(tlddoc = "Allow the configuration of column visibility.")
    boolean columnVisibilityConfigurable;
    @Property(tlddoc = "Allow the configuration of column headerText properties.")
    boolean columnNameConfigurable;
    @Property(tlddoc = "Allow the configuration of column sorting priority and directions.")
    boolean columnSortingConfigurable;
    @Property(name = "for", tlddoc="Component ID of the DataTable this ConfigPanel manipulates.")
    String forTarget;
    @Property(tlddoc = "Hides columns with configurable property set to false.",
              defaultValue = "false",
              defaultValueType = DefaultValueType.EXPRESSION)
    boolean hideDisabledRows;
    @Property(tlddoc = "Display modes for the 'open' control panel button. Available: paginator-button, first-col, last-col, plain",
              defaultValue = "first-col")
    String type;
}
