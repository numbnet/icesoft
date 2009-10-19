package org.icepush.sample.basic;

import org.icepush.PushContext;
import org.icepush.PushContextLocator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;

public class PageNotifier extends HttpServlet {
    private Timer timer;

    public void init(ServletConfig servletConfig) throws ServletException {
        timer = new Timer(true);
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        final PushContext pc = PushContextLocator.getInstance(httpServletRequest);
        final String pushId = pc.createPushId(httpServletRequest.getSession(true).getId());

        renderHTMLPage(pushId, httpServletResponse.getWriter());
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pc.notify(pushId);
            }
        }, 0, 5000);
    }

    private void renderHTMLPage(String pushID, Writer w) throws IOException {
        w.write("<html><head><title>");
        w.write(pushID);
        w.write("</title>");
        w.write("<script type=\"text/javascript\" src=\"icepush.js\"></script>");
        w.write("</head><body>");
        w.write("<script type=\"text/javascript\">");
        w.write("ice.onLoad(function() {");
        w.write("ice.Application({session: 'aaa', view: ");
        w.write(pushID);
        w.write(", connection: {heartbeat: {}, context: {current: '/icepush-basic/',async: '/icepush-basic/'}}});");
        w.write("});</script>");
        w.write("<script type=\"text/javascript\">");
        w.write("ice.onLoad(function() {");
        w.write("ice.onNotification(function(pushIds) { ice.info(ice.logger, pushIds); });");
        w.write("});</script>");
        w.write("</body></html>");
    }

    public void destroy() {
        timer.cancel();
    }
}
