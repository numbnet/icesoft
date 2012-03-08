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

import org.icefaces.component.utils.MessageUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Locale;

/**
 * FileEntryStatuses are the built-in statuses which correspond to the
 * localised messages defined in the org.icefaces.component.resources.messages 
 * ResourceBundle. These can be overriden by specifing corresponding entries 
 * in the application ResourceBundle.
 * 
 * Refer to the getParameters(-) javadoc for the MessageFormat parameters 
 * that are available to applications which override these messages.
 * 
 * Note: PROBLEM_READING_MULTIPART does not have access to these parameters.
 * 
 * @see #getParameters
 * @see #PROBLEM_READING_MULTIPART
 */
public enum FileEntryStatuses implements FileEntryStatus {
    UPLOADING(false),
    SUCCESS(true),
    INVALID(false),
    MAX_TOTAL_SIZE_EXCEEDED(false) {
        @Override
        public FacesMessage getFacesMessage(FacesContext facesContext,
                UIComponent fileEntry, FileEntryResults.FileInfo fi) {
            String pattern = (String) fileEntry.getAttributes().get(
                "maxTotalSizeMessage");
            return (pattern != null && pattern.length() > 0) ?
                getFacesMessage(facesContext, fileEntry, fi, pattern) :
                super.getFacesMessage(facesContext, fileEntry, fi);
        }
    },
    MAX_FILE_SIZE_EXCEEDED(false) {
        @Override
        public FacesMessage getFacesMessage(FacesContext facesContext,
                UIComponent fileEntry, FileEntryResults.FileInfo fi) {
            String pattern = (String) fileEntry.getAttributes().get(
                "maxFileSizeMessage");
            return (pattern != null && pattern.length() > 0) ?
                getFacesMessage(facesContext, fileEntry, fi, pattern) :
                super.getFacesMessage(facesContext, fileEntry, fi);
        }
    },
    MAX_FILE_COUNT_EXCEEDED(false) {
        @Override
        public FacesMessage getFacesMessage(FacesContext facesContext,
                UIComponent fileEntry, FileEntryResults.FileInfo fi) {
            String pattern = (String) fileEntry.getAttributes().get(
                "maxFileCountMessage");
            return (pattern != null && pattern.length() > 0) ?
                getFacesMessage(facesContext, fileEntry, fi, pattern) :
                super.getFacesMessage(facesContext, fileEntry, fi);
        }
    },

    /**
     * This one message is for when users have not uploaded a file, so there
     * is no FileInfo to make use of, limiting the useful parameters to just
     * the fileEntry label. 
     */
    REQUIRED(false) {
        @Override
        public FacesMessage getFacesMessage(FacesContext facesContext,
                UIComponent fileEntry, FileEntryResults.FileInfo fi) {
            FacesMessage.Severity sev = getSeverity();
            Object[] params = getParametersForRequired(facesContext, fileEntry);
            String pattern = (String) fileEntry.getAttributes().get(
                "requiredMessage");
            if (pattern != null && pattern.length() > 0) {
                Locale locale = facesContext.getViewRoot().getLocale();
                return MessageUtils.getMessage(locale, sev, pattern, pattern, params);
            }
            else {
                String messageId = MESSAGE_KEY_PREFIX + name();
                return MessageUtils.getMessage(facesContext, sev, messageId, params);
            }
        }
    },
    UNKNOWN_SIZE(false),
    UNSPECIFIED_NAME(false),
    INVALID_CONTENT_TYPE(false),

    /**
     * If there's a RuntimeException thrown by the FileEntryCallback, this
     * will be the status. Ideally, any FileEntryCallback will trap its
     * Exceptions, and return a custom status.
     */
    PROBLEM_WITH_CALLBACK(false),

    /**
     * This one message is for when we have problems reading the multipart
     * form submit, and so do not know which file or other form field has
     * caused the postback to fail. 
     */
    PROBLEM_READING_MULTIPART(false) {
        @Override
        public FacesMessage getFacesMessage(FacesContext facesContext,
                UIComponent fileEntry, FileEntryResults.FileInfo fi) {
            FacesMessage.Severity sev = getSeverity();
            String messageId = MESSAGE_KEY_PREFIX + name();
            return MessageUtils.getMessage(facesContext, sev, messageId, null);
        }
    };
    
    
    private static final String MESSAGE_KEY_PREFIX =
        "org.icefaces.component.fileEntry.";
    
    private boolean success;
    

    private FileEntryStatuses(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public FacesMessage getFacesMessage(FacesContext facesContext,
            UIComponent fileEntry, FileEntryResults.FileInfo fi) {
//System.out.println("FileEntryStatuses.getFacesMessage()");
        FacesMessage.Severity sev = getSeverity();
//System.out.println("FileEntryStatuses.getFacesMessage()  sev: " + sev);
        String messageId = MESSAGE_KEY_PREFIX + name();
//System.out.println("FileEntryStatuses.getFacesMessage()  messageId: " + messageId);
        Object[] params = getParameters(facesContext, fileEntry, fi);
        FacesMessage fm = MessageUtils.getMessage(facesContext, sev, messageId, params);
//System.out.println("FileEntryStatuses.getFacesMessage()  fm: " + fm);
        return fm;
    }
    
    protected FacesMessage.Severity getSeverity() {
        FacesMessage.Severity sev = isSuccess() ?
            FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR;
        return sev;
    }
    
    /**
     * When formatting the MessageFormat patterns that comes from the 
     * ResourceBundles, the following parameters are provided:
     * 
     * param[0] : label        (Identifies the fileEntry component)
     * param[1] : fileName     (The original file name, on user's computer)
     * param[2] : contentType  (MIME type of uploaded file)
     * param[3] : file         (Stored file, on server)
     * param[4] : size         (Size of the uploaded file)
     * param[5] : maxTotalSize (Maximum sum of all uploaded file sizes)
     * param[6] : maxFileSize  (Maximum size of each uploaded file)
     * param[7] : maxFileCount (Maximum number of uploaded files)
     */
    protected Object[] getParameters(FacesContext facesContext,
            UIComponent fileEntry, FileEntryResults.FileInfo fi) {
        Object[] params = new Object[] {
            fileEntry.getAttributes().get("facesMessageLabel"),
            fi.getFileName(),
            fi.getContentType(),
            fi.getFile(),
            fi.getSize(),
            fileEntry.getAttributes().get("maxTotalSize"),
            fileEntry.getAttributes().get("maxFileSize"),
            fileEntry.getAttributes().get("maxFileCount")
        };
        return params;
    }

    /**
     * When formatting the MessageFormat patterns that comes from the
     * ResourceBundles, for the required status, the following parameters
     * are provided:
     *
     * param[0] : label        (Identifies the fileEntry component)
     */
    protected Object[] getParametersForRequired(
            FacesContext facesContext, UIComponent fileEntry) {
        Object[] params = new Object[] {
            fileEntry.getAttributes().get("facesMessageLabel")
        };
        return params;
    }

    /**
     * Some of the status messages can be overridden by attributes on the 
     * component, which specify the MessageFormat pattern themselves.
     */
    protected FacesMessage getFacesMessage(
            FacesContext facesContext, UIComponent fileEntry,
            FileEntryResults.FileInfo fi, String pattern) {
        Locale locale = facesContext.getViewRoot().getLocale();
        FacesMessage.Severity sev = getSeverity();
        Object[] params = getParameters(facesContext, fileEntry, fi);
        return MessageUtils.getMessage(locale, sev, pattern, pattern, params);
    }
}    
