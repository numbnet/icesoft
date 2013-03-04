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
package org.icefaces.mobi.component.datatable;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
        tagName = "dataTable",
        componentClass = "org.icefaces.mobi.component.datatable.DataTable",
        rendererClass = "org.icefaces.mobi.component.datatable.DataTableRenderer",
        generatedClass = "org.icefaces.mobi.component.datatable.DataTableBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.DataTable",
        rendererType = "org.icefaces.DataTableRenderer",
        componentFamily = "org.icefaces.DataTable",
        tlddoc = "mobile data table "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class DataTableMeta extends UIComponentBaseMeta{
    
    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "Disable all features of the data table.", defaultValue = "false",
            defaultValueType= DefaultValueType.EXPRESSION)
    private boolean disabled;
    
    @Property(tlddoc = "Defines a tabindex to be shared by all keyboard navigable elements of the table. " +
            "This includes sort controls, filter fields and individual rows themselves.",
            defaultValue = "0",
            defaultValueType = DefaultValueType.EXPRESSION)
    private Integer tabIndex;
    
    @Property(tlddoc = "Return the internal DataModel object representing the data objects " +
    		"that we will iterate over in this component's rendering.",
    		defaultValue = "0", defaultValueType= DefaultValueType.EXPRESSION)
    private int first;
    
    @Property(tlddoc = "Return the number of rows in the underlying data model.",
            defaultValue = "0", defaultValueType= DefaultValueType.EXPRESSION)
    private int rowCount;
    
    @Property(tlddoc = "Return the zero-relative index of the currently selected row.",
            defaultValueType= DefaultValueType.EXPRESSION)
    private Object value;

    @Property(tlddoc = "Return the request-scope attribute under which the data object for the " +
    		"current row will be exposed when iterating.",
            defaultValueType= DefaultValueType.STRING_LITERAL)
    private String var;
    
    @Property(tlddoc = "Enables lazy loading. Lazy loading expects the 'value' property to reference " +
            "an instance of LazyDataModel, an interface to support incremental fetching of " +
            "table entities.")
    private boolean lazy;

    @Property(defaultValue="0",
            defaultValueType= DefaultValueType.EXPRESSION,
            implementation=Implementation.GENERATE,
            tlddoc="The number of rows to be displayed, or zero to display the entire " +
                    "set of available rows.")
    private int rows;

    @Property(tlddoc = "Define a string to render when there are no records to display.")
    private String emptyMessage;

    @Property(tlddoc = "Defines the index of the current page, beginning at 1.")
    private int page;

    @Property(tlddoc = "Enables pagination on the table. Note that the paginator works by adjusting the " +
            "'first' and 'page' properties and that disabling the paginator will not return these " +
            "properties to their defaults; instead leaving the table at the position that was paginated to. " +
            "To return the table to the first page, 'first' must be set to 0, or 'page' must be set to 1.")
    private boolean paginator;

    @Property(tlddoc = "The orientation of the detail panel, top or bottom. ", defaultValue = "top")
    private String detailOrientation;
    
    @Property(tlddoc = "The orientation of the detail panel for large view clients, " +
    		"such as tablets or desktops, top, bottom, left, or right. This attribute " +
    		"will override the detailOrientation attribute for those clients. " )
    private String largeViewDetailOrientation;
    
    @Property(tlddoc = "The clientSide property will cause the DataTable to render with solely " +
    		"client-side functionality. ", defaultValue = "true" )
    private boolean clientSide;




}
