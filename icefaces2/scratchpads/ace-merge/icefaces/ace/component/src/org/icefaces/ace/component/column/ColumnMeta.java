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

package org.icefaces.ace.component.column;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

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
	
	@Property(tlddoc="Supplementary CSS classes to add to those already applied on this component.")
	private String styleClass;
	
	@Property(tlddoc="Sorting via a EL reference to a comparable type field on the row object.")
	private Object sortBy;
	
	@Property(tlddoc="A second provided method of sorting. Sorting via the equivalent of a compare method operating on the type of this row.")
	private MethodExpression sortFunction;
	
	@Property(tlddoc="Filtering via an EL reference to a field of the row object to filter by.")
	private Object filterBy;
	
	@Property(tlddoc="JS event on which to trigger filter event, ex. \'keyup\', \'blur\', etc. Default is keyup.", defaultValue="keyup")
	private String filterEvent;
	
	@Property(tlddoc="Additional CSS rules to be applied to the filter input.")
	private String filterStyle;
	
	@Property(tlddoc="Supplementary CSS classes to add to those already applied on this component.")
	private String filterStyleClass;
	
	@Property(tlddoc="A collection of SelectItem objects for use as filter choices.")
	private Object filterOptions;
	
	@Property(tlddoc="Method of filter comparison used, default is \"startsWith\". Types available include: \"contains\", \"exact\", \"starts-with\" and \"ends-with\".", defaultValue="startsWith")
	private String filterMatchMode;
	
	@Property(tlddoc="Defines the number of rows the rendered cell spans. Only useful within a column group.", defaultValue="1")
	private int rowspan;
	
	@Property(tlddoc="Defines the number of columns the rendered cell spans.", defaultValue="1")
	private int colspan;
	
	@Property(tlddoc="A property to set a plain text header with less effort than adding a facet.")
	private String headerText;
	
	@Property(tlddoc="A property to set a plain text footer with less effort than adding a facet.")
	private String footerText;
	
	@Property(tlddoc="Enables column-level selection. Types of selection available include \"single\" & \"multiple\".")
	private String selectionMode;

    @Property(tlddoc="When a column is stacked, is is rendered underneath the previous column.")
    private boolean stacked;
}
