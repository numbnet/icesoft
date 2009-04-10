package com.icesoft.faces.push.server.arp;

import java.io.Serializable;

public class HttpVersionNotSupportedResponse
extends ServerErrorResponse
implements Cloneable, Serializable {
    public HttpVersionNotSupportedResponse() {
        super(
            HttpResponse.HTTP_VERSION_NOT_SUPPORTED,
            "HTTP Version Not Supported");
    }
}
