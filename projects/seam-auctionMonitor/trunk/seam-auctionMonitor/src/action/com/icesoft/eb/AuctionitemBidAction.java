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
@Restrict("#{identity.loggedIn}")

public class AuctionitemBidAction implements AuctionitemBid {
    @PersistenceContext(type=EXTENDED)
    private EntityManager em;
    
    @In 
    private User user;
    
    @In(required=false) @Out
    private AuctionitemBean auctionitemBean;
    
    @In(required=false) 
    @Out(required=false)
    private Bid bid;
      
    @In
    private FacesMessages facesMessages;
       
    @In
    private Events events;
    
    @Logger 
    private Log log;
    
//    @In
//    ViewManagerAction viewManager;

    double bidInput;
    
    @Begin(join=true)
    public String selectItem(AuctionitemBean selectedItem)
    {
       try{
       auctionitemBean = em.merge(selectedItem);
       auctionitemBean.setBidding(true);
       bidInput = auctionitemBean.getAuctionitem().getPrice();
       bid = new Bid(auctionitemBean.getAuctionitem(), user);
       bid.setBidValue(bidInput);
       }catch(Exception e){
           e.printStackTrace();
       }
       return "";
    }
    
    @End(ifOutcome={"success"})
    public String bid()
    {
        if ( bidInput <= auctionitemBean.getAuctionitem().getPrice() )
        {
           facesMessages.addToControl("item_localBid", "Bid must be higher than existing price");
           return "";
        }
        else if ( bidInput > 999999 )
        {
           facesMessages.addToControl("item_localBid", "Bid must be less than $1,000,000");
           return "";
        }        
       Calendar calendar = Calendar.getInstance();
       bid.setTimestamp( calendar.getTime() );
       em.persist(bid);
       facesMessages.add("Thank you, #{user.name}, bid of #{bid.bidValue} accepted.");
       log.info("New bid: #{bid.id} for #{user.username}");
       events.raiseTransactionSuccessEvent("bidConfirmed");
       auctionitemBean.setBidding(false);
       auctionitemBean.buildBidEffect();
       auctionitemBean.render();
       return "success";
    }
    
    @End
    public String cancel() {
        auctionitemBean.setBidding(false);
        return "";
    }
    
    @Destroy @Remove
    public void destroy() {}

}
