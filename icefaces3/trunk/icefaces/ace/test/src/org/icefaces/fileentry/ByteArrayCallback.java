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
import org.icefaces.ace.component.fileentry.FileEntryStatus;

import java.io.ByteArrayOutputStream;

public class ByteArrayCallback implements FileEntryCallback {
    private ByteArrayOutputStream out;
    private FileEntryResults.FileInfo beginFileInfo;
    private FileEntryResults.FileInfo endFileInfo;

    public void begin(FileEntryResults.FileInfo fileInfo) {
        out = new ByteArrayOutputStream(16*1024);
        beginFileInfo = fileInfo;
    }

    public void write(byte[] buffer, int offset, int length) {
        out.write(buffer, offset, length);
    }

    public void write(int data) {
        out.write(data);
    }

    public void end(FileEntryResults.FileInfo fileInfo) {
        endFileInfo = fileInfo;
        System.out.println("Callback fileInfo objects same: " + (beginFileInfo == endFileInfo));
    }
}
