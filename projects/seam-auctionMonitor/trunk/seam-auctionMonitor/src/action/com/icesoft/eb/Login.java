/*
 * Login.java
 *
 * Created on June 5, 2007, 4:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package action.com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface Login
{
   public String login();
}

