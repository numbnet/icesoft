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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class FileResource implements Resource {
    private File file;

    public FileResource(File file) {
        this.file = file;
    }

    public String calculateDigest() {
        return file.getPath();
    }

    public Date lastModified() {
        return new Date(file.lastModified());
    }

    public InputStream open() throws IOException {
        return new FileInputStream(file);
    }

    public void withOptions(Options options) throws IOException {
        options.setLastModified(new Date(file.lastModified()));
        options.setFileName(file.getName());
    }
    
    public File getFile(){
    	return file;
    }
}
