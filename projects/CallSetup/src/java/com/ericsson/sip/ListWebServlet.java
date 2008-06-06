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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerService;
import javax.transaction.UserTransaction;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.ericsson.sip.Registration;

/* TODO Add imports */
/**
 * TODO Add comments (class description)
 * 
 * 
 * @author ekrigro
 * @since 2005-apr-12
 * 
 */
@PersistenceContext(name = "persistence/LogicalName", unitName = "EricssonSipPU")
public class ListWebServlet extends HttpServlet
{
    @PersistenceUnit(unitName = "EricssonSipPU") 
    private EntityManagerFactory emf;

    @PersistenceContext ( unitName="EricssonSipPU" )
    private EntityManager em;
    
    @Resource
    private UserTransaction utx;
  
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private Logger logger = Logger.getLogger("CallSetup");
   SipFactory sf = null;
   TimerService ts = null;
   ServletContext ctx = null;
   String requestURIofUAS = null;
   String fromURIofUAC = null;
   public static final String VERSION = "Registrar ver 0.0.3";

   
   
   
   
   
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {  
      logger.log(Level.FINE, "doGet() ListWebServlet.");
     
      EntityManager em = emf.createEntityManager();
      Query q = em.createQuery("select object(o) from Registration as o");
      List<Registration> registrations = q.getResultList();
      em.close();
        
      resp.setStatus(200);
      ServletOutputStream out = resp.getOutputStream();
      out.println("<pre>");
      out.println("Version = " + VERSION);
      out.println("URI : CONTACT");      
      
      Iterator registrationIterator = registrations.iterator();
      for (Registration registration : registrations) {
          out.println(registration.getId() + " : " + registration.getContact());
	  }
      
      
      out.println("Session's:");
      Enumeration e = ctx.getAttributeNames();
      while( e.hasMoreElements() ) {
         Object o = e.nextElement();
         if( o instanceof PresenceInfo ) {
            PresenceInfo pi = (PresenceInfo) o;
            out.println();
            out.print("PresID = ");
            out.print( pi.getId() );
            out.print(" : ContentType = ");
            out.print( pi.getContentType() );
            out.print(" : Publish = ");
            out.print( pi.getLastPublish().toString() );
            out.println();
            Iterator<SipSession> is = pi.getSubscriptions().iterator();
            while( is.hasNext() ) {
               SipSession s = is.next();
               out.println(s.toString());
            }
         }
      }     
      out.println("</pre>");
      out.flush();
      // Call both people if there are two registered for testing
      if( registrations.size() == 2 ) {
    	  
    	  
    	  registrations.get(1);
    	  
          SipApplicationSession as = sf.createApplicationSession();
          //Just to make it simpler to understand 0 will be called A 1 B
          String callA = registrations.get(0).getContact();
          String callB = registrations.get(1).getContact();
          
          Address to = sf.createAddress(callB);
          Address from = sf.createAddress(callA);
          
          SipServletRequest sipReq = sf.createRequest(as, "INVITE", from, to);

         logger.log(Level.FINE, "ListWebServlet sipRequest = " + sipReq.toString());
         sipReq.setAttribute("CALL","INITIAL");
         
         // set servlet to invoke by reponse
         SipSession s = sipReq.getSession();
         s.setHandler("b2b");
         
         // lets send invite to B ...
         sipReq.send();
      }
   }

   @Override
   public void destroy()
   {
      super.destroy();
   }

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
         logger.log(Level.SEVERE, "Could not retreive the SipFactory", t);
      }

      logger.log(Level.FINE, "Started ListWebServlet.");

   }
}
