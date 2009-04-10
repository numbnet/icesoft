package com.icesoft.faces.push.server.arp;

import com.icesoft.faces.push.server.ProductInfo;
import com.icesoft.net.HttpDate;

import java.io.Serializable;
import java.util.Date;

public abstract class ServerErrorResponse
extends HttpResponse
implements Cloneable, Serializable {
    protected ServerErrorResponse(
        final int statusCode, final String reasonPhrase) {

        super(HttpResponse.HTTP_11, statusCode, reasonPhrase);
        // response header fields
        putHeader(
            HttpResponse.SERVER,
            (
                ProductInfo.PRODUCT + " " +
                ProductInfo.PRIMARY + "." +
                ProductInfo.SECONDARY + "." +
                ProductInfo.TERTIARY + " " +
                ProductInfo.RELEASE_TYPE
            ).trim(),
            true);
        // general header fields
        putHeader(HttpResponse.CONNECTION, "close", true);
        putHeader(
            HttpResponse.DATE,
            HttpDate.RFC_1123_DATE_FORMAT.format(new Date()),
            true);
        // entity header fields
        /*
         * Note: Firefox and Safari both seem to need the Content-Length
         *       header even though there is no Entity-Body and
         *       "Connection: close" is specified.
         */
        putHeader(HttpResponse.EntityBody.CONTENT_LENGTH, 0, true);
        // extension header fields
        putHeader(HttpResponse.PROXY_CONNECTION, "close", true);
    }

    public void setEntityBody(final EntityBody entityBody) {
        super.setEntityBody(entityBody);
        putHeader(
            HttpResponse.EntityBody.CONTENT_LENGTH,
            entityBody.getBytes().length,
            true);
    }
}
