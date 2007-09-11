package com.icesoft.faces.context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

public class StringResource implements Resource {
    private static final Date LastModified = new Date();
    private String content;
    private String encoding;

    public StringResource(String content) {
        this(content, "UTF-8");
    }

    public StringResource(String content, String encoding) {
        this.content = content;
        this.encoding = encoding;
    }

    public String calculateDigest() {
        return content;
    }

    public Date lastModified() {
        return LastModified;
    }

    public InputStream open() throws Exception {
        return new ByteArrayInputStream(content.getBytes(encoding));
    }
}
