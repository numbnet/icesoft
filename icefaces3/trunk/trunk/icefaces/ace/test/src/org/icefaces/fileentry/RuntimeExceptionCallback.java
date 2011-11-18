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

import org.icefaces.ace.component.fileentry.FileEntryCallback;
import org.icefaces.ace.component.fileentry.FileEntryResults;

public class RuntimeExceptionCallback implements FileEntryCallback {
    private RuntimeException throwable;
    private boolean onBegin;
    private boolean onWrite;
    private boolean onEnd;

    RuntimeExceptionCallback(RuntimeException throwable, boolean onBegin, boolean onWrite, boolean onEnd) {
        this.throwable = throwable;
        this.onBegin = onBegin;
        this.onWrite = onWrite;
        this.onEnd = onEnd;
    }
    public void begin(FileEntryResults.FileInfo fileInfo) {
        if (onBegin) {
            throw throwable;
        }
    }

    public void write(byte[] buffer, int offset, int length) {
        if (onWrite) {
            throw throwable;
        }
    }

    public void write(int data) {
        if (onWrite) {
            throw throwable;
        }
    }

    public void end(FileEntryResults.FileInfo fileInfo) {
        if (onEnd) {
            throw throwable;
        }
    }
}
