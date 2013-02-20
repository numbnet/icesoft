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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A File implementation of the Resource interface.
 *
 */
public class FileResource extends AbstractResource {

    private File wrappedFile;
    private ResourceStore<FileResource> store;

    /**
     * Get the wrapped File for the resource.
     * 
     * @return The wrapped File
     */
    public File getFile() {
        return wrappedFile;
    }

    /**
     * Set the wrapped File for the resource.
     * 
     * @param file The resource file
     */
    public void setFile(File file) {
        this.wrappedFile = file;
    }

    /**
     * Delete the wrapped file. 
     * 
     * If the store is not null, store.remove(resource)
     * will also be called.
     */
    public void delete() {
        if (wrappedFile.exists()) {
            wrappedFile.delete();
        }
        if (store != null) {
            store.remove(this);
        }
    }

    /**
     * Set the ResourceStore for this resource.
     * 
     * @param store the ResourceStore
     */
    public void setStore(ResourceStore store) {
        this.store = store;
    }

    /**
     * Set the InputStream for this resource.
     * 
     * @param is The resource stream
     */
    public void setInputStream(InputStream is) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(wrappedFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get the InputStream for the resource 
     * as a FileInputSteam.
     * 
     * @return The FileInputStream
     */
    public InputStream getInputStream() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(wrappedFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stream;
    }

    public String toString() {
        return "FileResource [wrappedFile=" + wrappedFile + ", store=" + store
                + ", id=" + id + ", contentType=" + contentType + ", token="
                + token + ", name=" + name + ", uuid=" + uuid + "]";
    }

    /**
     * Get the file length
     * 
     * @return the file size in bytes
     */
    public long contentLength() {
        if( wrappedFile == null ){
            return 0;
        }
        else{
            FileInputStream is = null;
            try {
                is = new FileInputStream(wrappedFile);
                long result = is.getChannel().size();
                is.close();
                return result;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            } finally{
                if( is != null ){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        }
    }

}
