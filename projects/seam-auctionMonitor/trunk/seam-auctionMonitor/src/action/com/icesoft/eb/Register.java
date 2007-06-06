/*
 * Register.java
 *
 * Created on June 5, 2007, 3:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package action.com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface Register
{
   public String register();
   public String getVerify();
   public void setVerify(String verify);
   
   public void destroy();
}