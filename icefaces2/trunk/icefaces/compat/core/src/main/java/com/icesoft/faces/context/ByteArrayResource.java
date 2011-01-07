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

package com.icesoft.faces.context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Arrays;

public class ByteArrayResource implements Resource, Serializable {
    private final Date lastModified;
    private byte[] content;

    public ByteArrayResource(byte[] content) {
        this.content = content;
        this.lastModified = new Date();
    }

    public String calculateDigest() {
        return String.valueOf(content);
    }

    public Date lastModified() {
        return lastModified;
    }

    public InputStream open() throws IOException {
        return new ByteArrayInputStream(content);
    }

    public void withOptions(Options options) throws IOException {
        //no options
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof ByteArrayResource)) {
            return false;
        }
        ByteArrayResource bar = (ByteArrayResource) obj;
        if (!lastModified.equals(bar.lastModified))
            return false;
        if (!Arrays.equals(content, bar.content)) {
            return false;
        }
        return true;
    }
}
