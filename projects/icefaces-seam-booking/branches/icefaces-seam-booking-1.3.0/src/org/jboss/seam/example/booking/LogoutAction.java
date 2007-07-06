//$Id: LogoutAction.java,v 1.1 2006/11/20 05:19:01 gavin Exp $
package org.jboss.seam.example.booking;

import javax.ejb.Stateless;

import org.jboss.seam.Seam;
import org.jboss.seam.annotations.Name;

@Stateless
@LoggedIn
@Name("logout")
public class LogoutAction implements Logout
{
   public String logout()
   {
      Seam.invalidateSession();
      return "login";
   }
}
