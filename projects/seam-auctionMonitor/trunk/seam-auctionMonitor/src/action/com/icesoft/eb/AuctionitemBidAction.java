package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;
import java.util.Calendar;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.Begin;
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
//@Restrict("#{identity.loggedIn}")

public class AuctionitemBidAction implements AuctionitemBid {
    @PersistenceContext(type=EXTENDED)
    private EntityManager em;
    
    @In 
    private User user;
    
    @In(required=false) @Out
    private AuctionitemBean item;
    
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
    
    @Begin
    public void selectItem(AuctionitemBean selectedItem)
    {
       item = em.merge(selectedItem);
    }
    
    public void bid()
    {      
       bid = new Bid(item.getAuctionitem(), user);
       Calendar calendar = Calendar.getInstance();
       bid.setTimestamp( calendar.getTime() );
    }
    public void setBidDetails()
    {
       Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.DAY_OF_MONTH, -1);
       if ( bid.getAuctionItem().getPrice()<= item.getAuctionitem().getPrice() )
       {
          //facesMessages.addToControl("checkinDate", "Bid must be higher than existing price");
          bidValid=false;
       }
       else if ( bid.getAuctionItem().getPrice()> 999999 )
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
       item.render();
    }
    
    @End
    public void cancel() {}
    
    @Destroy @Remove
    public void destroy() {}
}
