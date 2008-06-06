/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.  
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright (c) Ericsson AB, 2004-2007. All rights reserved.
 */

package com.ericsson.sip;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TimerService;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author Sreeram
 * @version
 */
public class SipCallSetupServlet extends HttpServlet {
    
	private Logger logger = Logger.getLogger("CallSetup");
	SipFactory sf = null;
	TimerService ts = null;
	ServletContext ctx = null;

	   
	   
    /** Processes requests for both HTTP <code>GET</code> 
     *  and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String callA = null;
        String callB = null;
        String[] contacts = request.getParameterValues("CONTACT");
        
        if ( contacts == null || contacts.length < 2 ) {
            processError(request, response);
            logger.log(Level.WARNING, "Less that two phones (users) were registered");
            return;
        }

        callA = contacts[0];
        callB = contacts[1];
    
        SipApplicationSession as = sf.createApplicationSession();

        Address to = sf.createAddress(callB);
        Address from = sf.createAddress(callA);
        
        SipServletRequest sipReq = sf.createRequest(as, "INVITE", from, to);

          logger.log(Level.FINE, "SipCallSetupServlet sipRequest = " + sipReq.toString());

        sipReq.setAttribute("CALL","INITIAL");
       
         // set servlet to invoke by reponse
         SipSession s = sipReq.getSession();
         s.setHandler("b2b");
       
         // lets send invite to B ...
         sipReq.send();

        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet SipCallSetupServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet SipCallSetupServlet at " + request.getContextPath () + "</h1>");
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
    
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {

      logger.log(Level.FINE, "Starting ListWebServlet.");

       ctx = config.getServletContext();
       try
       {
          sf = (SipFactory) ctx.getAttribute(SipServlet.SIP_FACTORY);
          ts = (TimerService) ctx.getAttribute(SipServlet.TIMER_SERVICE);

          logger.log(Level.FINE, "Special servlet calls if two registered.");

       }
       catch(Throwable t)
       {
          logger.log(Level.SEVERE,"Could not retreive the SipFactory", t);
       }

          logger.log(Level.FINE, "Started ListWebServlet.");

    }
    
    /**
     * This method processes any error that is thrown during the service method
     *  invocation
     * @param request
     * @param response
     */
    protected void processError(HttpServletRequest request,
                                   HttpServletResponse response)
                                       throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet SipCallSetupServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("Error thrown while setting up the call");
        out.println("<br> Possible causes :");
        out.println("<ul> <li>No Phones were registered</li>");
        out.println(" <li>Only one Phone was registered</li></ul>");        
        out.println("</body>");
        out.println("</html>");
        
        out.close();
        
    }
}

