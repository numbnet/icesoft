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
package org.icefaces.ace.component.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

public class CSVExporter extends Exporter {
	
    @Override
	public void export(FacesContext facesContext, DataTable table, String filename, boolean pageOnly, int[] excludeColumns, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor, boolean includeHeaders, boolean includeFooters, boolean selectedRowsOnly) throws IOException {
        ExternalContext ec = facesContext.getExternalContext();
		OutputStream os = ec.getResponseOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os , encodingType);
		PrintWriter writer = new PrintWriter(osw);	
		List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	
    	if (includeHeaders) {
			ColumnGroup columnGroup = getColumnGroupHeader(table);
			if (columnGroup != null) {
				List<Row> rows = getRows(columnGroup);
				for (Row row : rows) {
					List<UIColumn> rowColumns = getRowColumnsToExport(row, table, excludeColumns);
					addFacetColumns(writer, rowColumns, ColumnType.HEADER);
				}
			} else {
				addFacetColumns(writer, columns, ColumnType.HEADER);
			}
		}
    	
    	int first = pageOnly ? table.getFirst() : 0;
    	int size = pageOnly ? (first + table.getRows()) : table.getRowCount();
		
		Object originalData = null;
		if (selectedRowsOnly) {
			originalData = table.getValue();
			table.setValue(table.getStateMap().getSelected());
			first = 0;
			size = table.getRowCount();
		}
    	
    	for (int i = first; i < size; i++) {
    		table.setRowIndex(i);
    		addColumnValues(writer, columns);
			writer.write("\n");
		}
		
		if (selectedRowsOnly) {
			table.setValue(originalData);
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(writer, columns, ColumnType.FOOTER);
        }
    	
    	table.setRowIndex(-1);
    	
    	ec.setResponseContentType("text/csv");
    	ec.setResponseHeader("Expires", "0");
        ec.setResponseHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        ec.setResponseHeader("Pragma", "public");
        ec.setResponseHeader("Content-disposition", "attachment;filename="+ filename + ".csv");
        
        writer.flush();
        writer.close();
        
        os.flush();
	}
	
	private void addColumnValues(PrintWriter writer, List<UIColumn> columns) throws IOException {
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
            addColumnValue(writer, iterator.next().getChildren());
            if (iterator.hasNext()) writer.write(",");
		}
	}

	private void addFacetColumns(PrintWriter writer, List<UIColumn> columns, ColumnType columnType) throws IOException {
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
			UIColumn uiColumn = iterator.next();
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			if (facet != null) {
				addColumnValue(writer, facet);
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
				writer.write("\"" + value + "\"");
			}
            if (iterator.hasNext()) writer.write(",");
		}
		writer.write("\n");
    }
	
	private void addColumnValue(PrintWriter writer, UIComponent component) throws IOException {
		String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
        writer.write("\"" + value + "\"");
	}
	
	private void addColumnValue(PrintWriter writer, List<UIComponent> components) throws IOException {
		StringBuilder builder = new StringBuilder();
		for (UIComponent component : components)
			if (component.isRendered()) {
				String value = exportValue(FacesContext.getCurrentInstance(), component);
				builder.append(value);
            }
		writer.write("\"" + builder.toString() + "\"");
	}
}
