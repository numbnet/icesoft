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

package org.icefaces.mobi.component.dataview;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
    tagName = "dataView",
    componentClass = "org.icefaces.mobi.component.dataview.DataView",
    rendererClass = "org.icefaces.mobi.component.dataview.DataViewRenderer",
    generatedClass = "org.icefaces.mobi.component.dataview.DataViewBase",
    componentType = "org.icefaces.DataView",
    rendererType = "org.icefaces.DataViewRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.icefaces.DataView",
    tlddoc = "The DataView component renders a table region for iterative output using columns " +
            "defined with the required DataViewColumns component child, and a detail region for " +
            "extensive viewing (and editing) of the row objects of the table model. The detail region" +
            "is defined with the DataViewDetails child component and it's contents are displayed when a " +
            "row of table region is activated, typically by a row tap."
)
@ResourceDependencies({
    @ResourceDependency( library = "org.icefaces.component.util", name = "component.js" )
})
public class DataViewMeta extends UIComponentBaseMeta {
    @Property(tlddoc = TLDConstants.DISABLED)
    boolean disabled;

    @Property(tlddoc = TLDConstants.STYLE)
    String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    String styleClass;

    @Property(tlddoc = "Define the Expression Language variable name to be given to the row object during iterative rendering, " +
            "either in the ValueExpressions of a DataViewColumnModel defining a table cell or within the components of the detail region.")
    String var;

    @Property(tlddoc = "Define the Expression Language variable name to be given to the index of the row object during iterative rendering, " +
            "either in the ValueExpressions of a DataViewColumnModel defining a table cell or within the components of the detail region.")
    String rowIndexVar;

    @Property(tlddoc = "Define the index-based data model of DataView, currently supported types include List and instances of DataViewLazyDataModel.")
    Object value;

    @Property(tlddoc = "Renders cell background shading on the odd numbered rows.")
    boolean rowStripe;

    @Property(defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION,
              tlddoc = "Renders faint bottom border to each row.")
    boolean rowStroke;

    @Property(tlddoc = "ActiveRowIndex property allows control of the row object index currently displayed in the detail region. " +
            "The detail region components instances are not updated until DataView.initDetailContext() is called. " +
            "initDetailContext is regularly called within the component phase iterations.")
    Integer activeRowIndex;

    @Property(defaultValue = "org.icefaces.mobi.component.dataview.ActivationMode.server",
              defaultValueType = DefaultValueType.EXPRESSION,
              tlddoc = "This enumeration defines the operation of the detail region. When set to 'server' (the default) " +
                      "the detail region may contain arbitrary JSF components and is rendered by an ajax request. " +
                      "When set to 'client' an activation 'renders' the detail region by updating, entirely on the client, " +
                      "an existing rendering with the dynamic attributes unique to an iterative rendering. The components " +
                      "and attributes supported for client encoding are limited, though growing with new releases and user desire." +
                      "Specifics can be found on our wiki at: http://www.icesoft.org/wiki/display/icemobile/DataView")
    ActivationMode activationMode;

    @Property(defaultValue = "false",
        defaultValueType = DefaultValueType.EXPRESSION,
        tlddoc="Enabling causes the table region to display as many columns as possible in the given device width, " +
                "hiding the remainder, following priority given with the 'reactivePriority' attribute of DataViewColumnModel.")
    boolean reactiveColumnVisibility;
    
    @Property
    String rowStyleClass;
    
    @Property
    String rowStyle;
    
    @Property(defaultValue="true", tlddoc="Setting this to true (default) will cause the DataView component to automatically scroll the "
            + "selected row to the top of the data list.")
    boolean scrollOnRowSelection;
    
    @Property(defaultValue="true", tlddoc="Setting this to true (default) will cause the column headers to remain fixed while scrolling the data region.")
    boolean fixedHeaders;

    @Property(defaultValue="50", tlddoc="time in ms for the duplicated and calculated headers to disappear. ")
    Integer dupHeaderDelay;

}
