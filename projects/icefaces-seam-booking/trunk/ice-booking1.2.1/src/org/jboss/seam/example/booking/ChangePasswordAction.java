//$Id: ChangePasswordAction.java,v 1.19 2007/02/13 03:49:42 sbryzak2 Exp $
package org.jboss.seam.example.booking;
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
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.core.FacesMessages;
@Stateful
@Scope(EVENT)
@Name("changePassword")
@Restrict("#{identity.loggedIn}")
public class ChangePasswordAction implements ChangePassword
{
   @In @Out
   private User user;
   
   @PersistenceContext
   private EntityManager em;
   
   private String verify;
   
   private boolean changed;
   
   @In
   private FacesMessages facesMessages;
   
   public void changePassword()
   {
      if ( user.getPassword().equals(verify) )
      {
          System.out.println("CHANGE PASSWORD: before em call");
         user = em.merge(user);
         System.out.println("\t after em");
         facesMessages.add("Password updated");
         changed = true;
      }
      else 
      {
         facesMessages.addToControl("verify", "Re-enter new password");
         revertUser();
         verify=null;
      }
   }
   
   public boolean isChanged()
   {
      return changed;
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
