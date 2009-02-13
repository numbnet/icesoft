package com.icesoft.faces.async.server;

import com.icesoft.faces.net.http.HttpResponse;

import java.io.Serializable;

public class HttpVersionNotSupportedResponse
extends ServerErrorResponse
implements Cloneable, Serializable {
    public HttpVersionNotSupportedResponse() {
        super(
            HttpResponse.HTTP_VERSION_NOT_SUPPORTED,
            "HTTP Version Not Suipported");
    }
}
