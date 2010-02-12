package org.icepush;

import org.icepush.http.Request;
import org.icepush.http.Response;
import org.icepush.http.ResponseHandler;
import org.icepush.http.Server;

public class ConfigurationServer implements Server, ResponseHandler {
    private String configCode;

    public ConfigurationServer(Configuration configuration) {
        String uriPrefix = configuration.getAttribute("uriPrefix", "");
        String uriSuffix = configuration.getAttribute("uriSuffix", "");
        configCode =
                "ice.push.configuration.uriSuffix='" + uriSuffix + "';" +
                        "ice.push.configuration.uriPrefix='" + uriPrefix + "';";
    }

    public void service(Request request) throws Exception {
        request.respondWith(this);
    }

    public void respond(Response response) throws Exception {
        response.setHeader("Content-Type", "text/javascript");
        response.writeBody().write(configCode.getBytes("UTF-8"));
    }

    public void shutdown() {
    }
}