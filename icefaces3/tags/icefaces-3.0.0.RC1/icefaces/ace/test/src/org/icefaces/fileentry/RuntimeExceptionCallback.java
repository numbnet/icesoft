/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
