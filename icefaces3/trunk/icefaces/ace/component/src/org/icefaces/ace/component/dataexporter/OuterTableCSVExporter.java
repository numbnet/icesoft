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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.panelexpansion.PanelExpansion;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;

/**
 * This custom exporter is only available by using the 'customExporter' attribute.
 * You can see a demo in the ICEfaces showcase application on how to use it.
 */
public class OuterTableCSVExporter extends CSVExporter {

	private List<String> innerTables;
	
	public OuterTableCSVExporter(List<String> innerTables) {
		this.innerTables = innerTables;
	}
	
	@Override
	public String export(FacesContext facesContext, DataExporter component, DataTable table) throws IOException {
		setUp(component, table);
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
				StringBuilder rowBuilder = new StringBuilder();
				addColumnValues(rowBuilder, columns);
				builder.append(rowBuilder.toString());
				builder.append("\n");
				PanelExpansion pe = table.getPanelExpansion();
				if (pe != null) {
					for (UIComponent kid : pe.getChildren()) {
						String clientId = kid.getClientId();
						if (this.innerTables.contains(clientId)) {
							InnerTableCSVExporter innerExporter = new InnerTableCSVExporter(rowBuilder.toString() + ",");
							String innerTable = innerExporter.export(facesContext, component, (DataTable) kid);
							builder.append(innerTable);
							break;
						}
					}
				}
			}
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(builder, columns, ColumnType.FOOTER);
        }
    	
    	table.setRowIndex(-1);
        
		byte[] bytes = builder.toString().getBytes();
		
		return registerResource(bytes, filename + ".csv", "text/csv");
	}
}