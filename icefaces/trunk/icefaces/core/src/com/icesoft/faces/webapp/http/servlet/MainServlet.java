package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.FileLocator;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.core.DisposeBeans;
import com.icesoft.faces.webapp.http.core.ResourceServer;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesCommonlet;
import com.icesoft.util.IdGenerator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class MainServlet extends HttpServlet {
    static {
        final String headless = "java.awt.headless";
        if (null == System.getProperty(headless)) {
            System.setProperty(headless, "true");
        }
    }

    private PathDispatcher dispatcher = new PathDispatcher();
    private String contextPath;
    private ServletContext context;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.context = servletConfig.getServletContext();
        try {
            final Configuration configuration = new ServletContextConfiguration("com.icesoft.faces", context);
            final IdGenerator idGenerator = new IdGenerator(context.getResource("/WEB-INF/web.xml").getPath());
            final MimeTypeMatcher mimeTypeMatcher = new MimeTypeMatcher() {
                public String mimeTypeFor(String extension) {
                    return context.getMimeType(extension);
                }
            };
            final FileLocator localFileLocator = new FileLocator() {
                public File locate(String path) {
                    URI contextURI = URI.create(contextPath);
                    URI pathURI = URI.create(path);
                    String result = contextURI.relativize(pathURI).getPath();
                    String fileLocation = context.getRealPath(result);
                    return new File(fileLocation);
                }
            };

            PseudoServlet resourceServer = new BasicAdaptingServlet(new ResourceServer(configuration, mimeTypeMatcher, localFileLocator));
            PseudoServlet sessionServer = new SessionDispatcher() {
                protected PseudoServlet newServlet(HttpSession session, Monitor sessionMonitor) {
                    return new MainSessionBoundServlet(session, sessionMonitor, idGenerator, mimeTypeMatcher, configuration);
                }
            };

            dispatcher.dispatchOn(".*(\\.iface$|\\.jsf|\\.faces$|\\.jsp$|\\.jspx$|\\.html$|\\.xhtml$|\\.seam$|uploadHtml$|block\\/|/spring/)", sessionServer);
            dispatcher.dispatchOn(".*", resourceServer);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //set flag to indicate an ICEfaces request so that delegateNonIface
        //will detect this and execute D2DViewHandler for it
        request.setAttribute(PersistentFacesCommonlet.SERVLET_KEY,
                PersistentFacesCommonlet.PERSISTENT);
        contextPath = request.getContextPath();
        try {
            dispatcher.service(request, response);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void destroy() {
        DisposeBeans.in(context);
        dispatcher.shutdown();
    }
}
