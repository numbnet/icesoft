package org.icepush.sample.basic;

import org.icepush.PushContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

public class PageNotifier extends HttpServlet {
    private Timer timer;

    public void init(ServletConfig servletConfig) throws ServletException {
        timer = new Timer(true);
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        final PushContext pc = PushContext.getInstance(httpServletRequest);
        final String browserId = httpServletRequest.getSession(true).getId();
        final String idA = pc.createPushId();
        final String idB = pc.createPushId();

        PrintWriter w = httpServletResponse.getWriter();
        w.write("<html><head><title>");
        w.write(idA + "; " + idB);
        w.write("</title>");
        w.write("<script type=\"text/javascript\" src=\"icepush.js\"></script>");
        w.write("</head><body>");

        w.write("<script type=\"text/javascript\">");
        w.write("ice.onLoad(function() {");
        w.write("ice.Application({session: '" + browserId + "', view: ");
        w.write(idA);
        w.write(", connection: {heartbeat: {}, context: {current: '/icepush-basic/',async: '/icepush-basic/'}}});");
        w.write("});</script>");

        w.write("<script type=\"text/javascript\">");
        w.write("ice.onLoad(function() {");
        w.write("ice.Application({session: '" + browserId + "', view: ");
        w.write(idB);
        w.write(", connection: {heartbeat: {}, context: {current: '/icepush-basic/',async: '/icepush-basic/'}}});");
        w.write("});</script>");

        w.write("<script type=\"text/javascript\">");
        w.write("ice.push.register([" + idA + ", " + idB + "], function(pushIds) { ice.info(ice.logger, pushIds); });");
        w.write("</script>");
        w.write("</body></html>");

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pc.notify(idA);
            }
        }, 0, 5000);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pc.notify(idB);
            }
        }, 1000, 5000);
    }

    public void destroy() {
        timer.cancel();
    }
}
