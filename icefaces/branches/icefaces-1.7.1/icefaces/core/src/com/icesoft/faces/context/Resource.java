package com.icesoft.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Resource represents a handle to a read-only stream of bytes.
 */
public interface Resource {

    /**
     * Open reading stream.
     *
     * @return the stream
     */
    InputStream open() throws IOException;

    /**
     * Return timestamp when resource was last updated or created.
     *
     * @return the timestamp
     */
    Date lastModified();

    /**
     * Calculate a digest that uniquely identifies the content of the resource.
     *
     * @return the digest
     */
    String calculateDigest();
}
