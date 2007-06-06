/*
 * LogoutAction.java
 *
 * Created on June 5, 2007, 4:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package action.com.icesoft.eb;

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

