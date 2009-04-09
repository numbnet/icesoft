package com.icesoft.faces.component.outputchart;

import com.icesoft.faces.context.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class ChartResource implements Resource {
	private static long prevDigest = 0;
    ByteArrayOutputStream bos;

    public ChartResource(ByteArrayOutputStream bos) {
        this.bos = bos;
    }

    public String calculateDigest() {
        long digest = System.currentTimeMillis();
        synchronized (getClass()) { // ICE-3052
            if (digest <= prevDigest) {
                digest = prevDigest + 1;
            }
            prevDigest = digest;
        }
        return String.valueOf(digest);
    }

    public Date lastModified() {
        return new Date();
    }

    public InputStream open() throws IOException {
        return new ByteArrayInputStream(bos.toByteArray());
    }

    public void withOptions(Resource.Options options) throws IOException {
        //no options
    }
}
