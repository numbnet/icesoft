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
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;

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
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.URI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import javax.annotation.Resource;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.ericsson.sip.Registration;
import org.icefaces.x.core.push.SessionRenderer;

/**
 * @author lmcpepe
 * @created 3-Aug-2004
 */
@PersistenceContext(name = "persistence/LogicalName", unitName = "EricssonSipPU")
public class RegistrarServlet extends SipServlet{
    
    @PersistenceUnit(unitName = "EricssonSipPU")
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private Logger                          logger              = Logger
            .getLogger("CallSetup");
    private static final long               serialVersionUID = 3977861786890484016L;
    public static final String              REGISTRATION_MAP = "REGISTRATION_MAP";
    private Hashtable<SipURI, SipURI>       registrations    = new Hashtable<SipURI, SipURI>();
    private Hashtable<SipURI, ServletTimer> timers           = new Hashtable<SipURI, ServletTimer>();
    
    
    SipFactory                              sf               = null;
    
    TimerService                            ts;
    
    ServletContext                          ctx              = null;
    
    protected void doRegister(SipServletRequest request) throws ServletException, IOException {
        
        SipServletResponse response = request.createResponse(200);
        try {
            SipURI to = cleanURI((SipURI) request.getTo().getURI());
            ListIterator<Address> li = request.getAddressHeaders("Contact");
            while (li.hasNext()){
                Address na = li.next();
                SipURI contact = (SipURI) na.getURI();
                logger.log(Level.FINE, "Contact = " + contact);
                String expiresString = na.getParameter("expires");
                logger.log(Level.FINE, "Expires param = " + expiresString);
                if (expiresString == null) { // Check the Expires header
                    expiresString = request.getHeader("Expires");
                    logger.log(Level.FINE, "Expires header = " + expiresString);
                }
                if (expiresString == null) {
                    logger.log(Level.FINE, "Missing expires value in request!");
                    expiresString = "3600";
	                response.setExpires(3600);
                }
                long expires = Integer.parseInt(expiresString);
                //Map<SipURI, SipURI> reg = (Map) ctx.getAttribute(REGISTRATION_MAP);
                if (expires == 0) {
                    EntityManager em = emf.createEntityManager();
                    
                    try {
                        utx.begin();
                        Registration registration = new Registration();
                        registration.setId(to.toString());
                        
                        registration = em.merge(registration);
                        em.remove(registration);
                        utx.commit();
                        logger.log(Level.FINE, "Registration was successfully created.");
                    } catch (Exception ex) {
                        try {
                            logger.log(Level.FINE, ex.getLocalizedMessage());
                            utx.rollback();
                        } catch (Exception e) {
                            logger.log(Level.FINE, e.getLocalizedMessage());
                        }
                    }
                    em.close();
                    
                    
                    
                    // TODO FIXME Use EntityManager to remove the registration.
//               SipURI cTo = reg.remove(contact);
//
//               if (log.isInfoEnabled()) log.info("Removed = " + contact + " reg = " + cTo);
                    ServletTimer st = timers.remove(contact);
                    logger.log(Level.FINE, "Cancel timer = " + st);
                    if (st != null) {
                        st.cancel();
                    }
                } else {
                    logger.log(Level.INFO, "Adding = " + to + " reg = " + contact);
                    //reg.put(contact, to);
                    storeRegistration( to ,contact, expires);
                    if (ts != null) {
                        // Create a timer to expire after 1 sec
                        
                        // TODO FIXME send Entity Manager with Timer
//                  TimerContent tc = new TimerContent(contact, reg, "Registrar");
//                  if (log.isDebugEnabled()) log.debug("TimerContent = " + tc);
//                  ServletTimer st = ts.createTimer(request.getSession().getApplicationSession(),
//                        1000 * expires, false, tc);
                        
//                  if (log.isDebugEnabled()) log.debug("Storeing timer = " + st + " key = " + to);
//                  st = timers.put(contact, st);
                        // Cancel the old timer if there is one
//                  if (log.isDebugEnabled()) log.debug("Cancel old timer = " + st);
//                  if (st != null) st.cancel();
                    }
                    response.setHeader("Contact",na.toString());
                }
            }
            response.send();
            logger.log(Level.FINE, "Sent 200 response.");
        } catch(Exception e) {
            logger.log(Level.FINE, "Sent 500 response.", e);
            response.setStatus(500);
            response.send();
        }
        SipApplicationSession appsess = request.getApplicationSession(false);
        if (appsess != null) {
            appsess.invalidate();
        }
        //Push the update to the web page
        SessionRenderer.render("presence");
    }
    
    protected void doInvite(SipServletRequest request) throws ServletException, IOException {
        logger.log(Level.INFO, "doInvite()");
        doProxy(request);
    }
    
   /*
    * (non-Javadoc)
    *
    * @see javax.servlet.sip.SipServlet#doMessage(javax.servlet.sip.SipServletRequest)
    */
    protected void doMessage(SipServletRequest request) throws ServletException, IOException {
        logger.log(Level.FINE, "doMessage()");
        doProxy(request);
        
        SipApplicationSession appsess = request.getApplicationSession(false);
        if (appsess != null) {
            appsess.invalidate();
        }
    }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ctx = config.getServletContext();
        logger.log(Level.INFO, "Registrar Init");
        
        try {
//               Context ctx = (Context) new InitialContext().lookup("java:comp/env");
//               em =  (EntityManager) ctx.lookup("persistence/LogicalName");
//               utx.begin();
//               em.persist(object);
//               utx.commit();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error retreiving EntityManager", e);
        }
        
        // Test to see that annotations are correct
        if ( sf == null ) {
            logger.log(Level.SEVERE, "Registrar.init() SipFactory not initialized ");
        }
        if ( ts == null ) {
            logger.log(Level.SEVERE, "Registrar.init() Timer Service not initialized ");
        }
    }
    
   /*
    * (non-Javadoc)
    *
    * @see javax.servlet.sip.SipServlet#doOptions(javax.servlet.sip.SipServletRequest)
    */
    protected void doOptions(SipServletRequest request) throws ServletException, IOException {
        int maxFw = request.getMaxForwards();
        if (maxFw == 0) {
            logger.log(Level.INFO, "doOptions - UAS");
            SipServletResponse response = request.createResponse(200);
            response.send();
        } else { // Proxy
            logger.log(Level.INFO,"doOptions - Proxy");
            doProxy(request);
        }
        SipApplicationSession appsess = request.getApplicationSession(false);
        if (appsess != null) {
            appsess.invalidate();
            logger.log(Level.FINE, "appsession.invalidate()");
        }
    }
    
   /*
    * (non-Javadoc)
    *
    * @see javax.servlet.sip.SipServlet#doResponse(javax.servlet.sip.SipServletResponse)
    */
    protected void doResponse(SipServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "Servlet got response = " + resp);
        // resp.getSession().invalidate();
        SipApplicationSession appsess = resp.getApplicationSession(false);
        if (appsess != null) {
            appsess.invalidate();
        }
    }
    
    void doProxy(SipServletRequest request) throws ServletException, IOException {
        SipURI reqURI = cleanURI((SipURI) request.getRequestURI());
        Map reg = (Map) ctx.getAttribute(REGISTRATION_MAP);
        boolean foundProxy = false;
        if(reg != null){
            Iterator<SipURI> i = reg.keySet().iterator();
            while( i.hasNext() ) {
                SipURI target = i.next();
                SipURI to = (SipURI) reg.get(target);
                if( to.equals(reqURI) ) {
                    Proxy proxy = request.getProxy();
                    proxy.setRecordRoute( false );
                    logger.log(Level.INFO,"Proxy reqUri = " + reqURI + " to " + target);
                    if (target != null) proxy.proxyTo(target);
                    foundProxy = true;
                }
            }
        }
        if(!foundProxy) {
            logger.log(Level.FINE, "Sending 404");
            SipServletResponse response = request.createResponse(404);
            response.send();
        }
    }
    
    SipURI cleanURI(SipURI original) {
        logger.log(Level.FINE, "cleanURI original = " + original);
        SipURI copy = (SipURI) original.clone();
        Iterator headers = copy.getHeaderNames();
        if (headers.hasNext()) {
            headers.next();
            headers.remove();
        }
        Iterator parameters = copy.getParameterNames();
        if (parameters.hasNext()) {
            String param = (String) parameters.next();
            logger.log(Level.FINE, "cleanURI removing param = " + param);
            copy.removeParameter(param);
        }
        logger.log(Level.FINE, "cleanURI copy = " + copy);
        return copy;
    }
    
    
    private void storeRegistration( SipURI publicID, SipURI contact, long expires ) {
        Registration registration = new Registration();
        registration.setContact( ( (URI) contact).toString() );
        
        // expires is expressed in seconds, while settime is milli seconds
        Date expiresTime = new Date();
        expiresTime.setTime( expiresTime.getTime() + expires * 1000 );
        registration.setExpirationTime(expiresTime);
        
        // Set current Time
        registration.setRegistrationTime(new Date());
        
        
        // Set public ID
        registration.setId( ( (URI) publicID).toString() );
        
        EntityManager em = emf.createEntityManager();
        
        try {
            utx.begin();
            em.persist(registration);
            utx.commit();
            logger.log(Level.FINE, "Registration was successfully created.");
        } catch (Exception ex) {
            try {
                logger.log(Level.FINE, ex.getLocalizedMessage());
                utx.rollback();
            } catch (Exception e) {
                logger.log(Level.FINE, e.getLocalizedMessage());
            }
        }
        em.close();
    }
    
}
