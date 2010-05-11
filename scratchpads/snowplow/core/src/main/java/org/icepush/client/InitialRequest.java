package org.icepush.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitialRequest
extends HttpRequest {
    private static final Logger LOGGER = Logger.getLogger(InitialRequest.class.getName());

    public InitialRequest(final String contextURI)
    throws URISyntaxException {
        super(
            Method.GET,                                                                                        // Method
            // throws URISyntaxException
            new URI(contextURI + "/in.jsp").normalize());                                                 // Request-URI
        // todo: Remove this logging.
        LOGGER.log(
            Level.INFO,
            "[Jack] " +
                "InitialRequest(" +
                    "contextURI: '" + contextURI + "')");
    }
}
