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

package org.icefaces.component.fileentry;

import java.io.Serializable;

/**
 * When files are being uploaded, they will be saved into the file-system
 * unless an FileEntryCallback is provided, in which case the application
 * may take responsibility for the process of saving the files. This is
 * useful in cases where the files should be saved into a database, or
 * processed in memory before being written to the file-system, etc.
 * 
 * Implementors will be stored in the session, and so must be Serializable,
 * and should not reference large amounts of memory, when not in use.
 * 
 * Each call to begin(-) can be followed by any number of calls to the write(-)
 * methods, and then a call to end(-). Even if the begin(-) call shows that the
 * file had pre-failed (say, due to an invalid file extension), end(-) will
 * still be called, but not any of the write(-) methods. 
 */
public interface FileEntryCallback extends Serializable {
    /**
     * Notify the callback of another file that has been uploaded
     * @param fileName The file name, as given by the user's browser
     * @param fileSize -1 for unknown, else the number of known bytes
     * @param status 
     */
    // Not sure if we can guarantee that we'll always know the file size, with all browsers
    // After writing the end method, I realised that we might know that a file has failed upload, before even commencing.
    // QUESTION: For files that have failed upload, before event beginning, should we simply not call begin at all, or inform the callback?
    public void begin(String fileName, long fileSize, FileEntryStatus status);

    // We write in chunks, as we read them in
    public void write(byte[] buffer, int offset, int length);

    // This can be helpfull for debugging
    public void write(int buffer);

    // If we detect that a file failed, say because it's over quota, then we'll tell the callback, and it might massage the result, to still accept the file, or possibly to fail the file that we thought was ok.
    // They can even return a custom status, with its own message format for the faces message.
    public FileEntryStatus end(FileEntryStatus status);
}