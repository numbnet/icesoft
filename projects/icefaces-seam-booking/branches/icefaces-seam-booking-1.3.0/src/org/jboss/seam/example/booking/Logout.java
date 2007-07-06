//$Id: Logout.java,v 1.1 2006/11/20 05:19:01 gavin Exp $

package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface Logout
{
   public String logout();
}