//$Id: RegisterAction.java,v 1.20 2007/02/25 19:09:39 gavin Exp $
package org.jboss.seam.example.booking;
import static org.jboss.seam.ScopeType.EVENT;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.FacesMessages;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import com.icesoft.faces.component.ext.HtmlInputText;

@Stateful
@Scope(EVENT)
@Name("register")
public class RegisterAction implements Register
{
   @In
   private User user;
   
   @PersistenceContext
   private EntityManager em;
   
   @In
   private FacesMessages facesMessages;
   
   private String verify;
   
   private boolean registered;
   
   public void register()
   {
       List allUsers = em.createQuery("select u.username from User u").getResultList();
       Iterator i = allUsers.iterator();
       System.out.println(">>>>>list all users");
       while(i.hasNext()){
           String u1 = (String)i.next();
           System.out.println("\t user="+u1);
       }    
      if ( user.getPassword().equals(verify) )
      {
         List existing = em.createQuery("select u.username from User u where u.username=#{user.username}")
            .getResultList();
 
         if (existing.size()==0)
         {
            em.persist(user);
            facesMessages.add("Successfully registered as #{user.username}");
            registered = true;
         }
         else
         {
            facesMessages.addToControl("username", "Username #{user.username} already exists");
         }
      }
      else 
      {
         facesMessages.addToControl("verify", "Re-enter your password");
         verify=null;
      }
   }
   
   public void invalid()
   {
       System.out.println("in register:invalid()");
      facesMessages.add("Please try again");
   }
   
   public boolean isRegistered()
   {
      return registered;
   }
   public String getVerify()
   {
      return verify;
   }
   public void setVerify(String verify)
   {
      this.verify = verify;
   }
   
   public void errorInput(ValueChangeEvent event){
       System.out.println("Why can't I get into this method!!!");
       HtmlInputText component1 = (HtmlInputText)event.getSource();
       System.out.println("In errorUserName with component1 ="+component1.COMPONENT_TYPE);
       List currMsgs= facesMessages.getCurrentMessages();
       Iterator i = currMsgs.iterator();
       while(i.hasNext()){
           FacesMessage msg = (FacesMessage)i.next();
           System.out.println("FacesMessage.getDetail is ="+msg.getDetail());
           System.out.println("FacesMessage.  is="+msg.getSummary());
          // component1.setFocus(true);
           System.out.println("getting text from component="+ component1.getText());
           component1.requestFocus();
       }
   }
   
   @Destroy @Remove
   public void destroy() {}
}
