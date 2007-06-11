package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;
import java.util.Calendar;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Conversational;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.FacesMessages;
import org.jboss.seam.log.Log;

@Stateful
@Name("itemBid")
//@Conversational(ifNotBegunOutcome="main")
//@Restrict("#{identity.loggedIn}")
@LoggedIn

public class AuctionitemBidAction implements AuctionitemBid {
    @PersistenceContext(type=EXTENDED)
    private EntityManager em;
    
    @In 
    private User user;
    
    @In(required=false) @Out
    private AuctionitemBean selectedItem;
    
    @In(required=false) 
    @Out(required=false)
    private Bid bid;
      
    @In
    private FacesMessages facesMessages;
       
    @In
    private Events events;
    
    @Logger 
    private Log log;
    
    private boolean bidValid;
    
    @In
    ViewManager viewManager;
    
    @Begin(join=true)
    public void selectItem(AuctionitemBean selectedItem)
    {
       selectedItem = em.merge(selectedItem);
    }
    
    public void bid()
    {      
       bid = new Bid(selectedItem.getAuctionitem(), user);
       Calendar calendar = Calendar.getInstance();
       bid.setTimestamp( calendar.getTime() );
    }
    public void setBidDetails()
    {
       if ( bid.getBidValue()<= selectedItem.getAuctionitem().getPrice() )
       {
          //facesMessages.addToControl("checkinDate", "Bid must be higher than existing price");
          bidValid=false;
       }
       else if ( bid.getBidValue()> 999999 )
       {
          //facesMessages.addToControl("checkoutDate", "Bid must be less than $1,000,000");
          bidValid=false;
       }
       else
       {
          bidValid=true;
       }
    }
    
    public boolean isBidValid()
    {
       return bidValid;
    }
    
    @End
    public void confirm()
    {
       em.persist(bid);
       //facesMessages.add("Thank you, #{user.name}, your confimation number for #{item.name} is #{bid.id}");
       log.info("New booking: #{bid.id} for #{user.username}");
       events.raiseTransactionSuccessEvent("bidConfirmed");
       selectedItem.render();
    }
    
    @End
    public void cancel() {}
    
    @Destroy @Remove
    public void destroy() {}
}
