package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.http.AbstractServer;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.standard.OKHandler;
import org.icefaces.util.EnvUtils;

public class DisposeWindowScope extends AbstractServer {
    private final WindowScopeManager windowScopeManager;
    private SessionViewManager sessionViewManager;

    public DisposeWindowScope(WindowScopeManager windowScopeManager, SessionViewManager sessionViewManager) {
        this.windowScopeManager = windowScopeManager;
        this.sessionViewManager = sessionViewManager;
    }

    public void service(Request request) throws Exception {
        String windowID = request.getParameter("ice.window");
        windowScopeManager.disposeWindow(windowID);

        if (EnvUtils.isICEpushPresent()) {
            String[] viewIDs = request.getParameterAsStrings("ice.view");
            for (int i = 0; i < viewIDs.length; i++) {
                sessionViewManager.removeView(viewIDs[i]);
            }
        }

        request.respondWith(OKHandler.HANDLER);
    }
}
