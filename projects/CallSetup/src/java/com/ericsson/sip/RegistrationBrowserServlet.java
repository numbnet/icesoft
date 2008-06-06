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
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import javax.servlet.sip.SipFactory;

/**
 *
 * @author Sreeram
 * @version
 */
@PersistenceContext(name = "persistence/LogicalName", unitName = "EricssonSipPU")
public class RegistrationBrowserServlet extends HttpServlet {
@PersistenceUnit(unitName = "EricssonSipPU") private EntityManagerFactory emf;

   @Resource
   private UserTransaction utx;
   
   @Resource
   private SipFactory sf;

   public Collection getRegistrations() {
       EntityManager em = emf.createEntityManager();
       Query q = em.createQuery("select object(o) from Registration as o");
       return q.getResultList();
   }

   /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
       response.setContentType("text/html;charset=UTF-8");
       PrintWriter out = response.getWriter();
       Collection registrations = getRegistrations();
       Iterator iter = registrations.iterator();

       out.println("<html>");
       out.println("<head>");
       out.println("<title>Servlet NewServlet</title>");
       out.println("</head>");
       out.println("<body>");
       out.println("<br>");
       out.println("<p> The list of registered users with this application");
       out.print("</p>");
       out.println("<FORM ACTION = \"/CallSetup/SipCallsetupServlet\" METHOD = POST>");
       out.println("<table border=\"1\">");

       while (iter.hasNext()) {
           Registration rs = (Registration) iter.next();
           out.println("<tr>");

           out.println("<td>");
           out.println("<INPUT TYPE=\"CHECKBOX\" NAME=\"CONTACT\"" + " VALUE=\"" + rs.getContact() + "\">" + rs.getContact());
           out.println("</td>");

           out.println("<td>");
           out.println(rs.getRegistrationTime());
           out.println("</td>");

           out.println("<td>");
           out.println(rs.getExpirationTime());
           out.println("</td>");

           out.println("</tr>");
       }
       out.println("</table>");
       out.println("<INPUT TYPE=SUBMIT NAME=Submit VALUE=\"Submit\">");
       out.println("</FORM>");
       
       out.println("</body>");
       out.println("</html>");
       out.close();
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
}
   // </editor-fold>

   
