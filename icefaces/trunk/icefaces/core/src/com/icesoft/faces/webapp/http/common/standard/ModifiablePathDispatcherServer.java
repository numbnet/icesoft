package com.icesoft.faces.webapp.http.common.standard;

import com.icesoft.faces.webapp.http.common.Server;

public class ModifiablePathDispatcherServer extends PathDispatcherServer {

    protected synchronized Server findServer(String path) {
        return super.findServer(path);
    }

    public synchronized void dispatchOn(String pathExpression, Server toServer) {
        super.dispatchOn(pathExpression, toServer);
    }

    public synchronized void stopDispatchFor(Server toServer) {
        int position = servers.indexOf(toServer);
        servers.remove(position);
        matchers.remove(position);
    }
}
