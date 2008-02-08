package com.icesoft.ejb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.io.Serializable;
import java.util.Calendar;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;

import com.icesoft.eb.AuctionitemBean;
import com.icesoft.eb.Bid;
import com.icesoft.eb.User;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.util.SeamUtilities;

@Stateful
@Name("bidAction")
@Scope(ScopeType.EVENT)
public class BidAction implements IBidAction, Serializable {
	AuctionitemBean auctionitemBean;

	@In(create=true) 
	@Out(required=false)
	private Bid bid;

	@PersistenceContext
	private EntityManager em;
	
//	@In
//	private FacesMessages facesMessages;
	
	@In
	private User user;
	
	@Logger
	private Log log;
	
	@In
	private transient RenderManager renderManager;
	
    
    public void bid(AuctionitemBean selectItem)  {
      	auctionitemBean=selectItem;

	    // Validate input
	    if ( getBidInput() <= auctionitemBean.getBid().getBidValue() )
	    {
	    	log.info("Bid must be higher than existing price");
//	           facesMessages.add("Bid must be higher than existing price");
	     }
	    else if ( getBidInput() > 999999 )
	    {
//	    	facesMessages.add("Bid must be less than $1,000,000");
	    }
	    else if (getBidInput()-auctionitemBean.getBid().getBidValue() < auctionitemBean.getAuctionitem().getPrice() )
	    {
//	    	facesMessages.add("Bids on this item must be in increments of " + auctionitemBean.getAuctionitem().getPrice());
	    }
	    else  {
	       // Persist new Bid
		    try{
		     	bid = new Bid();
			    bid.setBidValue(getBidInput());
			    bid.setAuctionItem(auctionitemBean.getAuctionitem());
			    bid.setUser(user);

			    Calendar calendar = Calendar.getInstance();
			    bid.setTimestamp( calendar.getTime() );
			    bid.setCreditCard("1234123412341234");
			    bid.setCreditCardName("ICEsoft Financial");
			    log.info("PERSISTING BID");

			 	em.persist(bid);

		        // Make necessary changes to current view and call render.
			    auctionitemBean.setBidding(false);
			    //let render call update the last bid

				em.flush();
				String itemId = Long.toString(auctionitemBean.getAuctionitem().getItemId());
                renderManager.getOnDemandRenderer(itemId).requestRender();
//				facesMessages.add("Thank you, #{user.name}, bid of #{bid.bidValue} accepted.");
				log.info("Thank you, #{user.name}, bid of #{bid.bidValue} accepted.");

		    }catch (Exception e){
		    	e.printStackTrace();    
		    }
	    }    
	}
	

    public void cancel(){
    	log.info("action is cancelled");
    }

    @Remove @Destroy
	public void destroy() {
		// TODO Auto-generated method stub
	}
    
    private double getBidInput() {
        return this.auctionitemBean.getBidInput();
    }

}
