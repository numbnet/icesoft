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

package org.icefaces.ace.component.dataexporter;

import java.io.IOException;
import java.util.List;
import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.context.FacesContext;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;

/**
 * This custom exporter is invoked by the OuterTableCSVExporter.
 */
public class InnerTableCSVExporter extends CSVExporter {

	private String parentRow;
	
	public InnerTableCSVExporter(String parentRow) { 
		if (parentRow == null) {
			this.parentRow = "";
		} else {
			this.parentRow = parentRow;
		}
	}
	
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
					builder.append(parentRow);
					addFacetColumns(builder, rowColumns, ColumnType.HEADER);
				}
			} else {
				builder.append(parentRow);
				addFacetColumns(builder, columns, ColumnType.HEADER);
			}
		}
    	
		int rowCount = table.getRowCount();
    	int first = pageOnly ? table.getFirst() : 0;
    	int size = pageOnly ? (first + table.getRows()) : rowCount;
		size = size > rowCount ? rowCount : size;

		RowStateMap rowStateMap = table.getStateMap();

		String rowIndexVar = table.getRowIndexVar();
		rowIndexVar = rowIndexVar == null ? "" : rowIndexVar;
    	for (int i = first; i < size; i++) {
    		table.setRowIndex(i);
			boolean exportRow = true;
			if (selectedRowsOnly) {
				RowState rowState = rowStateMap.get(table.getRowData());
				if (!rowState.isSelected()) exportRow = false;
			}
			if (exportRow) {
				if (!"".equals(rowIndexVar)) {
					facesContext.getExternalContext().getRequestMap().put(rowIndexVar, i);
				}
				builder.append(parentRow);
				addColumnValues(builder, columns);
				builder.append("\n");
			}
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(builder, columns, ColumnType.FOOTER);
        }
    	
    	table.setRowIndex(-1);
        
		return builder.toString();
	}
}