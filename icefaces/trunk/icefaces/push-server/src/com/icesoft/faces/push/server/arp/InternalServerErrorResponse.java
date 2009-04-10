package com.icesoft.faces.push.server.arp;

import java.io.Serializable;

public class InternalServerErrorResponse
extends ServerErrorResponse
implements Cloneable, Serializable {
    public InternalServerErrorResponse() {
        super(HttpResponse.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    public InternalServerErrorResponse(final Throwable throwable) {
        this();
//        if (throwable != null) {
//            ByteArrayOutputStream _byteArrayOutputStream =
//                new ByteArrayOutputStream();
//            throwable.printStackTrace(new PrintStream(_byteArrayOutputStream));
//            setEntityBody(
//                new HttpResponse.EntityBody(
//                    _byteArrayOutputStream.toByteArray()));
//        }
    }
}
