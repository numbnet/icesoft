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
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipURI;

/**
 * @author ekrigro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PresenceServlet extends SipServlet {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3978425801979081269L;
	//Reference to context - The ctx Map is used as a central storage for this app
	ServletContext ctx = null;

	/*
	 * Fix bc of Publish is not supported yet in SSA
	 */
	protected void doRequest(SipServletRequest req) throws ServletException,
			IOException {
		if( req.getMethod().equals("PUBLISH") ) {
			doPublish( req );
		}
		else {
			super.doRequest(req);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.sip.SipServlet#doResponse(javax.servlet.sip.SipServletResponse)
	 */
	protected void doResponse(SipServletResponse resp) throws ServletException,
			IOException {
      String ss = resp.getRequest().getHeader("Subscription-State");
      if( ss.startsWith("terminated") ) {
         SipApplicationSession appsess = resp.getApplicationSession(false);
         if (appsess != null)
         {
            appsess.invalidate();
         }
      }
      
		//log("Servlet got response = "+resp);
	}
	/*
	 * Saves the dialog in the helper object PresenceInfo based on TO header
	 */

	protected void doSubscribe(SipServletRequest req) throws ServletException,
			IOException {
		SipURI to = (SipURI)req.getTo().getURI();
		// ID for the hashmap is based on user@host
		StringBuilder id = new StringBuilder(to.getUser());
		id.append('@');
		id.append(to.getHost());
		String key = id.toString();
      synchronized( key ) {
         PresenceInfo pi = (PresenceInfo)ctx.getAttribute( key );
		
         String expires = req.getHeader("Expires");
         int exp = 3600;
         if( expires != null && expires.length() > 0 ) {
            exp = Integer.parseInt( expires );
         }
      
         SipServletResponse resp = req.createResponse(200);
         resp.setHeader("Expires",expires);
         resp.send();
		
         SipSession session = resp.getSession();
		
         String event = req.getHeader("Event");
         if( pi != null && pi.getLastPublish() != null ) {
            if( event.equals("presence") ) {
               Set<SipSession> s = pi.getSubscriptions();
               s.remove(session);
               if( exp > 0 ) s.add(session);
               else System.out.println("Removed SESSION = "+session);
            }
         }
         else {
            pi = new PresenceInfo(key,session);
         }
         //Store the accept headers
         @SuppressWarnings("unchecked") ListIterator<String> li = req.getHeaders("Accept");
         while ( li.hasNext() ) {
            pi.getAccept().add(li.next().toString());
         }
      
         ctx.setAttribute( key, pi );
		
         //Send notify
         SipServletRequest notify = session.createRequest("NOTIFY");
         if( exp > 0 )
         {
            notify.setHeader("Subscription-State","active;expires="+expires);
         }
         else {
            notify.setHeader("Subscription-State","terminated;deactivated");
         }
         notify.setHeader("Event",req.getHeader("Event"));
         Object content = pi.getLastPublish();
         String type = pi.getContentType();
         if( event.equalsIgnoreCase("presence.winfo")) {
            StringBuilder body = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><watcherinfo xmlns=\"urn:ietf:params:xml:ns:watcherinfo\" version=\"2\" state=\"partial\"><watcher-list resource=\"");
            body.append(pi.getId());
            body.append("\" package=\"presence\"></watcher-list></watcherinfo>");
            notify.setContent( body.toString(), "application/watcherinfo+xml" );
         }
         else if( content != null && type != null ) {
            //Check Accept headers
            if( pi.getAccept().isEmpty() || pi.getAccept().contains(type) ) {
               notify.setContent( content, type );
            }
         }
         notify.send();
      }
	}
	
	/*
	 * Create a PresenceInfo to store the last publish state
	 * After the 200 all subscriber should be notified. 
	 */
	
	protected void doPublish(SipServletRequest req) throws ServletException,
	IOException {
		SipURI to = (SipURI)req.getTo().getURI();
		byte [] body = req.getRawContent();
		StringBuilder id = new StringBuilder(to.getUser());
		id.append('@');
		id.append(to.getHost());
		String key = id.toString();
		String expires = req.getHeader("Expires");
		boolean expired = false;
        if( expires != null && expires.equals("0"))
        	expired = true;
      synchronized( key ) {
         PresenceInfo pi = (PresenceInfo)ctx.getAttribute( key );
         if( pi != null ) {
            pi.setContentType(req.getContentType());
             
            pi.setLastPublish(body,expired);
            Iterator<SipSession> i = pi.getSubscriptions().iterator();
            while( i.hasNext() ) {
               SipSession s = i.next();
               // Send notify
               SipServletRequest notify = s.createRequest("NOTIFY");
               //s.getApplicationSession().getTimers()
               notify.setHeader("Subscription-State","active;expires="+600);
               notify.setHeader("Event",req.getHeader("Event"));
               notify.setContent( pi.getLastPublish(), pi.getContentType() );
               notify.send();
            }
            pi.setLastPublish( body, expired );
         }
         else {
            pi = new PresenceInfo(key,body,req.getContentType());
         }
         ctx.setAttribute( key, pi );
      }
		//System.out.println("Test : "+ ctx.getAttribute("TEST"));
		if( expires == null ) expires = "600";
		SipServletResponse resp = req.createResponse(200);
		resp.setHeader("Expires",expires);
		resp.send();
      SipApplicationSession appsess = req.getApplicationSession(false);
      if (appsess != null)
      {
         appsess.invalidate();
      }

	}
	
	public void init(ServletConfig config) throws ServletException
    {
		super.init(config);
		ctx = config.getServletContext();
	}
}
