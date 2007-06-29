//$Id: BookingList.java,v 1.5 2006/12/17 01:04:51 gavin Exp $
package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface BookingList
{
   public void getBookings();
   public Booking getBooking();
   public void cancel();
   public void destroy();
}