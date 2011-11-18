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
