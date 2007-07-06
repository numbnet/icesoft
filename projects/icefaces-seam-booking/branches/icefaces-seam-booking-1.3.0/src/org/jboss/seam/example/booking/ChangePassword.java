//$Id: ChangePassword.java,v 1.3 2006/12/17 01:04:51 gavin Exp $
package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface ChangePassword
{
   public void changePassword();
   public boolean isChanged();
   
   public String getVerify();
   public void setVerify(String verify);
   
   public void destroy();
}