/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.component.dataexporter;

import java.io.IOException;

import javax.faces.context.FacesContext;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelOutputHandler extends OutputTypeHandler{
	
	WritableSheet sheet = null;
	WritableWorkbook workbook = null;
	

	public ExcelOutputHandler(String path, FacesContext fc, String title) {
		super(path);
		try{
			WorkbookSettings settings = new WorkbookSettings();
			settings.setLocale(fc.getViewRoot().getLocale());
			workbook = Workbook.createWorkbook(super.getFile());
			sheet = workbook.createSheet(title, 0);
			
			this.mimeType = "application/vnd.ms-excel";
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	
	public void flushFile() {
		try{
			workbook.write();
			workbook.close();
		}
		catch( WriteException ioe){
			ioe.printStackTrace();
		}
		catch( IOException ioe){
			ioe.printStackTrace();
		}
	}

	public void writeCell(Object output, int col, int row) {
		WritableCell cell = null;
		if( output instanceof String ){
			cell = new Label(col, row + 1, (String)output);
		}
		else if( output instanceof Double ){
			cell = new Number(col, row + 1, ((Double)output).doubleValue()); 
		}
		try{
			sheet.addCell(cell);
		}
		catch(WriteException e){
			System.out.println("Could not write excel cell");
			e.printStackTrace();
		}			
		
	}

	public void writeHeaderCell(String text, int col) {
		try{
			WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10); 
			WritableCellFormat arial10format = new WritableCellFormat (arial10font); 
			arial10font.setBoldStyle(WritableFont.BOLD);
			Label label = new Label(col, 0, text, arial10format);
			sheet.addCell(label);
		}
		catch(WriteException we){
			we.printStackTrace();
		}
		
	}

}
