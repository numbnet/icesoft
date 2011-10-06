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

package org.icefaces.ace.component.export;

import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.TagHandlerType;
import org.icefaces.ace.meta.annotation.Required;

import javax.el.ValueExpression;
import javax.el.MethodExpression;

@TagHandler(
    tagName = "dataExporter",
    tagHandlerType = TagHandlerType.TAG_HANDLER,
    tagHandlerClass = "org.icefaces.ace.component.export.DataExporterTagHandler",
    generatedClass = "org.icefaces.ace.component.export.DataExporterTagHandlerBase",
    extendsClass = "javax.faces.view.facelets.TagHandler",
    tlddoc = "Utility to export data from a datatable as an Excel, PDF, XML or CSV document."
)
public class DataExporterTagHandlerMeta {

	@Property(required=Required.yes, tlddoc="Server side id of the datatable whose date would be exported.")
	private ValueExpression target;
	
	@Property(required=Required.yes, tlddoc="Export type: \"xls\",\"pdf\",\"csv\", \"xml\".")
	private ValueExpression type;
	
	@Property(required=Required.yes, tlddoc="Filename of the generated export file, defaults to datatable server side id.")
	private ValueExpression fileName;
	
	@Property(required=Required.no, tlddoc="Exports only current page instead of whole dataset.")
	private ValueExpression pageOnly;
	
	@Property(required=Required.no, tlddoc="Comma separated list(if more than one) of column indexes to be excluded from export.")
	private ValueExpression excludeColumns;
	
	@Property(required=Required.no, tlddoc="PreProcessor for the exported document.")
	private MethodExpression preProcessor;
	
	@Property(required=Required.no, tlddoc="PostProcessor for the exported document.")
	private MethodExpression postProcessor;
	
	@Property(required=Required.no, tlddoc="Character encoding to use.")
	private ValueExpression encoding;
	
	@Property(required=Required.no, tlddoc="Boolean value to specify whether column headers should be included at the top of the file.", defaultValue="true")
	private ValueExpression includeHeaders;
	
	@Property(required=Required.no, tlddoc="Boolean value to specify whether column footers should be included at the bottom of the file.", defaultValue="true")
	private ValueExpression includeFooters;
	
	@Property(required=Required.no, tlddoc="Exports only the currently selected rows instead of whole dataset.", defaultValue="false")
	private ValueExpression selectedRowsOnly;
}