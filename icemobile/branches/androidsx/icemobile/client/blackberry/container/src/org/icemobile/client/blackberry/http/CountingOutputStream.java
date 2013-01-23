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
package org.icemobile.client.blackberry.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Code reused from http://stackoverflow.com/questions/254719/file-upload-with-java-with-progress-bar
 * 
 * Currently unused as the upload request for Blackberry leverages 
 * the BrowserFieldRequest object, which doesn't expose fetching 
 * an outputStream
 */
public class CountingOutputStream  extends OutputStream {

    private long transferred = 0;
    private OutputStream wrappedOutputStream_;
    private long chunkSize;
    private long outCount=0;
    private ProgressListener mListener; 

    public CountingOutputStream(final OutputStream out, long chunkSize) {
        super();
        wrappedOutputStream_ = out;
        this.chunkSize = chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
        transferred = 0;
        outCount = 0;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        wrappedOutputStream_.write(b,off,len);
        outCount += len;
        if (outCount >= chunkSize) {
            outCount = 0;
            ++transferred;
            mListener.progress(transferred, chunkSize);
        }
    }

    /**
     * We need to implement this, but this should never be used
     */
    public void write(int b) throws IOException {
        throw new UnsupportedOperationException("write(int) not supported in progress stream");        
    }
}