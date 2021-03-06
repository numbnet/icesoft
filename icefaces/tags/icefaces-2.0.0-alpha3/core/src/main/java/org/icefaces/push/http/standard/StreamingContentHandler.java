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

package org.icefaces.push.http.standard;

import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class StreamingContentHandler implements ResponseHandler {
    private String mimeType;
    private String characterSet;

    protected StreamingContentHandler(String mimeType, String characterSet) {
        this.mimeType = mimeType;
        this.characterSet = characterSet;
    }

    public abstract void writeTo(Writer writer) throws IOException;

    public void respond(Response response) throws Exception {
        response.setHeader("Content-Type", mimeType + "; charset=" + characterSet);
        OutputStreamWriter writer = new OutputStreamWriter(response.writeBody(), characterSet);
        writeTo(writer);
        writer.flush();
    }
}
