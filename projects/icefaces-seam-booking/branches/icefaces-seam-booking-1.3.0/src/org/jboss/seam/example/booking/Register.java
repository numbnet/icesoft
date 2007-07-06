//$Id: Register.java,v 1.3 2006/12/19 01:38:51 gavin Exp $

package org.jboss.seam.example.booking;


import javax.ejb.Local;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
@Local
public interface Register
{
   public void register();
   public void invalid();
   public String getVerify();
   public void setVerify(String verify);
   public boolean isRegistered();  
   public void errorInput(ValueChangeEvent event);
   public void destroy();

}