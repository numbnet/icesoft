/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;

import org.icefaces.application.ResourceRegistry;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class XMLExporter extends Exporter {

    @Override
	public String export(FacesContext facesContext, DataTable table, String filename, boolean pageOnly, int[] excludeColumns, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor, boolean includeHeaders, boolean includeFooters, boolean selectedRowsOnly) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	
		List<String> headers;
		ColumnGroup columnGroup = getColumnGroupHeader(table);
		if (columnGroup != null) {
			headers = getHeadersFromColumnGroup(columnGroup, columns, table, excludeColumns);
		} else {
			headers = getFacetTexts(columns, ColumnType.HEADER);
		}
    	List<String> footers = getFacetTexts(columns, ColumnType.FOOTER);
    	String var = table.getVar().toLowerCase();
    	
    	builder.append("<?xml version=\"1.0\"?>\n");
    	builder.append("<" + table.getId() + ">\n");
    	
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
    		
    		builder.append("\t<" + var + ">\n");
    		addColumnValues(builder, columns, headers);
    		builder.append("\t</" + var + ">\n");
		}
		
		if (selectedRowsOnly) {
			table.setValue(originalData);
		}

        if (hasColumnFooter(columns) && includeFooters) {
            builder.append("\t<footers>\n");
            addFooterValues(builder, footers, headers);
            builder.append("\t</footers>\n");
        }
    	
    	builder.append("</" + table.getId() + ">");
    	
    	table.setRowIndex(-1);
    	
		byte[] bytes = builder.toString().getBytes();
		//ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		//ExporterResource resource = new ExporterResource(bais);
		ExporterResource resource = new ExporterResource(bytes);
		resource.setContentType("text/xml");
		Map<String, String> httpHeaders = resource.getResponseHeaders();
		httpHeaders.put("Expires", "0");
        httpHeaders.put("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        httpHeaders.put("Pragma", "public");
        httpHeaders.put("Content-disposition", "attachment;filename=" + filename + ".xml");
		String path = ResourceRegistry.addSessionResource(resource);
		
		return path;
	}
	
	private void addColumnValues(StringBuilder builder, List<UIColumn> columns, List<String> headers) throws IOException {
		for (int i = 0; i < columns.size(); i++) {
            addColumnValue(builder, columns.get(i).getChildren(), headers.get(i));
		}
	}
	
	private void addFooterValues(StringBuilder builder, List<String> footers, List<String> headers) throws IOException {
		for (int i = 0; i < footers.size(); i++) {
			String footer = footers.get(i);
			
			if (footer.length() > 0)
				addColumnValue(builder, footer, headers.get(i));
		}
	}	
	
	private List<String> getFacetTexts(List<UIColumn> columns, ColumnType columnType) {
		List<String> facets = new ArrayList<String>();
		 
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
			UIColumn uiColumn = iterator.next();
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			if (facet != null) {
				facets.add(exportValue(FacesContext.getCurrentInstance(), facet));
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
				facets.add(value);
			}
		}
        return facets;
	}
	
	private String extractValueToDisplay(UIColumn column, ColumnType columnType) {
		UIComponent facet = column.getFacet(columnType.facet());
		
		if (facet != null && facet.isRendered()) {
			String value = exportValue(FacesContext.getCurrentInstance(), facet);
			
			return value;
		} else {
			String value = "";
			if (column instanceof Column) {
				Column _column = (Column) column;
				if (columnType == ColumnType.HEADER) {
					String headerText = _column.getHeaderText();
					value = headerText != null ? headerText : "";
				} else if (columnType == ColumnType.FOOTER) {
					String footerText = _column.getFooterText();
					value = footerText != null ? footerText : "";
				}
			}
			return value;
		}
	}
	
	private List<String> getHeadersFromColumnGroup(ColumnGroup columnGroup, List<UIColumn> columns, UIData data, int[] excludeColumns) {
	
		ArrayList<Row> rows = (ArrayList<Row>) getRows(columnGroup);
		int size = rows.size();
		if (size > 0) {
			List<UIColumn> rowColumns = getRowColumnsToExport(rows.get(size-1), data, excludeColumns); // only use last row in column group
			List<String> values = new ArrayList<String>();
			for (UIColumn column : rowColumns) {
				String value = extractValueToDisplay(column, ColumnType.HEADER);
				values.add(value);
			}
			return values;
		} else {
			return getFacetTexts(columns, ColumnType.HEADER);
		}
	}

	private void addColumnValue(StringBuilder builder, List<UIComponent> components, String header) throws IOException {
		StringBuilder builder1 = new StringBuilder();
		String tag = header.toLowerCase();
		builder.append("\t\t<" + tag + ">");

		for (UIComponent component : components) {
			if (component.isRendered()) {
				String value = exportValue(FacesContext.getCurrentInstance(), component);

				builder1.append(value);
			}
		}

		builder.append(builder1.toString());
		
		builder.append("</" + tag + ">\n");
	}
	
	private void addColumnValue(StringBuilder builder, String footer, String header) throws IOException {
		String tag = header.toLowerCase();
		builder.append("\t\t<" + tag + ">");

		builder.append(footer.toLowerCase());
		
		builder.append("</" + tag + ">\n");
	}
	
}
