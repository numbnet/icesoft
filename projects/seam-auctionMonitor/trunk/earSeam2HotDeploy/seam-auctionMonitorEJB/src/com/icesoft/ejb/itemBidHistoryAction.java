package com.icesoft.ejb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
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
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;

import org.jboss.seam.log.Log;

import com.icesoft.eb.Auctionitem;
import com.icesoft.eb.AuctionitemBean;
import com.icesoft.eb.Bid;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

@Stateless
@Name("itemHistory")
@Scope(ScopeType.EVENT)
public class itemBidHistoryAction extends SortableList implements itemBidHistory {
 //   @PersistenceContext(type=EXTENDED)
	@PersistenceContext
    private EntityManager em;
    
    @In(required=false)
    private AuctionitemBean auctionitemBean;
        
    @Logger
    private Log log;
    
    private boolean expanded = false;
    private static final String STYLE_CLASS_EXPANDED_ROW = "rowClassHilite";
    private static final String TRIANGLE_OPEN = "img/triangle_open.gif";
    private static final String TRIANGLE_CLOSED = "img/triangle_close.gif";
    // sort column names
    private static String userColumnName = "User";
    private static String bidItemColumnName = "Item";
    private static String bidColumnName = "Bid";
    private static String timestampColumnName = "Time";
    // bidComparator used to sort queues.
    private Comparator bidComparator;
    
    private List<Bid> returnedHistory;
 
    public itemBidHistoryAction(){
        // default sort header
        super(bidColumnName);
        System.out.println("BidHistory constructor version="+this);
   //     log.info("constructor");
    }


    public String selectItem(AuctionitemBean selectItem)
    {
    	expanded = !expanded;
    	auctionitemBean = selectItem;
       	System.out.println("BID HISTORY & expanded is now "+expanded+" for item="+selectItem.getAuctionitem().getTitle());
    	if (this.auctionitemBean !=null){
	    	log.info("selectItem for auctionitemBean="+auctionitemBean.getAuctionitem().getTitle());
	       try{
	           Auctionitem temp = selectItem.getAuctionitem();
	           temp = em.merge(selectItem.getAuctionitem());
	           auctionitemBean = selectItem;
	           createItemBidHistory();
	       }catch(Exception e){
	           e.printStackTrace();
	       }
    	}
    	else log.info("selectedItem is null");
       return "";
    }

    public void createItemBidHistory(){
       	if (this.auctionitemBean !=null){
    		log.info("getting history for item ="+auctionitemBean.getAuctionitem().getTitle());
    		sort(getSort(), isAscending());
    		List <Bid> tempHistory = auctionitemBean.getAuctionitem().getBids();
//    		List<Bid> tempHistory = em.createQuery("from Bid b where b.id=:itemId order by b.timestamp desc")
//    	           .setParameter("itemId", auctionitemBean.getAuctionitem().getItemId() )
//    	           .getResultList();
    		log.info("size of list for item history is "+tempHistory.size());
    		this.returnedHistory = tempHistory;
       	}
       	else log.info("auctionitemBean is null");
    	
    }
    
    public List<Bid> getItemBidHistory() {
    	if (this.returnedHistory !=null)return returnedHistory;
    	else return new ArrayList<Bid>();
//    	if (this.auctionitemBean !=null){
//    		log.info("getting history for item ="+auctionitemBean.getAuctionitem().getTitle());
//    		sort(getSort(), isAscending());
//    		List<Bid> tempHistory = auctionitemBean.getAuctionitem().getBids();
//    		log.info("size of list for item history is "+tempHistory.size());
//    		return auctionitemBean.getAuctionitem().getBids();
//    	}else {
//    		log.info("auctionitemBean is null");
//    		return new ArrayList<Bid>(); //return empty list if no item selected
//    	}
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        bidComparator = new Comparator(){
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

    Collections.sort(auctionitemBean.getAuctionitem().getBids(), bidComparator);
        
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
    
 //   @Destroy @Remove
    @Remove
    public void destroy() {
        cleanup();
    }

    public void cleanup(){
    	log.info("cleanup()");
    }

	
	public String getCurrentTime(){
	    Date a = new Date(System.currentTimeMillis());
	    if (a!=null) {log.info("date is "+a.toString());
		  return a.toString();
	    }
		  else return "no time available";
	}
	   public boolean isExpanded() { return expanded; }
	    public void setExpanded(boolean expanded) { this.expanded = expanded; }
	    
	    public String getExpandedStyleClass() {
	    	System.out.println("getExpandedStyleClass");
	        if (expanded) {
	            return STYLE_CLASS_EXPANDED_ROW;
	        } else {
	            return "";
	        }
	    }
	    
	    public void pressExpandButton(AuctionitemBean auctionitemBean){
//	    	System.out.println("pressExpandButton & expanded="+expanded);\
	    	this.auctionitemBean = auctionitemBean;
	        expanded = !expanded;
	    }
	    
	    public String getExpandTriangleImage() {
	    	System.out.println("getExpandTriangleImage");
	        if (expanded) {
	            return TRIANGLE_OPEN;
	        } else {
	            return TRIANGLE_CLOSED;
	        }
	    }

}
