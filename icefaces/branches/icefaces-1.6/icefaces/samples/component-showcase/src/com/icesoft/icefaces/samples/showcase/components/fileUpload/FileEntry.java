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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package org.icefaces.application.showcase.component.fileUpload;

import java.io.Serializable;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * <p>The FileRegistry class is the utility bean for the inputfile showcase
 * demonstration. It is used to store file information in the files uploaded list.</p>
 *
 * @since 
 */
public class FileEntry implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7625899643164777545L;
	private String absolutePath;
	private String folder;
    private Boolean selected = Boolean.FALSE;

	private String fileName = "none";
	private String fileType = "none";
	
	private long dateModified;
	private long size;


    public FileEntry(File file, String fileType) {
    	fileName=file.getName();
    	absolutePath=file.getAbsolutePath();
    	folder = file.getParent();
    	dateModified = file.lastModified();
    	size=file.length();
    	this.fileType = fileType;
    }

    public FileEntry(File file){
       	fileName=file.getName();
    	absolutePath=file.getAbsolutePath();
    	folder = file.getParent();
    	dateModified = file.lastModified();
    	size=file.length();
    }
      public String getFileName() {
    	 if (!fileName.equals("none"))return fileName;
    	 else return "";
     }

    public String getFileType() {
        if (!fileType.equals("none"))return fileType;
        else return "";
    }

    public String getDateModified() {
    	Date date = new Date(dateModified);  	
        return date.toString();
    }
    public String getFolder(){ return this.folder;}
    public long getSize(){return size;}

    public String getAbsolutePath(){return absolutePath;}

    //to delete a file using rowSelector
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
 
    
}
