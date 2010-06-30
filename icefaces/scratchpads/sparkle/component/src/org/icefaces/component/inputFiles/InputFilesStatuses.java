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

public enum InputFilesStatuses implements InputFilesStatus {
    UPLOADING(false, "Uploading"),
    SUCCESS(true, "Success"),
    INVALID(false, "Invalid"),
    MAX_TOTAL_SIZE_EXCEEDED(false, "Exceeded max total size"),
    MAX_FILE_SIZE_EXCEEDED(false, "Exceeded max file size"),
    MAX_FILE_COUNT_EXCEEDED(false, "Exceeded max file count"),
    REQUIRED(false, "No file specified, when one was required"),
    UNKNOWN_SIZE(false, "Unknown file size"),
    UNSPECIFIED_NAME(false, "Unspecified name"),
    INVALID_CONTENT_TYPE(false, "Invalid content type");

    private InputFilesStatuses(boolean success, String messageFormat) {
        this.success = success;
        this.messageFormat = messageFormat;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessageFormat() {
        return this.messageFormat;
    }

    private boolean success;
    private String messageFormat;
}    
