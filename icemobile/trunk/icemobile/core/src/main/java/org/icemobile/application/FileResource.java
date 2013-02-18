/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icemobile.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResource implements Resource{
    
    private File wrappedFile;
    private String contentType;
    
    public FileResource(File file, String contentType){
        this.contentType = contentType;
        this.wrappedFile = file;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        InputStream stream = null;
        try{
            stream = new FileInputStream(wrappedFile);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return stream;
    }

    public void setContentType(String type) {
        this.contentType = type;        
    }

}
