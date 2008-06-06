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

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerService;

import java.util.logging.Logger;
import java.util.logging.Level;

public class B2BCallServlet extends SipServlet {
    private Logger logger = Logger.getLogger("CallSetup");
    SipFactory                              sf               = null;
    TimerService                            ts;
    ServletContext                          ctx              = null;
    
    
   /*
    * (non-Javadoc)
    *
    * @see javax.servlet.sip.SipServlet#doResponse(javax.servlet.sip.SipServletResponse)
    */
    protected void doResponse(SipServletResponse resp) throws ServletException, IOException {
        SipApplicationSession sas = resp.getApplicationSession(true);
        SipServletRequest origReq = resp.getRequest();
        
        SipServletRequest proxyRequest = (SipServletRequest) origReq.getAttribute("REQUEST");
        if( proxyRequest != null ) { // ESTABLISHED call proxied by do Request
            SipServletResponse proxyResp = proxyRequest.createResponse(resp.getStatus(),resp.getReasonPhrase());
            if( resp.getContentLength() > 0 ) {
                proxyResp.setContent(resp.getContent(),resp.getContentType());
            }
            proxyResp.send();
            return;
        }
        
        //IF SDP
        //TODO maybe only on 1xx?
        String alreadySent = (String) origReq.getAttribute("SENT_INVITE");
        logger.log(Level.FINE, "AlreadySent = "+alreadySent);
        if( alreadySent == null && resp.getContentLength() > 0 && resp.getContentType().equalsIgnoreCase("application/sdp")) {
            String responseFrom = (String) origReq.getAttribute("CALL");        
            if("INITIAL".equals(responseFrom)) {
                //Take the SDP and send to A
                SipServletRequest newReq = sf.createRequest(sas,"INVITE",origReq.getTo(),origReq.getFrom());
                newReq.setContent(resp.getContent(),resp.getContentType());
                SipSession ssA = newReq.getSession(true);
                SipSession ssB = resp.getSession(true);
                ssA.setAttribute("OTHER_SESSION",ssB);
                ssB.setAttribute("OTHER_SESSION",ssA);
                //Test
                ssA.setHandler("b2b");
                ssB.setHandler("b2b");
                origReq.setAttribute("SENT_INVITE","SENT_INVITE");
                newReq.send(); //Send to A
            } else { // From A store SDP untill 200 from Both
                SipSession ssB = (SipSession) resp.getSession().getAttribute("OTHER_SESSION");
                ssB.setAttribute("SDP",resp.getContent());
                logger.log(Level.FINE, "Setting SDP in session "+ssB.toString()+"content : "+ resp.getContent() );
            }
        } else {
            System.out.println("Not a interesting response"+alreadySent);
            log("Not a interesting response"+alreadySent);
            return;
        }
        // Count so that both sides sent 200.
        
        if( resp.getStatus() == 200 ) {
            //sendAck(resp);            
            SipServletResponse first = (SipServletResponse) sas.getAttribute("GOT_FIRST_200");
            if( first == null ) { // This is the first 200
                sas.setAttribute("GOT_FIRST_200",resp);
            } 
            else { //This is the second 200 sen both ACK
                sendAck(resp);
                sendAck(first);
            }
        }
    }
    
    private void sendAck( SipServletResponse resp ) throws IOException {
        SipServletRequest ack = resp.createAck();
        //Check if pending SDP to include in ACK
        Object content = resp.getSession().getAttribute("SDP");
        logger.log(Level.FINE, "Getting SDP in session "+resp.getSession().toString()+"content : "+ content );
        if( content != null ) {
            logger.log(Level.FINE, "Found SDP!");
            ack.setContent(content,"application/sdp");
        }
        ack.send();
    }
    
    protected void doRequest(SipServletRequest req) throws ServletException, IOException {
        SipSession ss = (SipSession) req.getSession(true).getAttribute("OTHER_SESSION");
        if( ss != null ) { // We have sent INVITE to both sides no in proxy b2b mode
            SipServletRequest proxyReq = ss.createRequest(req.getMethod());
            if( req.getContentLength() > 0 ) {
                proxyReq.setContent(req.getContent(),req.getContentType());
            }
            //Setup for responses
            proxyReq.setAttribute("REQUEST",req);
            proxyReq.send();
        }
    }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ctx = config.getServletContext();
        logger.log(Level.INFO, "B2B Init");
        try {
            sf = (SipFactory) ctx.getAttribute(SipServlet.SIP_FACTORY);
            ts = (TimerService) ctx.getAttribute(SipServlet.TIMER_SERVICE);
        } catch(Throwable t) {
            logger.log(Level.SEVERE, "Could not retreive the SipFactory", t);
        }
    }
}
