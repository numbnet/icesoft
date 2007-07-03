package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
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

import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

@Stateful
@Name("itemBid")
@Restrict("#{identity.loggedIn}")

public class AuctionitemBidAction extends SortableList implements AuctionitemBid, Renderable {
    @PersistenceContext(type=EXTENDED)
    private EntityManager em;
    
    @In 
    private User user;
    
    @In(create=true, required=false) @Out
    private AuctionitemBean auctionitemBean;
    
    @In(required=false) 
    @Out(required=false)
    private Bid bid;
    
    @Logger
    private Log log;
    
//    @In
//    ViewManagerAction viewManager;

    private PersistentFacesState state = PersistentFacesState.getInstance();

    @In(required = false, scope = ScopeType.APPLICATION)
    @Out(required = false, scope = ScopeType.APPLICATION)
    private List<AuctionitemBean> globalAuctionItems;
    
    // sort column names
    private static String userColumnName = "User";
    private static String bidItemColumnName = "Item";
    private static String bidColumnName = "Bid";
    private static String timestampColumnName = "Time";
    // comparator used to sort queues.
    private Comparator comparator;
    
    public AuctionitemBidAction(){
        super(bidColumnName);
    }

    @Begin(join=true)
    public String selectItem(AuctionitemBean selectedItem)
    {
       Auctionitem temp = auctionitemBean.getAuctionitem();
       temp = em.merge(selectedItem.getAuctionitem());
       auctionitemBean = selectedItem;
       auctionitemBean.setBidding(true);
       bid = new Bid(temp, user);
       bid.setBidValue(auctionitemBean.getBidInput());
       auctionitemBean.addRenderable(this);
       return "";
    }
    
    @End(ifOutcome={"success"})
    public String bid()
    {
        if ( auctionitemBean.getBidInput() <= auctionitemBean.getBid().getBidValue() )
        {
           FacesMessages.instance().add("Bid must be higher than existing price");
           return "";
        }
        else if ( auctionitemBean.getBidInput() > 999999 )
        {
           FacesMessages.instance().add("Bid must be less than $1,000,000");
           return "";
        }        
       
       bid.setBidValue(auctionitemBean.getBidInput());
       Calendar calendar = Calendar.getInstance();
       bid.setTimestamp( calendar.getTime() );
       bid.setCreditCard("1234123412341234");
       bid.setCreditCardName("American Express");
       System.out.println("PERSISTING BID");
       em.persist(bid);
       //FacesMessages.instance().add("Thank you, #{user.name}, bid of #{bid.bidValue} accepted.");
       log.info("New bid on: #{bid.id} of #{bid.bidValue} for #{user.username}");
       auctionitemBean.setBid(bid);
       //auctionitemBean.render();
            AuctionitemBean tempBean = null;
            for (AuctionitemBean globalAuctionItem : globalAuctionItems) {
                System.out.println("GLOBAL AUCTION ITEM: " + globalAuctionItem.getAuctionitem().getDescription() + globalAuctionItem.getAuctionitem().getItemId());
                if (globalAuctionItem.getAuctionitem().getItemId() == auctionitemBean.getAuctionitem().getItemId()) {
                    System.out.println("globalAuctionItem = " + globalAuctionItem);
                    globalAuctionItem.setBid(bid);
                    globalAuctionItem.buildBidEffect();
                    globalAuctionItem.getAuctionitem().getBids().add(bid);
                    int newBidCount = globalAuctionItem.getAuctionitem().getBidCount() + 1;
                    globalAuctionItem.getAuctionitem().setBidCount(newBidCount);
                    tempBean = globalAuctionItem;
                }
            }
            if (tempBean != null) {
                System.out.println("globalAuctionItem = " + tempBean);
                System.out.println("globalAuctionItem.getBid().getBidValue() = " + tempBean.getBid().getBidValue());
                System.out.println("globalAuctionItem.getAuctionitem().getBidCount() = " + tempBean.getAuctionitem().getBidCount());
                System.out.println("CALLING RENDER");
                tempBean.render();
            }
       return "success";
    }
    
    @End
    public void cancel() {
        auctionitemBean.setBidInput(0.0);
        auctionitemBean.setBidding(false);
    }
    
    @Destroy @Remove
    public void destroy() {
        auctionitemBean.removeRenderable(this);
    }

    public PersistentFacesState getState() {
        return state;
    }

    /**
     * Callback method that is called if any exception occurs during an attempt
     * to render this Renderable.
     *
     * @param renderingException The exception that occurred when attempting
     * to render this Renderable.
     */
    public void renderingException(RenderingException renderingException) {

        if (renderingException instanceof TransientRenderingException ){

        }
        else if(renderingException instanceof FatalRenderingException){
            auctionitemBean.removeRenderable(this);
        }
    }

    public List<Bid> getAuctionitemBidList() {
        state = PersistentFacesState.getInstance();
        sort(getSort(), isAscending());
        return auctionitemBean.getAuctionitem().getBids();
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }

    protected void sort(final String column, final boolean ascending) {
        comparator = new Comparator(){
            public int compare(Object o1, Object o2) {
                Bid c1 = (Bid) o1;
                Bid c2 = (Bid) o2;
                if (column == null) {
                    return 0;
                }
                else if (column.equals(userColumnName)) {
                    return ascending ?
                            c1.getUser().getName().toLowerCase().compareTo( c2.getUser().getName().toLowerCase() ):
                            c2.getUser().getName().toLowerCase().compareTo( c1.getUser().getName().toLowerCase() );
                }
                else if (column.equals(bidItemColumnName)) {
                    return ascending ?
                            c1.getAuctionItem().getTitle().toLowerCase().compareTo( c2.getAuctionItem().getTitle().toLowerCase() ):
                            c2.getAuctionItem().getTitle().toLowerCase().compareTo( c1.getAuctionItem().getTitle().toLowerCase() );
                }
                else if (column.equals(bidColumnName)) {
                    return ascending ?
                            new Double(c1.getBidValue()).compareTo( new Double(c2.getBidValue()) ):
                            new Double(c2.getBidValue()).compareTo( new Double(c1.getBidValue()) );
                }
                else if (column.equals(timestampColumnName)) {
                    return ascending ?
                            c1.getTimestamp().compareTo(c2.getTimestamp()):
                            c2.getTimestamp().compareTo(c1.getTimestamp());
                }
                else return 0;
            }
        };

    Collections.sort(auctionitemBean.getAuctionitem().getBids(), comparator);
        
    }
    
    public String getUserColumnName() {
        return userColumnName;
    }

    public String getBidItemColumnName() {
        return bidItemColumnName;
    }

    public String getBidColumnName() {
        return bidColumnName;
    }

    public String getTimestampColumnName() {
        return timestampColumnName;
    }

}
