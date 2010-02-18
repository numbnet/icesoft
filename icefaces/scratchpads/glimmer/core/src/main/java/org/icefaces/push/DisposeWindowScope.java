package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.http.AbstractServer;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.standard.OKHandler;

public class DisposeWindowScope extends AbstractServer {
    private final WindowScopeManager windowScopeManager;
    private ViewNotificationManager viewNotificationManager;

    public DisposeWindowScope(WindowScopeManager windowScopeManager, ViewNotificationManager viewNotificationManager) {
        this.windowScopeManager = windowScopeManager;
        this.viewNotificationManager = viewNotificationManager;
    }

    public void service(Request request) throws Exception {
        String windowID = request.getParameter("ice.window");
        windowScopeManager.disposeWindow(windowID);

        String[] viewIDs = request.getParameterAsStrings("ice.view");
        for (int i = 0; i < viewIDs.length; i++) {
            viewNotificationManager.disposeViewIdentifier(viewIDs[i]);
        }

        request.respondWith(OKHandler.HANDLER);
    }
}
