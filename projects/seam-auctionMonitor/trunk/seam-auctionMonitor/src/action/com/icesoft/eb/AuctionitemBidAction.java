package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.util.Calendar;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Conversational;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.FacesMessages;
import org.jboss.seam.log.Log;

@Stateful
@Name("itemBid")
@Scope(ScopeType.SESSION)
//@Conversational(ifNotBegunOutcome="main")
@Restrict("#{identity.loggedIn}")

public class AuctionitemBidAction implements AuctionitemBid {
    @PersistenceContext(type=EXTENDED)
    private EntityManager em;
    
    @In 
    private User user;
    
    @In(create=true, required=false) @Out
    private AuctionitemBean auctionitemBean;
    
    @In(required=false) 
    @Out(required=false)
    private Bid bid;
       
    @In
    private Events events;
    
    @Logger 
    private Log log;
    
//    @In
//    ViewManagerAction viewManager;
    
    @Begin(join=true, nested=true)
    public String selectItem(AuctionitemBean selectedItem)
    {
       try{
       Auctionitem temp = auctionitemBean.getAuctionitem();
       temp = em.merge(selectedItem.getAuctionitem());
       auctionitemBean = selectedItem;
       auctionitemBean.setBidding(true);
       bid = new Bid(auctionitemBean.getAuctionitem(), user);
       bid.setBidValue(auctionitemBean.getBidInput());
       }catch(Exception e){
           e.printStackTrace();
       }
       return "";
    }
    
    @End(ifOutcome={"success"})
    public String bid()
    {
        try{
        System.out.println("BID!!!! CURRENT BID: " + auctionitemBean.getBidInput());
        if ( auctionitemBean.getBidInput() <= auctionitemBean.getBid().getBidValue() )
        {
           System.out.println("BID TOO LOW !!!!");
           FacesMessages.instance().add("Bid must be higher than existing price");
           return "";
        }
        else if ( auctionitemBean.getBidInput() > 999999 )
        {
           System.out.println("BID TOO HIGH !!!!");
           FacesMessages.instance().add("Bid must be less than $1,000,000");
           return "";
        }        
       
       bid.setBidValue(auctionitemBean.getBidInput());
       Calendar calendar = Calendar.getInstance();
       bid.setTimestamp( calendar.getTime() );
       System.out.println("PERSISTING BID");
       bid.setCreditCard("1234123412341234");
       bid.setCreditCardName("American Express");
       em.persist(bid);
       FacesMessages.instance().add("Thank you, #{user.name}, bid of #{bid.bidValue} accepted.");
       log.info("New bid on: #{bid.id} of #{bid.bidValue} for #{user.username}");
       auctionitemBean.setBid(bid);
       auctionitemBean.setBidding(false);
       auctionitemBean.buildBidEffect();
       System.out.println("CALLING RENDER");
       auctionitemBean.render();
        }catch(Exception e){
            e.printStackTrace();
        }
       return "success";
    }
    
    @End
    public String cancel() {
        auctionitemBean.setBidInput(0.0);
        auctionitemBean.setBidding(false);
        return "";
    }
    
    @Destroy @Remove
    public void destroy() {}

}
