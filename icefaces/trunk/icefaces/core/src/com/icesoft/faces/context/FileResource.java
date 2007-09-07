package com.icesoft.faces.context;

import java.io.File;
import java.io.FileInputStream;
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

    public InputStream open() throws Exception {
        return new FileInputStream(file);
    }
}
