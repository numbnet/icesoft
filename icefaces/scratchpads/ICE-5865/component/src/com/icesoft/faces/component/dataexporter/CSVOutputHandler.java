/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.component.dataexporter;

import java.io.FileWriter;
import java.io.IOException;

public class CSVOutputHandler extends OutputTypeHandler{
	
	StringBuffer buffer;
	int rowIndex = 0;

	public CSVOutputHandler(String path) {
		super(path);
		buffer = new StringBuffer();
		this.mimeType = "text/csv";
	}

	public void flushFile() {
        deleteComma();
		try{
			FileWriter fw = new FileWriter(getFile());
			fw.write(buffer.toString());
			fw.close();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	public void writeCell(Object output, int col, int row) {
		if( row != rowIndex ){
            deleteComma();
			buffer.append("\n");
			rowIndex ++;
		}
		buffer.append(escapeString(output));
		buffer.append(",");
	}

	public void writeHeaderCell(String text, int col) {
		//do nothing, no header to write for csv		
	}

    private void deleteComma() {
        int comma = buffer.lastIndexOf(",");
        if (comma > 0) {
            buffer.deleteCharAt(comma);
        }
    }
    
    protected String escapeString(Object output) {
        if (output == null) {
            return "";
        }
        else {
            StringBuffer sb = new StringBuffer(output.toString());
            boolean addDoubleQuotes = ( sb.indexOf(",") >= 0 );
            if (sb.indexOf("\"") >= 0) {
                addDoubleQuotes = true;
                for (int i = sb.length() - 1; i >= 0; i--) {
                    if (sb.charAt(i) == '"') {
                        sb.insert(i, '"');
                    }
                }
            }
            if (addDoubleQuotes) {
                sb.insert(0, '"');
                sb.append('"');
            }
            return sb.toString();
        }
    }

}
