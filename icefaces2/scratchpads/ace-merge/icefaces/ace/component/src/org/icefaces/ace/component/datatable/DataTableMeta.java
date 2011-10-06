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

package org.icefaces.ace.component.datatable;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIDataMeta;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.model.table.TreeDataModel;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.model.DataModel;
import java.util.ArrayList;
import java.util.List;

@Component(
        tagName = "dataTable",
        componentClass = "org.icefaces.ace.component.datatable.DataTable",
        generatedClass = "org.icefaces.ace.component.datatable.DataTableBase",
        rendererClass = "org.icefaces.ace.component.datatable.DataTableRenderer",
        extendsClass = "org.icefaces.ace.api.UIData",
        componentType = "org.icefaces.ace.component.DataTable",
        rendererType = "org.icefaces.ace.component.DataTableRenderer",
        componentFamily = "org.icefaces.ace.DataTable",
        tlddoc = ""
)
public class DataTableMeta extends UIDataMeta {
    @Property(tlddoc = "The JavaScript component instance variable name. This name must be unique among tables that might ever be viewed simultaneously. ")
    private String widgetVar;

    @Property(tlddoc = "Comma separated integer values that define the options for \"number of items per page\" presented to the user.")
    private String rowsPerPageTemplate;
    @Property(tlddoc = "Coded string defining the layout of the text displaying the current page. Default is: \"{currentPage} of {totalPages}\".")
    private String currentPageReportTemplate;

    @Property(tlddoc = "Coded string defining the controls available as part of the paginator. Default is: \"{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}\".")
    private String paginatorTemplate;
    @Property(tlddoc = "Defines the location of the paginator if enabled. Available options are top, bottom, or the default, both.", defaultValue = "both")
    private String paginatorPosition;

    @Property(tlddoc = "Enable paginator use on the datatable.")
    private boolean paginator;
    @Property(tlddoc = "Always display the paginator, even when fewer then 1 page worth of items are displayed.")
    private boolean paginatorAlwaysVisible;

    @Property(tlddoc = "Number of links to display per page.", defaultValue = "10", defaultValueType = DefaultValueType.EXPRESSION)
    private Integer pageLinks;
    @Property(tlddoc = "Index of the current page. Indexes start at 1. Required to keep pagination state.")
    private int page;
    @Property(tlddoc = "Fixed height for the table in pixels.")
    private Integer height;
    @Property(tlddoc = "When enabled, overflow the fixed table height and enable scrolling.")
    private boolean scrollable;

    @Property(tlddoc = "Expect the 'value' attribute will be a reference to an instance of LazyLoader, an interface to support incremental fetching of table entities.")
    private boolean lazy;

    @Property(tlddoc = "Enabling this setting requires a double-click to fire row/column/cell selection events.")
    private boolean dblClickSelect;
    @Property(tlddoc = "Enables the table to insert additional rows as scrolling reaches the bottom of the table.")
    private boolean liveScroll;
    @Property(tlddoc = "Enable resizing of the table columns via handles on the column headers.")
    private boolean resizableColumns;
    @Property(tlddoc = "")
    private boolean reorderableColumns;
    @Property(tlddoc = "Disabled sorting for multiple columns at once.", defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean singleSort;

    @Property(tlddoc = "Code word indicating method of table element selection. Available values include: \"multiple\", \"single\", \"cellblock\", \"cellrange\" and \"singlecell\".")
    private String selectionMode;
    @Property(tlddoc = "An object to be populated with the backing object corresponding to selected table cells. In the case of multiple element selection, it's expected that this object will be an implementer of the List interface.")
    private Object cellSelection;

    @Property(tlddoc = "EL name used to refer to the the index of this row in the column definitions.")
    private String rowIndexVar;
    @Property(tlddoc = "EL name used to refer to the the row state object of the row in the column definitions.", defaultValue = "rowState")
    private String rowStateVar;
    @Property(tlddoc = "Message to render when there are no records to display.")
    private String emptyMessage;

    @Property(tlddoc = "Additional CSS rules to be applied to this component.")
    private String style;
    @Property(tlddoc = "Supplementary CSS classes to add to those already applied on this component.")
    private String styleClass;
    @Property(tlddoc = "Adds the following style to each row of the dataTable. EL can be used in this attribute to produce conditional row styling.")
    private String rowStyleClass;

    @Property(expression = Expression.METHOD_EXPRESSION,
              methodExpressionArgument = "org.icefaces.ace.event.SelectEvent",
              tlddoc = "MethodExpression reference called whenever a table element is selected. The method receives a single argument, SelectEvent.")
    private MethodExpression rowSelectListener;
    @Property(expression = Expression.METHOD_EXPRESSION,
              methodExpressionArgument = "org.icefaces.ace.event.UnselectEvent",
              tlddoc = "MethodExpression reference called whenever a table element is deselected. The method receives a single argument, UnselectEvent.")
    private MethodExpression rowUnselectListener;
    @Property(expression = Expression.METHOD_EXPRESSION,
              methodExpressionArgument = "org.icefaces.ace.event.RowEditEvent",
              tlddoc = "MethodExpression reference called whenever a table row is edited. The method receives a single argument, RowEditEvent.")
    private MethodExpression rowEditListener;

    @Property(tlddoc = "ID of DOM node to re-render following events on this tree. None by default.")
    private String update;
    @Property(tlddoc = "ID of DOM node to re-render following row selection events on this tree. None by default.")
    private String onRowSelectUpdate;
    @Property(tlddoc = "ID of DOM node to re-render following row update events on this tree. None by default.")
    private String onRowUnselectUpdate;
    @Property(tlddoc = "ID of DOM node to re-render following row edit events on this tree. None by default.")
    private String onRowEditUpdate;

    @Property(tlddoc = "Javascript handler to run on the firing of a table element selection event.")
    private String onSelectStart;
    @Property(tlddoc = "Javascript handler to run on the completion of a table element selection ajax event.")
    private String onSelectComplete;
    @Property(tlddoc = "Javascript handler to run on the firing of a row selection event.")
    private String onRowSelectStart;
    @Property(tlddoc = "Javascript handler to run on the completion of a row selection ajax event.")
    private String onRowSelectComplete;
    @Property(tlddoc = "Javascript handler to run on the firing of a row expansion event.")
    private String onExpandStart;

    @Property(tlddoc = "A map of your row data objects to UI states. Row-level features (selection, expansion, etc.) are manipulable through this repository.")
    private RowStateMap stateMap;

    @Property(tlddoc = "A list of integers representing a rendering order for the Column children of the datatable.")
    private List<Integer> columnOrdering;

    @Field
    String configPanelId;

    // Detect when to recheck model for tree handling
    @Field
    Integer valueHashCode;

    // Serializable tree data model.
    // Only references to serializable data models should be kept here; experienced SWF errors otherwise.
    @Field
    TreeDataModel treeModel;

    // Only used when external map is absent
    @Field
    RowStateMap internalStateMap;

    private org.icefaces.ace.api.UIData xyz;
}
