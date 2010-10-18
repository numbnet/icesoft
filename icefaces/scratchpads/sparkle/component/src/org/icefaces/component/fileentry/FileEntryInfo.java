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

package org.icefaces.component.fileentry;

import java.util.ArrayList;
import java.io.Serializable;
import java.io.File;

public class FileEntryInfo implements Serializable, Cloneable {
    private ArrayList<FileInfo> fileInfos;
    private boolean viaCallback;
    private long totalSize;

    FileEntryInfo(boolean viaCallback) {
        fileInfos = new ArrayList<FileInfo>(6);
        this.viaCallback = viaCallback;
    }

    public ArrayList<FileInfo> getFiles() {
        return fileInfos;
    }
    
    public boolean isViaCallback() {
        return viaCallback;
    }

    /**
     * @return Deep copy
     */
    public Object clone() {
        FileEntryInfo info = new FileEntryInfo(viaCallback);
        int numFileEntries = fileInfos.size();
        info.fileInfos = new ArrayList<FileInfo>(Math.max(1,numFileEntries));
        for (FileInfo fi : fileInfos) {
            info.fileInfos.add( (FileInfo) fi.clone() );
        }
        return info;
    }
    
    public String toString() {
        String pre = "FileEntryInfo: {" +
                     "\n  viaCallback=" + viaCallback +
                     ",\n  totalSize=" + totalSize +
                     ",\n  files:\n";
        StringBuilder mid = new StringBuilder();
        for (FileInfo fi : fileInfos) {
            mid.append(fi.toString());
        }
        String post ="\n}";
        return pre + mid + post;
    }
    
    public boolean equals(Object ob) {
        if (!(ob instanceof FileEntryInfo)) {
            return false;
        }
        FileEntryInfo info = (FileEntryInfo) ob;
        if (this.viaCallback != info.viaCallback) {
            return false;
        }
        if ((this.fileInfos == null && info.fileInfos != null) ||
            (this.fileInfos != null && info.fileInfos == null)) {
            return false;
        }
        if (this.fileInfos != null) {
            int sz = this.fileInfos.size();
            int isz = info.fileInfos.size();
            if (sz != isz) {
                return false;
            }
            for (int i = 0; i < sz; i++) {
                FileInfo fi = this.fileInfos.get(i);
                FileInfo ifi = info.fileInfos.get(i);
                if (!fi.equals(ifi)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    void addCompletedFile(FileInfo fileInfo) {
        fileInfos.add(fileInfo);
        totalSize += fileInfo.getSize();
    }
    
    long getAvailableTotalSize(long maxTotalSize) {
        return Math.max(0, maxTotalSize - totalSize);
    }


    public static class FileInfo implements Serializable, Cloneable {
        private String fileName;
        private String contentType;
        private File file;
        private long size;
        private FileEntryStatus status;
        
        FileInfo() {
        }

        void begin(String fileName, String contentType) {
            this.fileName = fileName;
            this.contentType = contentType;
        }
        
        void finish(File file, long size, FileEntryStatus status) {
            this.file = file;
            this.size = size;
            this.status = status;
        }
        
        public String getFileName() { return fileName; }
        public String getContentType() { return contentType; }
        public File getFile() { return file; }
        public long getSize() { return size; }
        public FileEntryStatus getStatus() { return status; }
        
        public boolean isSaved() { return status.isSuccess(); }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FileInfo fileInfo = (FileInfo) o;

            if (fileName != null ? !fileName.equals(fileInfo.fileName) : fileInfo.fileName != null) return false;
            if (contentType != null ? !contentType.equals(fileInfo.contentType) : fileInfo.contentType != null)
                return false;
            if (file != null ? !file.equals(fileInfo.file) : fileInfo.file != null)
                return false;
            if (size != fileInfo.size) return false;
            if (status != fileInfo.status) return false;
            
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
            FileInfo fileInfo = new FileInfo();
            fileInfo.fileName = this.fileName;
            fileInfo.contentType = this.contentType;
            fileInfo.file = this.file;
            fileInfo.size = this.size;
            fileInfo.status = this.status;
            return fileInfo;
        }

        public String toString() {
            return
                "FileEntryInfo.FileInfo: {" +
                    "\n  fileName=" + fileName +
                    ",\n  contentType=" + contentType +
                    ",\n  file=" + file +
                    ",\n  size=" + size +
                    ",\n  status=" + status +
                    "\n}";
        }
    }
}
