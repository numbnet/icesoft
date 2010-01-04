package org.icepush.servlet;

import org.icepush.BlockingConnectionServer;
import org.icepush.Configuration;
import org.icepush.PushContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Observable;
import java.util.Timer;

public class BrowserBoundServlet extends PathDispatcher {
    private PushContext pushContext;

    public BrowserBoundServlet(PushContext pushContext, Observable outboundNotifier, Observable inboundNotifier, final Timer monitorRunner, Configuration configuration) {
        this.pushContext = pushContext;

        dispatchOn(".*listen\\.icepush", new EnvironmentAdaptingServlet(new BlockingConnectionServer(outboundNotifier, inboundNotifier, monitorRunner, configuration), configuration));
        dispatchOn(".*create-push-id\\.icepush", new CreatePushID());
        dispatchOn(".*notify\\.icepush", new NotifyPushID());
        dispatchOn(".*add-group-member\\.icepush", new AddGroupMember());
        dispatchOn(".*remove-group-member\\.icepush", new RemoveGroupMember());
    }

    private class CreatePushID extends AbstractPseudoServlet {
        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().write(pushContext.createPushId(request, response));
        }
    }

    private class NotifyPushID extends AbstractPseudoServlet {
        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String[] ids = request.getParameterValues("id");
            for (String id : ids) {
                pushContext.push(id);
            }
        }
    }

    private class AddGroupMember extends AbstractPseudoServlet {
        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String group = request.getParameter("group");
            String pushID = request.getParameter("id");
            pushContext.addGroupMember(group, pushID);
        }
    }

    private class RemoveGroupMember extends AbstractPseudoServlet {
        public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String group = request.getParameter("group");
            String pushID = request.getParameter("id");
            pushContext.removeGroupMember(group, pushID);
        }
    }
}
