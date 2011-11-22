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

package org.icefaces.ace.component.dataexporter;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.annotation.Expression;

import javax.el.ValueExpression;
import javax.el.MethodExpression;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName       = "dataExporter",
        componentClass  = "org.icefaces.ace.component.dataexporter.DataExporter",
		rendererClass   = "org.icefaces.ace.component.dataexporter.DataExporterRenderer",
        generatedClass  = "org.icefaces.ace.component.dataexporter.DataExporterBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.DataExporter",
        rendererType    = "org.icefaces.ace.component.DataExporterRenderer",
		componentFamily  = "org.icefaces.ace.component",
		tlddoc = "Utility to export data from a datatable as an Excel, PDF, XML or CSV document. This component renders an HTML button. More components and HTML elements can be nested inside this tag to give a different look to the button.")
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="dataexporter/dataexporter.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="click", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
})
public class DataExporterMeta extends UIComponentBaseMeta {

	@Property(required=Required.yes, tlddoc="Server side id of the datatable whose date would be exported.")
	private String target;
	
	@Property(required=Required.yes, tlddoc="Export type: \"xls\",\"pdf\",\"csv\", \"xml\".")
	private String type;
	
	@Property(required=Required.yes, tlddoc="Filename of the generated export file, defaults to datatable server side id.")
	private String fileName;

	@Property(required=Required.no, tlddoc="The text that will appear on the button that will trigger the data export. Default value is 'Export' (only if the component contains no children).")
	private String label;
	
	@Property(required=Required.no, tlddoc="Exports only current page instead of whole dataset.", defaultValue="false")
	private boolean pageOnly;
	
	@Property(required=Required.no, tlddoc="Comma separated list(if more than one) of column indexes to be excluded from export.")
	private String excludeColumns;
	
	@Property(required=Required.no, tlddoc="PreProcessor for the exported document.", expression = Expression.METHOD_EXPRESSION)
	private MethodExpression preProcessor;
	
	@Property(required=Required.no, tlddoc="PostProcessor for the exported document.", expression = Expression.METHOD_EXPRESSION)
	private MethodExpression postProcessor;
	
	@Property(required=Required.no, tlddoc="Character encoding to use.", defaultValue="UTF-8")
	private String encoding;
	
	@Property(required=Required.no, tlddoc="Boolean value to specify whether column headers should be included at the top of the file.", defaultValue="true")
	private boolean includeHeaders;
	
	@Property(required=Required.no, tlddoc="Boolean value to specify whether column footers should be included at the bottom of the file.", defaultValue="true")
	private boolean includeFooters;
	
	@Property(required=Required.no, tlddoc="Exports only the currently selected rows instead of whole dataset.", defaultValue="false")
	private boolean selectedRowsOnly;
	
	@Property(required=Required.no, tlddoc="When singleSubmit is true, no other components will be executed when triggering the data exporter action. Equivalent to <f:ajax execute='@this' render='@all'>.", defaultValue="false")
	private boolean singleSubmit;
	
    @Property(required=Required.no, tlddoc="Style class of the button element.")
    private String styleClass;  

    @Property(required=Required.no, tlddoc="Style of the button element.")
    private String style;
}