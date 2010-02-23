package com.icesoft.faces.component.inputfile;

import java.io.IOException;

/**
 * The Exception thrown when the inputFile has been configured to write to
 * a java.io.OutputStrean, instead of to a java.io.File, but the OutputStream
 * was null.
 * 
 * @author mcollette
 * @since 1.8.3
 */
public class FileUploadNullOutputStreamException extends IOException {
    public FileUploadNullOutputStreamException() {
        super();
    }
    
    public FileUploadNullOutputStreamException(String msg) {
        super(msg);
    }
}
