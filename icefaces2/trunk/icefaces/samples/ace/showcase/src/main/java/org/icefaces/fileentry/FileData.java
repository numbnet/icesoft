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
package org.icefaces.fileentry;

import org.icefaces.component.fileentry.FileEntryResults;

import java.io.Serializable;

/**
 * <p>The FileData class is a simple wrapper/storage for an object that is
 * returned by the fileEntry component.  The FileInfo class contains file
 * attributes that are associated during the file upload process.</p>
 */
public class FileData implements Serializable {

    // file info attributes
    private FileEntryResults.FileInfo fileInfo;
    private int id;

    /**
     * Create a new FileData object.
     *
     * @param fileInfo fileInfo object created by the fileEntry component
     */
    public FileData(FileEntryResults.FileInfo fileInfo, int id) {
        this.fileInfo = fileInfo;
        this.id = id;
    }

    public FileEntryResults.FileInfo getFileInfo() {
        return fileInfo;
    }
    
    public int getId() {
        return id;
    }

    /**
     * Method to return the file size as a formatted string
     * For example, 4096 bytes would be returned as 4 KB
     *
     *@return formatted file size
     */
    public String getSizeFormatted() {
        long ourLength = fileInfo.getSize();
        
        // Generate formatted label, such as 4kb, instead of just a plain number
        if (ourLength >= FileEntryController.MEGABYTE_LENGTH_BYTES) {
            return ourLength / FileEntryController.MEGABYTE_LENGTH_BYTES + " MB";
        }
        else if (ourLength >= FileEntryController.KILOBYTE_LENGTH_BYTES) {
            return ourLength / FileEntryController.KILOBYTE_LENGTH_BYTES + " KB";
        }
        else if (ourLength == 0) {
            return "0";
        }
        else if (ourLength < FileEntryController.KILOBYTE_LENGTH_BYTES) {
            return ourLength + " B";
        }
        
        return Long.toString(ourLength);
    }    
}
