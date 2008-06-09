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

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import org.icefaces.x.core.push.SessionRenderer;

import javax.servlet.ServletContext;

import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletRequest;

/**
 *
 * @author 
 * @version
 */
@PersistenceContext(name = "persistence/LogicalName", unitName = "EricssonSipPU")
public class CallTracker  {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("EricssonSipPU");
    private EntityManager em = emf.createEntityManager();
    private HashSet selections = new HashSet();
    private SipFactory sipFactory = null;

    public CallTracker()  {
        SessionRenderer.addCurrentSession("presence");
    }


   public Collection getRegistrations() {
       Query q = em.createQuery("select object(o) from Registration as o");
       Collection results = q.getResultList();
       Collection wrappedResults = wrapResults(results);
       printRegistrations(wrappedResults);
       return wrappedResults;
   }

    public Collection wrapResults(Collection c)  {
        Collection wrappedResults = new ArrayList();
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            Registration rs = (Registration) iter.next();
            wrappedResults.add(new RegistrationWrapper(this, rs));
        }
        return wrappedResults;
    }

    public void printRegistrations(Collection c)  {
        Iterator iter = c.iterator();
        System.out.println("List of Registered Users in " + c);
        while (iter.hasNext()) {
            RegistrationWrapper rs = (RegistrationWrapper) iter.next();
            System.out.println("Registered user " + rs.getId());
        }

    }


    public void setSelected(Registration registration, boolean selected)  {
        if (selected)  {
            selections.add(registration);
        } else {
            selections.remove(registration);
        }
    }
    
    public boolean getSelected(Registration registration)  {
        return selections.contains(registration);
    }

    public String call()  {
        Iterator callers = selections.iterator();
        Registration callerA = null;
        Registration callerB = null;
        if (callers.hasNext())  {
            callerA = (Registration) callers.next();
        }
        if (callers.hasNext())  {
            callerB = (Registration) callers.next();
        }
        if ( (null != callerA) && (null != callerB) )  {
            invite(callerA, callerB);
        }
        return "success";
    }
    
    private void invite(Registration regA, Registration regB)  {
        if (null == sipFactory)  {
            ServletContext servletContext = 
                    (ServletContext) FacesContext.getCurrentInstance()
                        .getExternalContext().getContext();
            sipFactory = (SipFactory) servletContext
                .getAttribute(SipServlet.SIP_FACTORY);
        }
          SipApplicationSession appSession = sipFactory
                .createApplicationSession();
          String callA = regA.getContact();
          String callB = regB.getContact();
          try  {
              Address to = sipFactory.createAddress(callB);
              Address from = sipFactory.createAddress(callA);
              
              SipServletRequest sipReq = sipFactory
                    .createRequest(appSession, "INVITE", from, to);

             sipReq.setAttribute("CALL","INITIAL");
             
             // set servlet to invoke by reponse
             SipSession sipSession = sipReq.getSession();
             sipSession.setHandler("b2b");
             
             // lets send invite to B ...
             sipReq.send();
        } catch (Exception e)  {
            e.printStackTrace();
        }

    }
}

   
