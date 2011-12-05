/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.dataexporter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;

import org.icefaces.application.ResourceRegistry;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class CSVExporter extends Exporter {
	
    @Override
	public String export(FacesContext facesContext, DataTable table, String filename, boolean pageOnly, int[] excludeColumns, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor, boolean includeHeaders, boolean includeFooters, boolean selectedRowsOnly) throws IOException {
		StringBuilder builder = new StringBuilder();
		List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	
    	if (includeHeaders) {
			ColumnGroup columnGroup = getColumnGroupHeader(table);
			if (columnGroup != null) {
				List<Row> rows = getRows(columnGroup);
				for (Row row : rows) {
					List<UIColumn> rowColumns = getRowColumnsToExport(row, table, excludeColumns);
					addFacetColumns(builder, rowColumns, ColumnType.HEADER);
				}
			} else {
				addFacetColumns(builder, columns, ColumnType.HEADER);
			}
		}
    	
		int rowCount = table.getRowCount();
    	int first = pageOnly ? table.getFirst() : 0;
    	int size = pageOnly ? (first + table.getRows()) : rowCount;
		size = size > rowCount ? rowCount : size;

		Object originalData = null;
		if (selectedRowsOnly) {
			originalData = table.getValue();
			table.setValue(table.getStateMap().getSelected());
			first = 0;
			size = table.getRowCount();
		}
    	
    	for (int i = first; i < size; i++) {
    		table.setRowIndex(i);
    		addColumnValues(builder, columns);
			builder.append("\n");
		}
		
		if (selectedRowsOnly) {
			table.setValue(originalData);
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(builder, columns, ColumnType.FOOTER);
        }
    	
    	table.setRowIndex(-1);
        
		byte[] bytes = builder.toString().getBytes();
		//ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		//ExporterResource resource = new ExporterResource(bais);
		ExporterResource resource = new ExporterResource(bytes);
		resource.setContentType("text/csv");
		Map<String, String> headers = resource.getResponseHeaders();
		headers.put("Expires", "0");
        headers.put("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        headers.put("Pragma", "public");
        headers.put("Content-disposition", "attachment; filename=\"" + filename + ".csv\"");
		String path = ResourceRegistry.addSessionResource(resource);
		
		return path;
	}
	
	private void addColumnValues(StringBuilder builder, List<UIColumn> columns) throws IOException {
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
            addColumnValue(builder, iterator.next().getChildren());
            if (iterator.hasNext()) { builder.append(","); }
		}
	}

	private void addFacetColumns(StringBuilder builder, List<UIColumn> columns, ColumnType columnType) throws IOException {
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
			UIColumn uiColumn = iterator.next();
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			if (facet != null) {
				addColumnValue(builder, facet);
			} else {
				String value = "";
				if (uiColumn instanceof Column) {
					Column column = (Column) uiColumn;
					if (columnType == ColumnType.HEADER) {
						String headerText = column.getHeaderText();
						value = headerText != null ? headerText : "";
					} else if (columnType == ColumnType.FOOTER) {
						String footerText = column.getFooterText();
						value = footerText != null ? footerText : "";
					}
				}
				builder.append("\"" + value + "\"");
			}
            if (iterator.hasNext()) { builder.append(","); }
		}
		builder.append("\n");
    }
	
	private void addColumnValue(StringBuilder builder, UIComponent component) throws IOException {
		String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
		builder.append("\"" + value + "\"");
	}
	
	private void addColumnValue(StringBuilder builder, List<UIComponent> components) throws IOException {
		StringBuilder builder1 = new StringBuilder();
		for (UIComponent component : components)
			if (component.isRendered()) {
				String value = exportValue(FacesContext.getCurrentInstance(), component);
				builder1.append(value);
            }
		builder.append("\"" + builder1.toString() + "\"");
	}
}
