/*
 * Logout.java
 *
 * Created on June 5, 2007, 4:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package action.com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface Logout
{
   public String logout();
}
