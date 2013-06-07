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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A Byte array implementation of a Resource. 
 * 
 * @see org.icemobile.application.Resource
 *
 */
public class ByteArrayResource extends AbstractResource{
    
    private byte[] bytes;
    private ResourceStore<ByteArrayResource> store;
    
    /**
     * Get the InputStream for the resource as a ByteArrayInputStream.
     * 
     * @return the ByteArrayInputStream
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * Delete the resource. 
     * 
     * The wrapped byte array will be emptied. If the store is not
     * null, the store.remove(resource) will also be called. 
     */
    public void delete() {
        this.bytes = null;
        if( store != null ){
            store.remove(this);
        }
    }

    /**
     * Get the bytes for the resource.
     * 
     * @return the byte array
     */
    public byte[] getBytes(){
        return bytes;
    }
    
    /**
     * Set the bytes for the resource.
     * 
     * @param bytes The resource bytes
     */
    public void setBytes(byte[] bytes){
        this.bytes = bytes;
    }

    /**
     * Set the ResourceStore for this resource.
     * 
     * @param store The ResourceStore
     * @see ResourceStore
     */
    public void setStore(ResourceStore store) {
        this.store = store;        
    }

    /**
     * Set the InputStream for the resource. 
     * 
     * @param stream The resource stream
     */
    public void setInputStream(InputStream stream) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if( bos != null ){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if( stream != null ){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String toString() {
        return "ByteArrayResource [bytes=" + contentLength()
                + ", store=" + store + ", id=" + id + ", contentType="
                + contentType + ", token=" + token + ", name=" + name + ", uuid=" + uuid + "]";
    }

    /**
     * The length of the wrapped bytes.
     */
    public long contentLength() {
        return bytes == null ? 0 : bytes.length;
    }

}
