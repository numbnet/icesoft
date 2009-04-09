package com.icesoft.faces.async.server;

import com.icesoft.faces.net.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

public class InternalServerErrorResponse
extends ServerErrorResponse
implements Cloneable, Serializable {
    public InternalServerErrorResponse() {
        super(HttpResponse.INTERNAL_SERVER_ERROR, "InternalServerError");
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
