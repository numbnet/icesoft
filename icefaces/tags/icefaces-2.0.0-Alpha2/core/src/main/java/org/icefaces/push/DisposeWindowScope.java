package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.http.AbstractServer;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.standard.OKHandler;

public class DisposeWindowScope extends AbstractServer {
    private final WindowScopeManager windowScopeManager;

    public DisposeWindowScope(WindowScopeManager windowScopeManager) {
        this.windowScopeManager = windowScopeManager;
    }

    public void service(Request request) throws Exception {
        String windowID = request.getParameter("ice.window");
        windowScopeManager.disposeWindow(windowID);
        request.respondWith(OKHandler.HANDLER);
    }
}
