
package com.icesoft.faces.webapp.http.servlet;

import java.lang.reflect.Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import com.sun.enterprise.web.connector.grizzly.comet.CometEngine;
import com.sun.enterprise.web.connector.grizzly.comet.CometContext;
import com.sun.enterprise.web.connector.grizzly.comet.CometEvent;
import com.sun.enterprise.web.connector.grizzly.comet.CometHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.icesoft.faces.webapp.http.core.SendUpdatedViews;
import com.icesoft.faces.webapp.http.core.ViewQueue;


public class GrizzlyPushServlet
    extends HttpServlet  {

    private static Log log = LogFactory.getLog(GrizzlyPushServlet.class);

    private String contextPath;

    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
System.out.println("Latest GrizzlyPushServlet");
        Method getContextPathMethod;
        ServletContext servletContext = config.getServletContext();
        try {
            getContextPathMethod =
                ServletContext.class
                .getMethod("getContextPath", new Class[] {});
            contextPath =  getContextPathMethod
                    .invoke(servletContext, (Object[]) null) +
                    "/block/receive-updated-views";
        } catch (Exception e) {
            throw new ServletException(
                    "ServletContext.getContextPath not defined", e );
        }
        CometEngine cometEngine = CometEngine.getEngine();
        CometContext context = cometEngine.register(contextPath);    
    }

    public void destroy() {
    }

    protected void begin(CometEvent event, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    }
        
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        //We need access to the CometEvent, but this is not passed down through
        //our dispatcher chain, so we obtain the queues directly here
        MainSessionBoundServlet mainBound = (MainSessionBoundServlet) 
                SessionDispatcher.getSingletonSessionServlet(request.getSession());
        ViewQueue allUpdatedViews = mainBound.getAllUpdatedViews();
        Collection synchronouslyUpdatedViews = 
                mainBound.getSynchronouslyUpdatedViews();

        if ( GrizzlyEventResponder.writeViews(
                allUpdatedViews, synchronouslyUpdatedViews,
                response.getWriter()) )  {
                return;
        }

        GrizzlyEventResponder grizzlyResponder = new GrizzlyEventResponder(
                synchronouslyUpdatedViews, allUpdatedViews);
        CometEngine cometEngine = CometEngine.getEngine();
        CometContext cometContext = cometEngine.getCometContext(contextPath);
        grizzlyResponder.attach(response);
        cometContext.addCometHandler(grizzlyResponder);
    }
    

}


class GrizzlyEventResponder implements Runnable,
        CometHandler<HttpServletResponse> {
    ViewQueue allUpdatedViews;
    Collection synchronouslyUpdatedViews;
    HttpServletResponse response;
    boolean valid = true;
    CometContext cometContext;
    Writer writer;

    public GrizzlyEventResponder( 
            Collection synchronouslyUpdatedViews, 
            ViewQueue allUpdatedViews) {
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
        this.allUpdatedViews = allUpdatedViews;
    }

    /**
     * Attach an intance of E to this class.
     */
    public void attach(HttpServletResponse httpResponse)  {
        response = httpResponse;
        try {
            writer = response.getWriter();
            response.setContentType("text/xml;charset=UTF-8");
        } catch (IOException e)  {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Receive <code>CometEvent</code> notification.
     */
    public void onEvent(CometEvent event) throws IOException  {
    }
    
    
    /**
     * Receive <code>CometEvent</code> notification when the underlying 
     * tcp communication is started by the client
     */
    public void onInitialize(CometEvent event) throws IOException {
        cometContext = event.getCometContext();  
        allUpdatedViews.onPut(this);
    }
    
    
    /**
     * Receive <code>CometEvent</code> notification when the underlying 
     * tcp communication is closed by the <code>CometHandler</code>
     */
    public void onTerminate(CometEvent event) throws IOException  {
System.out.println("onTerminate " + this);
        onInterrupt(event);
    }
    
    /**
     * Receive <code>CometEvent</code> notification when the underlying 
     * tcp communication is resumed by the Grizzly ARP.
     */
    public void onInterrupt(CometEvent event) throws IOException  {
        CometContext cometContext = event.getCometContext();  
        cometContext.removeCometHandler(this);
    }   

    public void run()  {
        try {
            synchronized(this) {
                if(!valid)  {
                     return;
                } else {
                    valid = false;
                }
            }
            writeViews(allUpdatedViews, synchronouslyUpdatedViews, writer);
            cometContext.resumeCometHandler(this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public static boolean writeViews(
            ViewQueue views, Collection synchronousViews, Writer writer)
            throws IOException {
        boolean result = false;
        views.removeAll(synchronousViews);
        synchronousViews.clear();
        Set viewIdentifiers = new HashSet(views);
        //potential race condition with views being updated before
        //being cleared here.  We need an atomic getAndClear()
        views.clear();
        if (!viewIdentifiers.isEmpty()) {
            result = true;
            Iterator identifiers = viewIdentifiers.iterator();
            writer.write("<updated-views>");
            while (identifiers.hasNext()) {
                Object identifier = identifiers.next();
                writer.write(identifier.toString());
                writer.write(' ');
            }
            writer.write("</updated-views>");
            writer.flush();
        }
        return result;
    }
    
}
