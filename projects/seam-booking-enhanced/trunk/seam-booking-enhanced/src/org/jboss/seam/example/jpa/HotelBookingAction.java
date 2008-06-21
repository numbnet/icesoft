//$Id: HotelBookingAction.java 5509 2007-06-25 16:19:40Z gavin $
package org.jboss.seam.example.jpa;

import java.util.Calendar;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Destroy;

import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;
import org.jboss.seam.core.ConversationEntry;
import java.io.Serializable;

@Name("hotelBooking")
@Scope(ScopeType.CONVERSATION)
public class HotelBookingAction 
{  
   @In
   private EntityManager em;
   
   @In
   private User user;
   
   @Out(required=false)
   private Hotel hotel;
   
   private Booking booking;
     
   @In
   private FacesMessages facesMessages;
      
   @In
   private Events events;
   
   @Logger 
   private Log log;
   
   private boolean bookingValid;
   @Out
   private boolean visible = false;
   @Out
   private boolean confirmVisible = false;
   
//   @Begin
   public void selectHotel(Hotel selectedHotel)
   {
	   /** I found that with row selector, the interception
	    * for using the @Begin was missed somehow
	    * this is something I will talk to our components
	    * team about.  It isn't a problem in non-portlet 
	    * environments, so I just did this manually for now
	    */
	  if (!Manager.instance().isLongRunningConversation()){
		   Manager.instance().beginConversation();
		   log.info("HAD TO MANUALLY START CONVERSATION!!");
	  }
	  else log.info("LR conversation already started");	  	 
      hotel = em.merge(selectedHotel);
   }
   
    @Begin(join=true)
   public void bookHotel()
   {  
	  this.visible=true;
      booking = new Booking(hotel, user);
      Calendar calendar = Calendar.getInstance();
      booking.setCheckinDate( calendar.getTime() );
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      booking.setCheckoutDate( calendar.getTime() );
   }
   public void setBookingDetails()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_MONTH, -1);
      if ( booking.getCheckinDate().before( calendar.getTime() ) )
      {
         facesMessages.addToControl("checkinDate", "Check in date must be a future date");
         bookingValid=false;
      }
      else if ( !booking.getCheckinDate().before( booking.getCheckoutDate() ) )
      {
         facesMessages.addToControl("checkoutDate", "Check out date must be later than check in date");
         bookingValid=false;
      }
      else
      {
         bookingValid=true;
      }
	  this.visible=false;
      this.confirmVisible=true;
   }
   public boolean isBookingValid()
   {
      return bookingValid;
   }
   
    
   @End(beforeRedirect=true)
   public void confirm()
   {
      em.persist(booking);
      facesMessages.add("Thank you, #{user.name}, your confimation number for #{hotel.name} is #{booking.id}");
      log.info("New booking: #{booking.id} for #{user.username}");
      events.raiseTransactionSuccessEvent("bookingConfirmed");
   }
   
   @End(beforeRedirect=true)
   public void cancel() {
	  Events.instance().raiseEvent("cancelDetails");
   }   
   
   @Observer("selectedHotel")
   public void setSelectedHotelToView(Hotel hotel){
	   this.selectHotel(hotel);
   }
   
   public void revise(){
	   this.visible=true;
	   this.confirmVisible=false;
   }
   
   public Hotel getHotel(){
	   return this.hotel;
   }

   public boolean isVisible(){
	   return this.visible;
   }
   
   public boolean isConfirmVisible(){
	   return this.confirmVisible;
   }
   
   public Booking getBooking(){
	   return this.booking;
   }

   @Destroy
   public void destroy(){
	   log.info("destroying Conversational bean");
   }
}
