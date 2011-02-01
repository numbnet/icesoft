/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.component.dataexporter;

import java.io.File;
import java.io.IOException;

public abstract class OutputTypeHandler {

	protected File file;
	protected String mimeType;

	public String getMimeType() {
		return mimeType;
	}

	public OutputTypeHandler(String path) {
		try {
			file = new File(path);
			file.createNewFile();
			file.deleteOnExit();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public abstract void writeHeaderCell(String text, int col);

    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header 
     */
	public abstract void writeCell(Object output, int col, int row);
    
    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header 
     */
    public void writeFooterCell(Object output, int col, int row) {
        // Empty, instead of abstract, so we don't break any other classes 
        // which extend this
    }

	public abstract void flushFile();

	public File getFile() {
		return file;
	}

}
