package com.icesoft.faces.async.server;

import com.icesoft.faces.net.http.HttpResponse;

import java.io.Serializable;

public class NotImplementedResponse
extends ServerErrorResponse
implements Cloneable, Serializable {
    public NotImplementedResponse() {
        super(HttpResponse.NOT_IMPLEMENTED, "Not Implemented");
    }
}
