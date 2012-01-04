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
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.model.DataModel;
import java.util.ArrayList;
import java.util.List;
import org.icefaces.ace.api.IceClientBehaviorHolder;


@Component(
        tagName = "dataTable",
        componentClass = "org.icefaces.ace.component.datatable.DataTable",
        generatedClass = "org.icefaces.ace.component.datatable.DataTableBase",
        rendererClass = "org.icefaces.ace.component.datatable.DataTableRenderer",
        extendsClass = "javax.faces.component.UIData",
        componentType = "org.icefaces.ace.component.DataTable",
        rendererType = "org.icefaces.ace.component.DataTableRenderer",
        componentFamily = "org.icefaces.ace.DataTable",
        tlddoc = "Renders an HTML table element. Rows are created from the List" +
                " or DataModel object bound by the value property. The " +
                "header/footer is rendered by the header/footer facet of Column" +
                " component children or a child ColumnGroup component definition."
)
@ResourceDependencies({
		@ResourceDependency(library="icefaces.ace", name="util/combined.css"),
        @ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
        @ResourceDependency(library="icefaces.ace", name="util/ace-datatable.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="page", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        @ClientEvent(name="select", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        @ClientEvent(name="deselect", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        @ClientEvent(name="sort", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        @ClientEvent(name="filter", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        @ClientEvent(name="reorder", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        // Edit has custom render and execute, @none is just a null placeholder for additional update/execute fields
        @ClientEvent(name="editStart", javadoc="...", tlddoc="...", defaultRender="@none", defaultExecute="@none"),
        @ClientEvent(name="editSubmit", javadoc="...", tlddoc="...", defaultRender="@none", defaultExecute="@none"),
        @ClientEvent(name="editCancel", javadoc="...", tlddoc="...", defaultRender="@none", defaultExecute="@none"),
        @ClientEvent(name="expand", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this"),
        @ClientEvent(name="contract", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@this")} ,
        defaultEvent = "select"
)
public class DataTableMeta extends UIDataMeta {
    /* ##################################################################### */
    /* ############################ Misc. Prop. ############################ */
    /* ##################################################################### */
    @Property(tlddoc = "The JavaScript global component instance name. " +
            "Must be unique among components on a page. ")
    private String widgetVar;

    @Property(tlddoc = "Disable all features of the data table.", defaultValue = "false",
            defaultValueType= DefaultValueType.EXPRESSION)
    private boolean disabled;

    @Property(tlddoc = "The request-scope attribute (if any) under which the data " +
        "object index for the current row will be exposed when iterating.")
    private String rowIndexVar;

    @Property(tlddoc = "The request-scope attribute (if any) under which the table" +
            " state object for the current row will be exposed when iterating.", defaultValue = "rowState")
    private String rowStateVar;

    @Property(tlddoc = "Enables lazy loading. Expects the 'value' property reference " +
            "an instance of LazyDataModel, an interface to support incremental fetching of " +
            "table entities.")
    private boolean lazy;

    @Property(defaultValue="0",
            defaultValueType= DefaultValueType.EXPRESSION,
            implementation=Implementation.GENERATE,
            tlddoc="The number of rows (starting with the one identified by the " +
                    "first property) to be displayed, or zero to display the entire " +
                    "set of available rows.")
    private int rows;

    @Property(tlddoc = "Message to render when there are no records to display.")
    private String emptyMessage;
    
    @Property(tlddoc = "Disable sorting for multiple columns at once. Sorting " +
            "is enabled by the use of sortBy on ace:column components.",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean singleSort;

    @Property(tlddoc = "A map of your row data objects to UI states. Row-level " +
            "features (selection, expansion, etc.) are manipulable through this repository.")
    private RowStateMap stateMap;

    @Property(tlddoc = "Enable the decoding of child components during table feature " +
            "requests. The table attempts to decode children whenever it is executed, " +
            "meaning whenever a parent region is submitted, or the table submits itself " +
            "to paginate, make a selection, reorder columns, or any other feature. " +
            "Decoding children during feature requests can result in unwanted input " +
            "submission (during pagination for example), so by default this component " +
            "suppresses child decoding whenever submitting itself. To decode the " +
            "children of the table, use the row editing feature for row-scoped input " +
            "decoding, submit the form (or other table parent) for broad submission " +
            "or enable this option to submit during all table operations.",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean alwaysExecuteContents;

    // ID of the configPanel that has been associated with this table, used for
    // component lookups during decodes.
    @Field(defaultValue = "null", defaultValueIsStringLiteral = false)
    protected String tableConfigPanel;
    @Field(defaultValue = "null", defaultValueIsStringLiteral = false)
    protected List filteredData;
    @Field(defaultValue = "null", defaultValueIsStringLiteral = false)
    protected Integer valueHashCode;
    @Field(defaultValue = "true", defaultValueIsStringLiteral = false)
    protected Boolean sortOrderChanged;
    @Field(defaultValue = "true", defaultValueIsStringLiteral = false)
    protected Boolean filterValueChanged;





    /* ##################################################################### */
    /* ########################## Style Properties ######################### */
    /* ##################################################################### */
    @Property(tlddoc = "Additional CSS rules to be applied to this component.")
    private String style;

    @Property(tlddoc = "Supplementary CSS classes to add to those already applied" +
            " on this component.")
    private String styleClass;

    @Property(tlddoc = "Adds the following style to each row of the dataTable. " +
            "EL can be used in this attribute to produce conditional row styling.")
    private String rowStyleClass;





    /* ##################################################################### */
    /* ############################# Listeners ############################# */
    /* ##################################################################### */
    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.SelectEvent",
            tlddoc = "MethodExpression reference called whenever a table " +
                    "element is selected. The method receives a single " +
                    "argument, RowSelectEvent.")
    private MethodExpression rowSelectListener;

    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.UnselectEvent",
            tlddoc = "MethodExpression reference called whenever a table " +
                    "element is deselected. The method receives a single " +
                    "argument, RowUnselectEvent.")
    private MethodExpression rowUnselectListener;

    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.TableFilterEvent",
            tlddoc = "MethodExpression reference called whenever the table row " +
                    "is filtered. The method receives a single argument, TableFilterEvent.")
    private MethodExpression filterListener;





    /* ##################################################################### */
    /* ############################# Pagination ############################ */
    /* ##################################################################### */
    @Property(tlddoc = "Comma separated integer values that define the options " +
            "for \"number of items per page\" presented to the user.")
    private String rowsPerPageTemplate;

    @Property(tlddoc = "Coded string defining the layout of the text displaying" +
            " the current page. Default is: \"{currentPage} of {totalPages}\".")
    private String currentPageReportTemplate;

    @Property(tlddoc = "Coded string defining the controls available as part of" +
            " the paginator. Default is: \"{FirstPageLink} {PrevgetSEliousPageLink} " +
            "{PageLinks} {NextPageLink} {LastPageLink}\".")
    private String paginatorTemplate;

    @Property(tlddoc = "Defines the location of the paginator if enabled. Available " +
            "options are top, bottom, or the default, both.", defaultValue = "both")
    private String paginatorPosition;

    @Property(tlddoc = "Always display the paginator, even when fewer then 1 page " +
            "full of items are displayed.")
    private boolean paginatorAlwaysVisible;

    @Property(tlddoc = "Maximum number of individual page links to display in paginator.",
            defaultValue = "10", defaultValueType = DefaultValueType.EXPRESSION)
    private Integer pageCount;

    @Property(tlddoc = "Enables pagination on the table.")
    private boolean paginator;

    @Property(tlddoc = "Index of the current page, beginning at 1.")
    private int page;





    /* ##################################################################### */
    /* ####################### Javascript Callbacks ######################## */
    /* ##################################################################### */
    @Property(tlddoc = "Javascript handler to run on the firing of a table " +
            "row or cell selection event.")
    private String onSelectStart;

    @Property(tlddoc = "Javascript handler to run on the completion of a table " +
            "row or cell selection ajax event.")
    private String onSelectComplete;

    @Property(tlddoc = "Javascript handler to run on the firing of a row selection event.")
    private String onRowSelectStart;

    @Property(tlddoc = "Javascript handler to run on the completion of a row selection ajax event.")
    private String onRowSelectComplete;

    @Property(tlddoc = "Javascript handler to run on the firing of a row expansion event.")
    private String onExpandStart;





    /* ##################################################################### */
    /* ########################## Scrolling Prop. ########################## */
    /* ##################################################################### */
    @Property(tlddoc = "Fixed height for the table in pixels. Must be set to " +
            "use vertical scrolling.")
    private Integer height;

    @Property(tlddoc = "When enabled, table overflows the fixed height and adds " +
            "a scrollbar. Note: Used in combination with multi-row headers defined by a ColumnGroup" +
            "component, it is assumed that every body column of the table will have a associated " +
            "single column spanning header column on the bottom row of the multi-row header. This is " +
            "to allow for appropriate sizing of the column the associated header.")
    private boolean scrollable;

    @Property(tlddoc = "Enables the table to insert additional rows as " +
            "scrolling reaches the bottom of the table. The 'rows' property" +
            " configures the number of new rows to be loaded")
    private boolean liveScroll;





    /* ##################################################################### */
    /* ########################## Selection Prop. ########################## */
    /* ##################################################################### */
    @Property(tlddoc = "Code word indicating method of table element selection." +
            " Available values include: \"multiple\", \"single\"," +
            " \"cellblock\", \"cellrange\" and \"singlecell\".")
    private String selectionMode;

    @Property(tlddoc = "Require a double-click to fire row/column/cell selection events.")
    private boolean dblClickSelect;

    @Property(tlddoc = "An object to be populated with the backing object " +
            "corresponding to selected table cells. In the case of multiple " +
            "element selection, it's expected that this object will be an implementer" +
            " of the List interface.")
    private Object cellSelection;





    /* ##################################################################### */
    /* ########################## Filtering Prop. ########################## */
    /* ##################################################################### */
    @Property(tlddoc = "Force creation of the filtered data set from the bound " +
            "value every render. Alternately attempt to use hashCodes of the " +
            "value property to detect changes and prompt refiltering.",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean constantRefilter;

    @Property(tlddoc="Javascript event on which to trigger filter event, ex. " +
            "\'keyup\', \'blur\', \'change\' and \'enter\'.", defaultValue="change")
    private String filterEvent;

    @Property(tlddoc="The input to the global filter, coming from the client, or " +
            "from the app via a value binding.")
    private String filterValue;





    /* ##################################################################### */
    /* ########################## Column Features ########################## */
    /* ##################################################################### */
    @Property(tlddoc = "Enable resizing of the table columns via handles on " +
            "the column headers.")
    private boolean resizableColumns;

    @Property(tlddoc = "Enable reordering of the table columns via header " +
            "dragging.")
    private boolean reorderableColumns;

    @Property(tlddoc = "A list of integers representing a rendering order for " +
            "the Column children of the datatable.")
    private List<Integer> columnOrdering;
}
