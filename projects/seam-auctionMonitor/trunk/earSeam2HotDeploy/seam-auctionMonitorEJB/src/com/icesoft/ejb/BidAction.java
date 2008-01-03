package com.icesoft.ejb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.io.Serializable;
import java.util.Calendar;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;


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
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
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

	@In(required=false) 
	@Out(required=false)
	private Bid bid;

    @PersistenceContext(type=EXTENDED)
	private EntityManager em;
	
	@In(create=true)
	private FacesMessages facesMessages;
	
	@In
	private User user;
	
	@Logger
	private Log log;
	
	@In
	RenderManager renderManager;
	
    private double bidInput = 0;
	
    
    public boolean bid(AuctionitemBean selectItem)
	{
    	bidInput = selectItem.getMinBid();
    	log.info("passed into BidAction selectItem="+selectItem.getAuctionitem().getTitle());
    	
      	this.auctionitemBean=selectItem;
    	log.info("in bid new value="+getBidInput());
	    // Validate input
	    if ( getBidInput() <= auctionitemBean.getBid().getBidValue() )
	    {
	           facesMessages.add("Bid must be higher than existing price");
	           return false;
	     }
	    else if ( getBidInput() > 999999 )
	    {
	    	facesMessages.add("Bid must be less than $1,000,000");
	        return false;
	    }
	    else if (getBidInput()-auctionitemBean.getBid().getBidValue() < auctionitemBean.getAuctionitem().getPrice() )
	    {
	    	facesMessages.add("Bids on this item must be in increments of " + auctionitemBean.getAuctionitem().getPrice());
	        return false;
	    }
  
	       // Persist new Bid
	    try{
	     	bid = new Bid();
		    bid.setBidValue(getBidInput());
		    bid.setAuctionItem(auctionitemBean.getAuctionitem());
		    bid.setUser(user);
//	        auctionitemBean.getBid().setBidValue(getBidInput());
		    Calendar calendar = Calendar.getInstance();
		    bid.setTimestamp( calendar.getTime() );
		    bid.setCreditCard("1234123412341234");
		    bid.setCreditCardName("ICEsoft Financial");
		    log.info("PERSISTING BID");
		    //if there is a transaction open, first join it --may not need this?
//		    if (em.isOpen()){
//		    	log.info("Entity Manager is open");
//		        try
//		         {
//		            Transaction.instance().enlist( em );
				 	em.persist(bid);
				    log.info("New bid on: #{bid.id} of #{bid.bidValue} for #{user.username}");

			        // Make necessary changes to current view and call render.
				    auctionitemBean.setBidding(false);
				    //let render call update the last bid
//				    auctionitemBean.setBidInput(auctionitemBean.getMinBid());

				    em.flush();
				    String renderGroup = Long.toString(auctionitemBean.getAuctionitem().getItemId());
				    log.info("before calling new render for this item's group="+renderGroup);
					makeRenderCall(renderGroup);
				    facesMessages.add("Thank you, #{user.name}, bid of #{bid.bidValue} accepted.");
			        return true;
//		         }
//		         catch (SystemException se)
//		         {
//		        	 se.printStackTrace();
//		        
//		         }
//		    }

	    }catch (Exception e){
	    	log.info("OK....>>>>> SOMETHING WRONG!!!");
	    	e.printStackTrace();    
	    }
	    
	    return false;
	}
	
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void makeRenderCall(String renderGroup){
    	SeamUtilities.setCid(Manager.instance().getCurrentConversationId());
    	renderManager.getOnDemandRenderer(renderGroup).requestRender();
    	log.info("at end of makeRenderCall & cid="+Manager.instance().getCurrentConversationId());
    }
    

    public void cancel(){
    	log.info("action is cancelled");
    }

    @Remove
	public void destroy() {
		// TODO Auto-generated method stub
		log.info("destroying....");
	}
    
    public double getBidInput() {
        return bidInput;
    }

    public void setBidInput(double bidInput) {
        this.bidInput = bidInput;
    }
}
