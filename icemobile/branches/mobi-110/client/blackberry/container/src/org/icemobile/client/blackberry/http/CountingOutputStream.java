/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */ 
package org.icemobile.client.blackberry.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Code borrowed from http://stackoverflow.com/questions/254719/file-upload-with-java-with-progress-bar
 * 
 * Currently unused as the upload request for Blackberry leverages 
 * the BrowserFieldRequest object, which doesn't expose hooks for this sort of 
 * thing. 
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