package org.icepush.servlet;

import org.icepush.BlockingConnectionServer;
import org.icepush.Configuration;
import org.icepush.PushContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Observable;
import java.util.Timer;

public class SessionBoundServlet extends PathDispatcher {

    public SessionBoundServlet(HttpSession session, final Timer monitorRunner, Configuration configuration) {
        Observable notifier = new Observable() {
            public synchronized void notifyObservers(Object o) {
                setChanged();
                super.notifyObservers(o);
                clearChanged();
            }
        };
        PushContext pushContext = new PushContext(notifier);
        session.setAttribute(PushContext.class.getName(), pushContext);

        dispatchOn(".*listen\\.icepush$", new EnvironmentAdaptingServlet(new BlockingConnectionServer(notifier, monitorRunner, configuration), configuration, session.getServletContext()));
        dispatchOn(".*create-push-id\\.icepush$", new CreatePushID(pushContext));
        dispatchOn(".*notify\\.icepush$", new NotifyPushID(pushContext));
    }

    private static class CreatePushID implements PseudoServlet {
        private final PushContext pushContext;

        public CreatePushID(PushContext pushContext) {
            this.pushContext = pushContext;
        }

        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().write(pushContext.createPushId());
            response.getWriter().write("\n\n");
        }

        public void shutdown() {
        }
    }

    private static class NotifyPushID implements PseudoServlet {
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

        public void shutdown() {
        }
    }
}
