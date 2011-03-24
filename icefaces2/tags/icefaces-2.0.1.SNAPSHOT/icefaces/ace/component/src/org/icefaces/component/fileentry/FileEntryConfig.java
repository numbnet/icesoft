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

package org.icefaces.component.fileentry;

import java.io.Serializable;

/**
 * The uploaded files need to be processed before RestoreViewPhase, meaning
 * that the FileEntry component is not yet available, to direct how and where
 * to save the files. So, any information that is needed to process uploaded
 * files needs to be accessible without a direct reference to the FileEntry.
 */
public class FileEntryConfig implements Serializable {
    private String identifier;
    private String clientId;
    
    private String absolutePath;
    private String relativePath;
    private boolean useSessionSubdir;
    private boolean useOriginalFilename;
    private String callbackEL;
    private long maxTotalSize;
    private long maxFileSize;
    private int maxFileCount;
    private boolean required;

    private String progressResourcePath;
    private String progressGroupName;
    
    /**
     * InputFile uses this for publishing its own property configuration
     */
    public FileEntryConfig(
        String identifier,
        String clientId,
        String absolutePath,
        String relativePath,
        boolean useSessionSubdir,
        boolean useOriginalFilename,
        String callbackEL,
        long maxTotalSize,
        long maxFileSize,
        int maxFileCount,
        boolean required,
        String progressResourcePath,
        String progressGroupName) {
        
        this.identifier = identifier;
        this.clientId = clientId;
        this.absolutePath = absolutePath;
        this.relativePath = relativePath;
        this.useSessionSubdir = useSessionSubdir;
        this.useOriginalFilename = useOriginalFilename;
        this.callbackEL = callbackEL;
        this.maxTotalSize = maxTotalSize;
        this.maxFileSize = maxFileSize;
        this.maxFileCount = maxFileCount;
        this.required = required;
        this.progressResourcePath = progressResourcePath;
        this.progressGroupName = progressGroupName;
    }


    public String getIdentifier() {
        return identifier;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getAbsolutePath() {
        return absolutePath;
    }
    
    public String getRelativePath() {
        return relativePath;
    }
    
    public boolean isUseSessionSubdir() {
        return useSessionSubdir;
    }
    
    public boolean isUseOriginalFilename() {
        return useOriginalFilename;
    }
    
    public String getCallbackEL() {
        return callbackEL;
    }
    
    public boolean isViaCallback() {
        return callbackEL != null;
    }
    
    public long getMaxTotalSize() {
        return maxTotalSize;
    }
    
    public long getMaxFileSize() {
        return maxFileSize;
    }
    
    public int getMaxFileCount() {
        return maxFileCount;
    }

    public boolean isRequired() {
        return required;
    }

    public String getProgressResourcePath() {
        return progressResourcePath;
    }

    public String getProgressGroupName() {
        return progressGroupName;
    }


    public String toString() {
        return
            "FileEntryConfig: {" +
            "\n  clientId=" + clientId +
            ",\n  absolutePath=" + absolutePath +
            ",\n  relativePath=" + relativePath +
            ",\n  useSessionSubdir=" + useSessionSubdir +
            ",\n  useOriginalFilename=" + useOriginalFilename +
            ",\n  callbackEL=" + callbackEL +
            ",\n  maxTotalSize=" + maxTotalSize +
            ",\n  maxFileSize=" + maxFileSize +
            ",\n  maxFileCount=" + maxFileCount +
            ",\n  required=" + required +
            ",\n  identifier=" + identifier +
            ",\n  progressResourcePath=" + progressResourcePath +
            ",\n  progressGroupName=" + progressGroupName +
            "\n}";
    }
}
