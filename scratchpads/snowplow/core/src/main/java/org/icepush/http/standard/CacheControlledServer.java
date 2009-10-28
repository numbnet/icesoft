package org.icepush.http.standard;

import org.icepush.http.Request;
import org.icepush.http.Response;
import org.icepush.http.ResponseHandler;
import org.icepush.http.Server;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


public class CacheControlledServer implements Server {
    private static final Date ExpirationDate = new Date(System.currentTimeMillis() + 2629743830l);
    private static final Collection cache = new HashSet();
    private static final Date StartupTime = new Date();
    private Server server;

    public CacheControlledServer(Server server) {
        this.server = server;
    }

    public void service(Request request) throws Exception {
        if (cache.contains(request.getHeader("If-None-Match"))) {
            request.respondWith(new NotModifiedHandler(ExpirationDate));
        } else {
            try {
                Date modifiedSince = request.getHeaderAsDate("If-Modified-Since");
                if (StartupTime.getTime() - modifiedSince.getTime() > 1000) {
                    server.service(new EnhancedRequest(request));
                } else {
                    request.respondWith(new NotModifiedHandler(ExpirationDate));
                }
            } catch (Exception e) {
                server.service(new EnhancedRequest(request));
            }
        }
    }

    public void shutdown() {
        cache.clear();
    }

    private class EnhancedRequest extends RequestProxy {

        public EnhancedRequest(Request request) {
            super(request);
        }

        public void respondWith(final ResponseHandler handler) throws Exception {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    String eTag = Integer.toHexString(request.getURI().hashCode());
                    cache.add(eTag);
                    response.setHeader("ETag", eTag);
                    //tell to IE to cache these resources
                    //see: http://mir.aculo.us/articles/2005/08/28/internet-explorer-and-ajax-image-caching-woes
                    //see: http://www.bazon.net/mishoo/articles.epl?art_id=958
                    //see: http://support.microsoft.com/default.aspx?scid=kb;en-us;319546
                    response.setHeader("Cache-Control", new String[]{"private", "max-age=2629743"});
                    response.setHeader("Last-Modified", StartupTime);
                    handler.respond(response);
                }
            });
        }
    }

    private static class NotModifiedHandler implements ResponseHandler {
        private Date expirationDate;

        public NotModifiedHandler(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        public void respond(Response response) throws Exception {
            response.setStatus(304);
            response.setHeader("Date", new Date());
            response.setHeader("Expires", expirationDate);
            response.setHeader("Content-Length", 0);
        }
    }
}
