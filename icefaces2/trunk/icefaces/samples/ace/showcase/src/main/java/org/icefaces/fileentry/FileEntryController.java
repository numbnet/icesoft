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
package org.icefaces.fileentry;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;
import java.io.Serializable;

import org.icefaces.component.fileentry.FileEntry;
import org.icefaces.component.fileentry.FileEntryResults;
import org.icefaces.component.fileentry.FileEntryEvent;

/**
 * <p>The FileEntryController is responsible for the file upload
 * logic as well as the file deletion logic.  A user's file uploads are only
 * visible to them, and are deleted when the session is destroyed.</p>
 */
@ManagedBean(name="fileEntryController")
@SessionScoped
public class FileEntryController implements Serializable {
    // File sizes used to generate formatted label
    public static final long MEGABYTE_LENGTH_BYTES = 1048576L;
    public static final long KILOBYTE_LENGTH_BYTES = 1024L;
    
    private int idCounter = 0;

    // files associated with the current user
    private final List fileList =
            Collections.synchronizedList(new ArrayList());
    
    /**
     * <p>Action event method which is triggered when a user clicks on the
     * upload file button.  Uploaded files are added to a list so that user have
     * the option to delete them programatically.  Any errors that occurs
     * during the file uploaded are added the messages output.</p>
     *
     * @param event jsf action event.
     */
    public void listener(FileEntryEvent event) {
        FileEntry fileEntry = (FileEntry) event.getSource();
        FileEntryResults results = fileEntry.getResults();
        for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
//System.out.println("Uploaded file: " + fileInfo.getFileName());
            if (fileInfo.isSaved()) {
//System.out.println("  Saved, adding to list");
                FileData fileData = new FileData(fileInfo, getIdCounter());
                synchronized (fileList) {
                    fileList.add(fileData);
                }
            }
        }
    }

    /**
     * <p>Allows a user to remove a file from a list of uploaded files.  This
     * methods assumes that a request param "fileId" has been set to a valid
     * FileData id that the user wishes to delete.</p>
     *
     * @param event jsf action event
     */
    public void removeUploadedFile(ActionEvent event) {
        // Get the inventory item ID from the context.
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String fileIdStr = (String) map.get("fileId");
        int fileId = Integer.parseInt(fileIdStr);
//System.out.println("removeUploadedFile()  fileId: " + fileId);

        synchronized (fileList) {
            FileData fileData;
            for (int i = 0; i < fileList.size(); i++) {
                fileData = (FileData)fileList.get(i);
                // remove our file
                if (fileData.getId() == fileId) {
//System.out.println("removeUploadedFile()    found: " + fileData.getFileInfo().getFileName());
                    boolean del = fileData.getFileInfo().getFile().delete();
//System.out.println("removeUploadedFile()    deleted: " + del);
                    fileList.remove(i);
                    break;
                }
            }
        }
    }

    private synchronized int getIdCounter() {
        return ++idCounter;
    }
    
    public List getFileList() {
        return fileList;
    }
    
    public boolean isEmptyFileList() {
        return fileList.isEmpty();
    }
}
