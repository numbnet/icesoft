//$Id: User.java,v 1.5 2006/10/25 15:31:57 gavin Exp $
package org.jboss.seam.example.booking;

import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Scope;
import javax.faces.event.ValueChangeEvent;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import com.icesoft.faces.component.ext.HtmlInputText;
import org.jboss.seam.core.FacesMessages;
import java.util.List;


@Entity
@Name("user")
@Scope(SESSION)
@Table(name="Customer")
public class User implements Serializable
{
   private String username;
   private String password;
   private String name;
 //   @In
 //  private FacesMessages facesMessages;
    
   public User(String name, String password, String username)
   {
      this.name = name;
      this.password = password;
      this.username = username;
   }
   
   public User() {}

   @NotNull
   @Length(max=100)
   public String getName()
   {
      return name;
   }
   public void setName(String name)
   {
      this.name = name;
   }
   
   @NotNull
   @Length(min=5, max=15)
   public String getPassword()
   {
      return password;
   }
   public void setPassword(String password)
   {
      this.password = password;
   }
   
   @Id
   @Length(min=5, max=15)
   @Pattern(regex="^\\w*$", message="not a valid username")
   public String getUsername()
   {
      return username;
   }
   public void setUsername(String username)
   {
      System.out.println("In User and name set to= "+username);
      this.username = username;
   }
   
   @Override
   public String toString() 
   {
      return "User(" + username + ")";
   }
   
 
/*   public void errorInput(ValueChangeEvent event){
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
   } */
}
