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

package org.icefaces.component.inputFiles;

import java.util.ArrayList;
import java.io.Serializable;
import java.io.File;

public class InputFilesInfo implements Serializable, Cloneable {
    private ArrayList<FileEntry> fileEntries;
    private boolean viaCallback;
    private long totalSize;
    
    InputFilesInfo(boolean viaCallback) {
        fileEntries = new ArrayList<FileEntry>(6);
        this.viaCallback = viaCallback;
    }
    
    public ArrayList<FileEntry> getFiles() {
        return fileEntries;
    }
    
    public boolean isViaCallback() {
        return viaCallback;
    }

    /**
     * @return Deep copy
     */
    public Object clone() {
        InputFilesInfo info = new InputFilesInfo(viaCallback);
        int numFileEntries = fileEntries.size();
        info.fileEntries = new ArrayList<FileEntry>(Math.max(1,numFileEntries));
        for (FileEntry fe : fileEntries) {
            info.fileEntries.add( (FileEntry) fe.clone() );
        }
        return info;
    }
    
    public String toString() {
        String pre = "InputFilesInfo: {" +
                     "\n  viaCallback=" + viaCallback +
                     ",\n  totalSize=" + totalSize +
                     ",\n  files:\n";
        StringBuilder mid = new StringBuilder();
        for (FileEntry fe : fileEntries) {
            mid.append(fe.toString());
        }
        String post ="\n}";
        return pre + mid + post;
    }
    
    public boolean equals(Object ob) {
        if (!(ob instanceof InputFilesInfo)) {
            return false;
        }
        InputFilesInfo info = (InputFilesInfo) ob;
        if (this.viaCallback != info.viaCallback) {
            return false;
        }
        if ((this.fileEntries == null && info.fileEntries != null) ||
            (this.fileEntries != null && info.fileEntries == null)) {
            return false;
        }
        if (this.fileEntries != null) {
            int sz = this.fileEntries.size();
            int isz = info.fileEntries.size();
            if (sz != isz) {
                return false;
            }
            for (int i = 0; i < sz; i++) {
                FileEntry fe = this.fileEntries.get(i);
                FileEntry ife = info.fileEntries.get(i);
                if (!fe.equals(ife)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    void addCompletedFileEntry(FileEntry fileEntry) {
        fileEntries.add(fileEntry);
        totalSize += fileEntry.getSize();
    }
    
    long getAvailableTotalSize(long maxTotalSize) {
        return Math.max(0, maxTotalSize - totalSize);
    }
    
    
    public static class FileEntry implements Serializable, Cloneable {
        private String fileName;
        private String contentType;
        private File file;
        private long size;
        private InputFilesStatus status;
        
        FileEntry() {
        }
        
        void begin(String fileName, String contentType) {
            this.fileName = fileName;
            this.contentType = contentType;
        }
        
        void finish(File file, long size, InputFilesStatus status) {
            this.file = file;
            this.size = size;
            this.status = status;
        }
        
        public String getFileName() { return fileName; }
        public String getContentType() { return contentType; }
        public File getFile() { return file; }
        public long getSize() { return size; }
        public InputFilesStatus getStatus() { return status; }
        
        public boolean isSaved() { return status.isSuccess(); }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FileEntry fileEntry = (FileEntry) o;

            if (fileName != null ? !fileName.equals(fileEntry.fileName) : fileEntry.fileName != null) return false;
            if (contentType != null ? !contentType.equals(fileEntry.contentType) : fileEntry.contentType != null)
                return false;
            if (file != null ? !file.equals(fileEntry.file) : fileEntry.file != null)
                return false;
            if (size != fileEntry.size) return false;
            if (status != fileEntry.status) return false;
            
            return true;
        }

        public int hashCode() {
            int result;
            result = (int) (size ^ (size >>> 32));
            result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
            result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
            result = 31 * result + (file != null ? file.hashCode() : 0);
            result = 31 * result + (status != null ? status.hashCode() : 0);
            return result;
        }

        public Object clone() {
            FileEntry fileEntry = new FileEntry();
            fileEntry.fileName = this.fileName;
            fileEntry.contentType = this.contentType;
            fileEntry.file = this.file;
            fileEntry.size = this.size;
            fileEntry.status = this.status;
            return fileEntry;
        }

        public String toString() {
            return
                "InputFilesInfo.FileEntry: {" +
                    "\n  fileName=" + fileName +
                    ",\n  contentType=" + contentType +
                    ",\n  file=" + file +
                    ",\n  size=" + size +
                    ",\n  status=" + status +
                    "\n}";
        }
    }
}
