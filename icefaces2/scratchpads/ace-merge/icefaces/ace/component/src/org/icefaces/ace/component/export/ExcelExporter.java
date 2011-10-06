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
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;

public class ExcelExporter extends Exporter {

    @Override
	public void export(FacesContext facesContext, DataTable table, String filename, boolean pageOnly, int[] excludeColumns, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor, boolean includeHeaders, boolean includeFooters, boolean selectedRowsOnly) throws IOException {    	
    	Workbook wb = new HSSFWorkbook();
    	Sheet sheet = wb.createSheet();
    	List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	int numberOfColumns = columns.size();
    	if (preProcessor != null) {
    		preProcessor.invoke(facesContext.getELContext(), new Object[]{wb});
    	}

    	int first = pageOnly ? table.getFirst() : 0;
    	int rowsToExport = pageOnly ? (first + table.getRows()) : table.getRowCount();
    	int sheetRowIndex = 0;

        if (includeHeaders) {
			ColumnGroup columnGroup = getColumnGroupHeader(table);
			if (columnGroup != null) {
				List<org.icefaces.ace.component.row.Row> rows = getRows(columnGroup);
				for (org.icefaces.ace.component.row.Row row : rows) {
					List<UIColumn> rowColumns = getRowColumnsToExport(row, table, excludeColumns);
					addFacetColumns(sheet, rowColumns, ColumnType.HEADER, sheetRowIndex++);
				}
			} else {
				sheetRowIndex = 1;
				addFacetColumns(sheet, columns, ColumnType.HEADER, 0);
			}
		}
		
		Object originalData = null;
		if (selectedRowsOnly) {
			originalData = table.getValue();
			table.setValue(table.getStateMap().getSelected());
			first = 0;
			rowsToExport = table.getRowCount();
		}
    	
    	for (int i = first; i < rowsToExport; i++) {
    		table.setRowIndex(i);
			Row row = sheet.createRow(sheetRowIndex++);
			
			for (int j = 0; j < numberOfColumns; j++) {
                addColumnValue(row, columns.get(j).getChildren(), j);
			}
		}
		
		if (selectedRowsOnly) {
			table.setValue(originalData);
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(sheet, columns, ColumnType.FOOTER, sheetRowIndex++);
        }
    	
    	table.setRowIndex(-1);
    	
    	if (postProcessor != null) {
    		postProcessor.invoke(facesContext.getELContext(), new Object[]{wb});
    	}
    	
    	writeExcelToResponse(facesContext.getExternalContext(), wb, filename);
	}
	
	private void addFacetColumns(Sheet sheet, List<UIColumn> columns, ColumnType columnType, int rowIndex) {
        Row rowHeader = sheet.createRow(rowIndex);

        for (int i = 0; i < columns.size(); i++) {
            UIColumn uiColumn = (UIColumn) columns.get(i);
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			
            if (facet != null) {
				addColumnValue(rowHeader, facet, i);
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
				Cell cell = rowHeader.createCell(i);
				cell.setCellValue(new HSSFRichTextString(value));
			}
        }
    }
	
    private void addColumnValue(Row rowHeader, UIComponent component, int index) {
        Cell cell = rowHeader.createCell(index);
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

        cell.setCellValue(new HSSFRichTextString(value));
    }
    
    private void addColumnValue(Row rowHeader, List<UIComponent> components, int index) {
        Cell cell = rowHeader.createCell(index);
        StringBuilder builder = new StringBuilder();
        
        for (UIComponent component : components) {
        	if (component.isRendered()) {
                String value = exportValue(FacesContext.getCurrentInstance(), component);
                
                if (value != null)
                	builder.append(value);
            }
		}  
        
        cell.setCellValue(new HSSFRichTextString(builder.toString()));
    }
    
    private void writeExcelToResponse(ExternalContext ec, Workbook generatedExcel, String filename) throws IOException {
        ec.setResponseContentType("application/vnd.ms-excel");
        ec.setResponseHeader("Expires", "0");
        ec.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        ec.setResponseHeader("Pragma", "public");
        ec.setResponseHeader("Content-disposition", "attachment;filename=" + filename + ".xls");

        generatedExcel.write(ec.getResponseOutputStream());
    }
}
