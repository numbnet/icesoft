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

package com.icesoft.faces.component.inputfile;

import java.io.Serializable;

public class FileInfo implements Cloneable, Serializable {
    private long size = 0;
    private String fileName = null;
    private String contentType = null;
    private String physicalPath = null;
    private int percent = 0;
    private Exception exception = null;
    private boolean preUpload = false;
    private boolean postUpload = false;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String path) {
        this.physicalPath = path;
    }

    public FileInfo() {
        super();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * InputFile can now send progress events before the file upload begins,
     * and after it has completed, to facilitate synchronous mode, so that
     * applications may turn on and off an indeterminate progress bar during
     * the file upload, since they can't push incremental progress updates.
     * @return If this lifecycle is before the file upload begins.
     */
    public boolean isPreUpload() {
        return preUpload;
    }
    
    public void setPreUpload(boolean pre) {
        preUpload = pre;
    }
    
    /**
     * InputFile can now send progress events before the file upload begins,
     * and after it has completed, to facilitate synchronous mode, so that
     * applications may turn on and off an indeterminate progress bar during
     * the file upload, since they can't push incremental progress updates.
     * @return If this lifecycle is after the file upload finishes.
     */
    public boolean isPostUpload() {
        return postUpload;
    }
    
    public void setPostUpload(boolean post) {
        postUpload = post;
    }
    
    void reset() {
        size = 0;
        fileName = null;
        contentType = null;
        physicalPath = null;
        percent = 0;
        exception = null;
        preUpload = false;
        postUpload = false;
    }
    
    public Object clone() {
        FileInfo fi = new FileInfo();
        fi.size         = this.size;
        fi.fileName     = this.fileName;
        fi.contentType  = this.contentType;
        fi.physicalPath = this.physicalPath;
        fi.percent      = this.percent;
        fi.exception    = this.exception;
        fi.preUpload    = this.preUpload;
        fi.postUpload   = this.postUpload;
        return fi;
    }
    
    public String toString() {
        return
            "FileInfo: {" +
            "\n  percent=" + percent +
            ",\n  preUpload=" + preUpload +
            ",\n  postUpload=" + postUpload +
            ",\n  exception=" + exception +
            ",\n  fileName=" + fileName +
            ",\n  physicalPath=" + physicalPath +
            ",\n  contentType=" + contentType +
            ",\n  size=" + size +
            "\n}";        
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileInfo = (FileInfo) o;

        if (percent != fileInfo.percent) return false;
        if (postUpload != fileInfo.postUpload) return false;
        if (preUpload != fileInfo.preUpload) return false;
        if (size != fileInfo.size) return false;
        if (contentType != null ? !contentType.equals(fileInfo.contentType) : fileInfo.contentType != null)
            return false;
        if (exception != null ? !exception.equals(fileInfo.exception) : fileInfo.exception != null) return false;
        if (fileName != null ? !fileName.equals(fileInfo.fileName) : fileInfo.fileName != null) return false;
        if (physicalPath != null ? !physicalPath.equals(fileInfo.physicalPath) : fileInfo.physicalPath != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (int) (size ^ (size >>> 32));
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (physicalPath != null ? physicalPath.hashCode() : 0);
        result = 31 * result + percent;
        result = 31 * result + (exception != null ? exception.hashCode() : 0);
        result = 31 * result + (preUpload ? 1 : 0);
        result = 31 * result + (postUpload ? 1 : 0);
        return result;
    }
}
