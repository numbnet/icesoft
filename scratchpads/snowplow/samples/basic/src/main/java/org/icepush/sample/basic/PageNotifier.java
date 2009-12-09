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
    private PushContext pushContext;

    public void init(ServletConfig servletConfig) throws ServletException {
        timer = new Timer(true);
        pushContext = PushContext.getInstance(servletConfig.getServletContext());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String idA = pushContext.createPushId(request, response);
        final String idB = pushContext.createPushId(request, response);

        PrintWriter w = response.getWriter();
        w.write("<html><head><title>");
        w.write(idA + "; " + idB);
        w.write("</title>");
        w.write("<script type=\"text/javascript\" src=\"code.icepush\"></script>");
        w.write("</head><body>");

        w.write("<script type=\"text/javascript\">");
        w.write("ice.push.register(['" + idA + "', '" + idB + "'], function(pushIds) { ice.info(ice.logger, ice.push.getCurrentNotifications()); });");
        w.write("</script>");
        w.write("</body></html>");
        response.setContentType("text/html");

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pushContext.push(idA);
            }
        }, 0, 5000);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pushContext.push(idB);
            }
        }, 1000, 5000);
    }

    public void destroy() {
        timer.cancel();
    }
}
