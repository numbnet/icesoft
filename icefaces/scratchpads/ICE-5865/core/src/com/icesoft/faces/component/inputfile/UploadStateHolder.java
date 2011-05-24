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

import java.io.OutputStream;

/**
 * The UploadServer can start a JSF lifecycle either in its own thread, or on 
 * another one. So there has to be a way of passing the state to the 
 * appropriate thread, where it will be put into a thread local field, so that 
 * the FileUploadPhaseListener can find it and
 * 
 * 
 * @since 1.8
 */
public class UploadStateHolder implements Runnable {
    private static ThreadLocal holder = new ThreadLocal();
    
    private UploadConfig uploadConfig;
    private FileInfo fileInfo;
    private boolean asyncLifecycle;
    private OutputStream outputStream;
    private String iframeContent;
    
    public UploadStateHolder(UploadConfig uploadConfig, FileInfo fileInfo) {
        this.uploadConfig = uploadConfig;
        this.fileInfo = fileInfo;
    }
    
    public UploadConfig getUploadConfig() {
        return uploadConfig;
    }
    
    public FileInfo getFileInfo() {
        return fileInfo;
    }
    
    public boolean isAsyncLifecycle() {
        return asyncLifecycle;
    }
    
    public void setAsyncLifecycle(boolean asyncLifecycle) {
        this.asyncLifecycle = asyncLifecycle;
    }
    
    public OutputStream getOutputStream() {
        return outputStream;
    }
    
    public void setOutputStream(OutputStream os) {
        outputStream = os;
    }

    public String getIframeContent() {
        return iframeContent;
    }
    
    public void setIframeContent(String iframeContent) {
        this.iframeContent = iframeContent;
    }

    /**
     * Save this state into the current thread's local storage
     */
    public void install() {
        holder.set(this);
    }

    /**
     * Retrieve the state from the current thread's local storage
     */
    public static UploadStateHolder take() {
        UploadStateHolder current = (UploadStateHolder) holder.get();
        holder.set(null);
        return current;
    }

    /**
     * Deferred install, for when we'll be running on another thread
     */
    public void run() {
        install();
    }
}
