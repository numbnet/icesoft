/*
 * LogoutAction.java
 *
 * Created on June 5, 2007, 4:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.ejb;

import javax.ejb.Stateless;

import org.jboss.seam.Seam;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;

@Stateless
@Name("logout")
public class LogoutAction implements Logout
{
	
   @In
   Identity identity;
   
   public String logout()
   {
      identity.logout();
      return "login";
   }
}

