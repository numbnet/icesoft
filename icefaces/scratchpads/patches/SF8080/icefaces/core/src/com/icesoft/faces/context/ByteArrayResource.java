package com.icesoft.faces.context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class ByteArrayResource implements Resource {
    private final Date lastModified = new Date();
    private byte[] content;

    public ByteArrayResource(byte[] content) {
        this.content = content;
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
}
