package org.icepush.servlet;

import org.icepush.BlockingConnectionServer;
import org.icepush.Configuration;
import org.icepush.PushContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Observable;
import java.util.Timer;

public class BrowserBoundServlet extends PathDispatcher {

    public BrowserBoundServlet(PushContext pushContext, Observable outboundNotifier, Observable inboundNotifier, final Timer monitorRunner, Configuration configuration) {
        dispatchOn(".*listen\\.icepush", new EnvironmentAdaptingServlet(new BlockingConnectionServer(outboundNotifier, inboundNotifier, monitorRunner, configuration), configuration));
        dispatchOn(".*create-push-id\\.icepush", new CreatePushID(pushContext));
        dispatchOn(".*notify\\.icepush", new NotifyPushID(pushContext));
    }

    private static class CreatePushID extends AbstractPseudoServlet {
        private final PushContext pushContext;

        public CreatePushID(PushContext pushContext) {
            this.pushContext = pushContext;
        }

        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().write(pushContext.createPushId(request, response));
            response.getWriter().write("\n\n");
        }
    }

    private static class NotifyPushID extends AbstractPseudoServlet {
        private final PushContext pushContext;

        public NotifyPushID(PushContext pushContext) {
            this.pushContext = pushContext;
        }

        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String[] ids = request.getParameterValues("id");
            for (int i = 0; i < ids.length; i++) {
                pushContext.push(ids[i]);
            }
        }
    }
}
