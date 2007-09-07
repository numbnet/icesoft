package com.icesoft.faces.context;

import java.io.InputStream;
import java.util.Date;

public class JarResource implements Resource {
    private static Date StartTime = new Date(System.currentTimeMillis());
    private String path;

    public JarResource(String path) {
        this.path = path;
    }

    public String calculateDigest() {
        return path;
    }

    public Date lastModified() {
        return StartTime;
    }

    public InputStream open() throws Exception {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }
}
