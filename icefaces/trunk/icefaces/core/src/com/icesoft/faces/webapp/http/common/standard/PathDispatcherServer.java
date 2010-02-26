package com.icesoft.faces.webapp.http.common.standard;

import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class PathDispatcherServer implements Server {
    protected List matchers = new ArrayList();
    protected List servers = new ArrayList();

    public void service(Request request) throws Exception {
        String path = request.getURI().getPath();
        Server server = findServer(path);

        if (server == null) {
            request.respondWith(new NotFoundHandler("Could not find resource at " + path));
        } else {
            server.service(request);
        }
    }

    protected Server findServer(String path) {
        ListIterator i = new ArrayList(matchers).listIterator();
        while (i.hasNext()) {
            int index = i.nextIndex();
            Pattern pattern = (Pattern) i.next();
            if (pattern.matcher(path).find()) {
                return (Server) servers.get(index);
            }
        }

        return null;
    }

    public void dispatchOn(String pathExpression, final Server toServer) {
        matchers.add(Pattern.compile(pathExpression));
        servers.add(toServer);
    }

    public void shutdown() {
        Iterator i = servers.iterator();
        while (i.hasNext()) {
            Server server = (Server) i.next();
            server.shutdown();
        }
    }
}
