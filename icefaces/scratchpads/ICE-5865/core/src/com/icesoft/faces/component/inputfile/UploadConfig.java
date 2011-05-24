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
 
package com.icesoft.faces.component.inputfile;

import java.io.Serializable;

/**
 * With state saving, the InputFile component exists only within a Lifecycle,
 * so any information that the UploadServer needs to process a file upload,
 * needs to be accessible without a direct reference to the InputFile.
 * 
 * Fields like sizeMax, uniqueFolder, uploadDirectory, uploadDirectoryAbsolute
 * take precendence over corresponding context-params in the web.xml. These 
 * fields being null means they're not set on the InputFile component, so the
 * UploadServer will use the context-params, or the context-param default values.
 * 
 * @since 1.8
 */
public class UploadConfig implements Serializable {
    private String clientId;
    // form in the component tree, not the rendered iframe
    private String formClientId;
    private Long sizeMax;
    private String fileNamePattern;
    private Boolean uniqueFolder;
    private String uploadDirectory;
    private Boolean uploadDirectoryAbsolute;
    private boolean progressRender;
    private boolean progressListener;
    private boolean failOnEmptyFile;
    private boolean outputStream;

    /**
     * InputFile uses this for publishing its own property configuration
     */
    public UploadConfig(
        String clientId,
        String formClientId,
        Long sizeMax,
        String fileNamePattern,
        Boolean uniqueFolder,
        String uploadDirectory,
        Boolean uploadDirectoryAbsolute,
        boolean progressRender,
        boolean progressListener,
        boolean failOnEmptyFile,
        boolean outputStream) {
        
        this.clientId = clientId;
        this.formClientId = formClientId;
        this.sizeMax = sizeMax;
        this.fileNamePattern = fileNamePattern;
        this.uniqueFolder = uniqueFolder;
        this.uploadDirectory = uploadDirectory;
        this.uploadDirectoryAbsolute = uploadDirectoryAbsolute;
        this.progressRender = progressRender;
        this.progressListener = progressListener;
        this.failOnEmptyFile = failOnEmptyFile;
        this.outputStream = outputStream;
    }

    /**
     * UploadServer uses this to resolve the combination of context-params
     * with the InputFile's properties.
     */
    public UploadConfig(
        UploadConfig componentUploadConfig,
        String clientId,
        long sizeMax,
        boolean uniqueFolder,
        String uploadDirectory,
        boolean uploadDirectoryAbsolute) {
        
        this.clientId = clientId;
        this.sizeMax = new Long(sizeMax);
        this.uniqueFolder = Boolean.valueOf(uniqueFolder);
        this.uploadDirectory = uploadDirectory;
        this.uploadDirectoryAbsolute = Boolean.valueOf(uploadDirectoryAbsolute);
        this.progressRender = false;
        this.progressListener = false;
        this.failOnEmptyFile = true;
        this.outputStream = false;
        
        if (componentUploadConfig != null) {
            if (componentUploadConfig.formClientId != null) {
                this.formClientId = componentUploadConfig.formClientId;
            }
            if (componentUploadConfig.sizeMax != null) {
                this.sizeMax = componentUploadConfig.sizeMax;
            }
            if (componentUploadConfig.fileNamePattern != null) {
                this.fileNamePattern = componentUploadConfig.fileNamePattern;
            }
            if (componentUploadConfig.uniqueFolder != null) {
                this.uniqueFolder = componentUploadConfig.uniqueFolder;
            }
            if (componentUploadConfig.uploadDirectory != null) {
                this.uploadDirectory = componentUploadConfig.uploadDirectory;
            }
            if (componentUploadConfig.uploadDirectoryAbsolute != null) {
                this.uploadDirectoryAbsolute = componentUploadConfig.uploadDirectoryAbsolute;
            }
            this.progressRender = componentUploadConfig.progressRender;
            this.progressListener = componentUploadConfig.progressListener;
            this.failOnEmptyFile = componentUploadConfig.failOnEmptyFile;
            this.outputStream = componentUploadConfig.outputStream;
        }
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getFormClientId() {
        return formClientId;
    }
    
    public Long getSizeMax() {
        return sizeMax;
    }
    
    public String getFileNamePattern() {
        return fileNamePattern;
    }
    
    public Boolean getUniqueFolder() {
        return uniqueFolder;
    }
    
    public String getUploadDirectory() {
        return uploadDirectory;
    }
    
    public Boolean getUploadDirectoryAbsolute() {
        return uploadDirectoryAbsolute;
    }
    
    public boolean isProgressRender() {
        return progressRender;
    }
    
    public boolean isProgressListener() {
        return progressListener;
    }

    public boolean isFailOnEmptyFile() {
        return failOnEmptyFile;
    }
    
    public boolean isOutputStream() {
        return outputStream;
    }
    
    public String toString() {
        return
            "UploadConfig: {" +
            "\n  clientId=" + clientId +
            ",\n  formClientId=" + formClientId +
            ",\n  sizeMax=" + sizeMax +
            ",\n  fileNamePattern=" + fileNamePattern +
            ",\n  uniqueFolder=" + uniqueFolder +
            ",\n  uploadDirectory=" + uploadDirectory +
            ",\n  uploadDirectoryAbsolute=" + uploadDirectoryAbsolute +
            ",\n  progressRender=" + progressRender +
            ",\n  progressListener=" + progressListener +
            ",\n  failOnEmptyFile=" + failOnEmptyFile +
            ",\n  outputStream=" + outputStream +
            "\n}";        
    }
}
