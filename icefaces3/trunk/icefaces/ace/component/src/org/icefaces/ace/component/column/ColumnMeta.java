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

package org.icefaces.ace.component.column;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

import java.util.Comparator;

@Component(
        tagName         = "column",
        componentClass  = "org.icefaces.ace.component.column.Column",
        generatedClass  = "org.icefaces.ace.component.column.ColumnBase",
        extendsClass    = "javax.faces.component.UIColumn",
        componentType   = "org.icefaces.ace.component.Column",
        rendererType    = "",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "Component that represents a column in a table. Configurable regarding sorting, filterting and column selection.")
@ResourceDependencies({

})

public class ColumnMeta extends UIColumnMeta {

	@Property(tlddoc="Additional CSS rules to be applied to this component.")
	private String style;
	
	@Property(tlddoc="Supplementary CSS classes to add to those already " +
            "applied on this component.")
	private String styleClass;
	
	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="When sorting this column use this ValueExpression as the " +
            "value of this row. Setting this variable for a column enables sorting.")
	private Object sortBy;
	
	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="An alternate method of sorting. Sort this column using a " +
            "Comparator<Object> object that takes the sortBy values of this column as input.")
	private Comparator sortFunction;

	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="When filtering this column use this ValueExpression as the " +
            "value of this row. Setting this variable for a column enables filtering.")
	private Object filterBy;

    @Property(tlddoc="The string input filtering this column, coming from the client, or from " +
            "the application via a value binding.")
    private String filterValue;
    
	@Property(tlddoc="Additional CSS rules to be applied to the filter text input.")
	private String filterStyle;
	
	@Property(tlddoc="Supplementary CSS classes to add to those already " +
            "applied on this component.")
	private String filterStyleClass;
	
	@Property(tlddoc="A collection of SelectItem objects for use as filter choices.")
	private Object filterOptions;
	
	@Property(tlddoc="Method of filter comparison used, default is \"startsWith\". " +
            "Types available include: \"contains\", \"exact\", \"startsWith\" and \"endsWith\".", defaultValue="startsWith")
	private String filterMatchMode;
	
	@Property(tlddoc="Defines the number of rows the rendered cell spans. Only " +
            "significant to Column components within a column group.", defaultValue="1")
	private int rowspan;
	
	@Property(tlddoc="Defines the number of columns the rendered cell spans. Only " +
            "significant to Column components within a column group.", defaultValue="1")
	private int colspan;
	
	@Property(tlddoc="A plain text header with less effort than adding a facet.")
	private String headerText;
	
	@Property(tlddoc="A plain text footer with less effort than adding a facet.")
	private String footerText;

    @Property(tlddoc="When enabled, this column is rendered underneath the previous column.")
    private boolean stacked;

    @Property(tlddoc="When disabled, this column is excluded from the list of columns available" +
            " for configuration on a TableConfigPanel component.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean configurable;

    @Property(tlddoc="Defines the priority of a column during sorting. The column " +
            "priorities incoming from the client during a sort request overwrite any " +
            "set by the application. Processing the sorted columns is done by the " +
            "component whenever a client edits a sort control or the application calls table.applySorting().")
    private Integer sortPriority;

    @Property(tlddoc="Defines the direction of column values during sorting. " +
            "The column directions incoming from the client during a sort request " +
            "overwrite any set by the application. Processing the sorted columns is " +
            "done by the component whenever a client edits a sort control or the " +
            "application calls table.applySorting().",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean sortAscending;

    @Property(tlddoc="Allows per-column control of column ordering when the " +
            "feature (\"reorderableColumns\") is enabled at the table level.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean reorderable;
}
