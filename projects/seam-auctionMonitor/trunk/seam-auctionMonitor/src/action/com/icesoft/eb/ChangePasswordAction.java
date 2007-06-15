/*
 * ChangePasswordAction.java
 *
 * Created on June 5, 2007, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.eb;

import static org.jboss.seam.ScopeType.EVENT;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.FacesMessages;

import com.icesoft.eb.User;

@Stateful
@Scope(EVENT)
@Name("changePassword")
public class ChangePasswordAction implements ChangePassword
{

   @In @Out
   private User user;
   
   @PersistenceContext
   private EntityManager em;
   
   private String verify;
   
   public String changePassword()
   {
      if ( user.getPassword().equals(verify) )
      {
         user = em.merge(user);
         FacesMessages.instance().add("Password updated");
         return "main";
      }
      else 
      {
         FacesMessages.instance().add("verify", "Re-enter new password");
         revertUser();
         verify=null;
         return null;
      }
   }
   
   public String cancel()
   {
      revertUser();
      return "main";
   }

   private void revertUser()
   {
      user = em.find(User.class, user.getUsername());
   }

   public String getVerify()
   {
      return verify;
   }

   public void setVerify(String verify)
   {
      this.verify = verify;
   }
   
   @Destroy @Remove
   public void destroy() {}
}
