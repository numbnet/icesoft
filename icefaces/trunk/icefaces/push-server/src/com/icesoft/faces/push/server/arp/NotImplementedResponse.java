package com.icesoft.faces.push.server.arp;

import java.io.Serializable;

public class NotImplementedResponse
extends ServerErrorResponse
implements Cloneable, Serializable {
    public NotImplementedResponse() {
        super(HttpResponse.NOT_IMPLEMENTED, "Not Implemented");
    }
}
