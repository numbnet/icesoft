package com.icesoft.faces.context;

import java.net.URI;
import java.util.HashMap;

public class ViewStatus {

    private HashMap viewRecords;
    private long viewsCreated = 0;
    private long viewsDisposed = 0;

    public ViewStatus() {
        viewRecords = new HashMap();
    }

    public void recordViewCreated(String viewIdentifier, URI uri) {
        viewRecords.put(viewIdentifier,uri.toString());
        viewsCreated++;
    }

    public void recordViewDisposed(String viewIdentifier) {
        viewRecords.remove(viewIdentifier);
        viewsDisposed++;
    }

    public HashMap getViewRecords() {
        return viewRecords;
    }

    public long getViewsCreated() {
        return viewsCreated;
    }

    public long getViewsDisposed() {
        return viewsDisposed;
    }

    public String getCurrentStatus(String sessionId){
        StringBuffer buff = new StringBuffer();
        buff.append("views for  ").append(sessionId).append("\n");
        buff.append("  created : ").append(viewsCreated).append("\n");
        buff.append("  disposed: ").append(viewsDisposed).append("\n");
        buff.append("  active  : ").append(viewRecords.size()).append("\n").append(viewRecords);
        return buff.toString();
    }

}
