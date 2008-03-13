
package com.icesoft.faces.webapp.http.servlet;


import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.icesoft.faces.webapp.http.core.SendUpdatedViews;
import com.icesoft.faces.webapp.http.core.ViewQueue;


public class TomcatPushServlet
    extends HttpServlet implements CometProcessor {

    private static Log log = LogFactory.getLog(TomcatPushServlet.class);

    public void init() throws ServletException {
    }

    public void destroy() {
    }

    /**
     * Process the given Comet event.
     * 
     * @param event The Comet event that will be processed
     * @throws IOException
     * @throws ServletException
     */
    public void event(CometEvent event)
        throws IOException, ServletException {

        HttpServletRequest request = event.getHttpServletRequest();
        HttpServletResponse response = event.getHttpServletResponse();
        
        if (event.getEventType() == CometEvent.EventType.BEGIN) {
            begin(event, request, response);
        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            error(event, request, response);
        } else if (event.getEventType() == CometEvent.EventType.END) {
            end(event, request, response);
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            read(event, request, response);
        }
    }

    protected void begin(CometEvent event, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        //We need access to the CometEvent, but this is not passed down through
        //our dispatcher chain, so we obtain the queues directly here
        MainSessionBoundServlet mainBound = (MainSessionBoundServlet) 
                SessionDispatcher.getSingletonSessionServlet(request.getSession());
        ViewQueue allUpdatedViews = mainBound.getAllUpdatedViews();
        Collection synchronouslyUpdatedViews = 
                mainBound.getSynchronouslyUpdatedViews();
        String sessionID = mainBound.getSessionID();
        allUpdatedViews.onPut( new EventResponder( event, sessionID,
                synchronouslyUpdatedViews, allUpdatedViews) );
    }
    
    protected void end(CometEvent event, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        event.close();
    }
    
    protected void error(CometEvent event, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    }
    
    protected void read(CometEvent event, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
            InputStream is = request.getInputStream();
            byte[] buf = new byte[512];
            do {
                int n = is.read(buf); //can throw an IOException
                if (n > 0) {
                    if (log.isDebugEnabled()) {
                        log.debug( "Read " + n + " bytes: " 
                                + new String(buf, 0, n) + " for session: " 
                                + request.getSession(true).getId() );
                    }
                } else if (n < 0) {
                    error(event, request, response);
                    return;
                }
            } while (is.available() > 0);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // Not used by Tomcat6
        throw new ServletException("service() not supported by TomcatPushServlet. Configure the connector, replacing protocol=\"HTTP/1.1\" with protocol=\"org.apache.coyote.http11.Http11NioProtocol\"");
    }
    

}


class EventResponder implements Runnable {
    ViewQueue allUpdatedViews;
    Collection synchronouslyUpdatedViews;
    CometEvent event;
    Writer writer;
    String sessionID;

    public EventResponder(CometEvent event, String sessionID,
            Collection synchronouslyUpdatedViews, 
            ViewQueue allUpdatedViews) {
        this.sessionID = sessionID;
        this.event = event;
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
        this.allUpdatedViews = allUpdatedViews;
        try {
            HttpServletResponse response =  event.getHttpServletResponse();
            writer = response.getWriter();
            response.setContentType("text/xml;charset=UTF-8"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()  {
        try {
            allUpdatedViews.removeAll(synchronouslyUpdatedViews);
            synchronouslyUpdatedViews.clear();
            Set viewIdentifiers = new HashSet(allUpdatedViews);
            if (!viewIdentifiers.isEmpty()) {
                Iterator identifiers = viewIdentifiers.iterator();
                writer.write("<updated-views>");
                while (identifiers.hasNext()) {
                    Object identifier = identifiers.next();
                    writer.write(sessionID + ":" + identifier);
                    writer.write(' ');
                }
                writer.write("</updated-views>");
                event.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
}
