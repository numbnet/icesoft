package org.icepush.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ICEpushServlet extends HttpServlet {
    private PseudoServlet mainServlet;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        mainServlet = new MainServlet(servletConfig.getServletContext());
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            mainServlet.service(request, response);
        } catch (ServletException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        mainServlet.shutdown();
    }
}
