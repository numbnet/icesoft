package com.icesoft.eb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Remove;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;


import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.Component;

import com.icesoft.ejb.IBidAction;
import com.icesoft.ejb.IViewManager;
import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;


/**
 * This class is the UI representation of an Auctionitem with the most recent Bid.
 * 
*/ 

@Name("itemBean")
public class AuctionitemBean implements Serializable{	
	
	@Out(required=false)
    private Auctionitem auctionitem;
    
    @Out(required=false)
    private Bid bid;
    
    @In
    private User user;
    
    private Effect bidEffect;
    private boolean bidding = false;
    private double bidInput = 0.0;
    private double minBid = 0.0;
    private boolean expanded = false;
    private static final String STYLE_CLASS_EXPANDED_ROW = "rowClassHilite";
    private static final String TRIANGLE_OPEN = "img/triangle_open.gif";
    private static final String TRIANGLE_CLOSED = "img/triangle_close.gif";
  	
    /* time for auction stuff */
    private boolean expired = false;
    private String expiredString="not yet";
    
    private String price="none";
    private String title="none";
    private boolean owner = false;
    
    private String timeLeftString="none";
    private static final int TIME_DAYS = 24 * 60 * 60 * 1000;
    private static final int TIME_HOURS = 60 * 60 * 1000;
    private static final int TIME_MINUTES = 60 * 1000;
    private static final String NO_PHOTO_URL = "./img/noimage.gif";
    private static final String TIME_LEFT_5 = "./img/time_left_5.gif";
    private static final String TIME_LEFT_10 = "./img/time_left_10.gif";
    private static final String TIME_LEFT_15 = "./img/time_left_15.gif";
    private static final String TIME_LEFT_30 = "./img/time_left_30.gif";
    private static final String TIME_LEFT_60 = "./img/time_left_45.gif";

    private static final String TIME_LEFT_DAYS = "./images/time_left_days.gif";
    private static final String TIME_LEFT_HOURS =
            "./images/time_left_hours.gif";

    private long[] timeLeftBrokenDown;
    private String timeLeftStyleClass;

    
    public AuctionitemBean(User user, Auctionitem auctionitem, Bid bid)  {
        this.user = user;
        this.auctionitem = auctionitem;
        this.bid = bid;
        auctionitem.setBidCount(auctionitem.getBids().size());
        this.bidInput = this.getMinBid();
    }
    

    public Auctionitem getAuctionitem() {
//    	System.out.println("AUB: - getting auction item = "+auctionitem.getTitle()+" version="+auctionitem.toString());
        return auctionitem;
    }

    public void setAuctionitem(Auctionitem auctionitem) {
//    	System.out.println("AUG: setAuction item version="+this);
    	this.auctionitem = auctionitem;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }
    
    public Effect getBidEffect(){
        return bidEffect;
    }

    public void setBidEffect(Effect bidEffect) {
        this.bidEffect = bidEffect;
    }
    
    public void buildBidEffect(){
        bidEffect = new Appear();
        bidEffect.setDuration(.5f);
    }

    public boolean isBidding() {
        return bidding;
    }

    public void setBidding(boolean bidding) {
        this.bidding = bidding;
    }
    


    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { 
    	this.expanded = expanded; 
    }
    
    public String getExpandedStyleClass() {
  //  	System.out.println("getExpandedStyleClass");
        if (expanded) {
            return STYLE_CLASS_EXPANDED_ROW;
        } else {
            return "";
        }
    }
    
    public void pressExpandButton(ActionEvent e){  
        expanded = !expanded;
    }
    
    public String getExpandTriangleImage() {
        if (expanded) {
            return TRIANGLE_OPEN;
        } else {
            return TRIANGLE_CLOSED;
        }
    }
    
    public boolean isExpired(){
       	Date endTimeCal = auctionitem.getExpires();
       	long endMillis = endTimeCal.getTime();
    	long millis = endMillis - Calendar.getInstance().getTime().getTime();
    	timeLeftString=(new Date(millis)).toString();
        if(!expired){
            Date current = new Date();
            if(current.after(auctionitem.getExpires())){
                expired = true;
                return expired;
            }else{
                return expired;
            }
        }else{
            return expired;
        }
    }
 
    public String getTimeLeftStyleClass() {
        return timeLeftStyleClass;
    }

    public long getTimeLeft(){
      	Date endTimeCal = auctionitem.getExpires();
       	long endMillis = endTimeCal.getTime();
    	long millis = endMillis - Calendar.getInstance().getTime().getTime();
    	return millis;
    }
    
    public long[] getTimeLeftBrokenDown() {
        long left, days, hours, minutes, seconds;
        left = getTimeLeft();
        days = left / TIME_DAYS;
        left = left - days * TIME_DAYS;
        hours = left / TIME_HOURS;
        left = left - hours * TIME_HOURS;
        minutes = left / TIME_MINUTES;
        left = left - minutes * TIME_MINUTES;
        seconds = left / 1000;
        return new long[]{days, hours, minutes, seconds};
    }

    public String getTimeImageUrl() {
        timeLeftBrokenDown = getTimeLeftBrokenDown();
        String timeImageUrl;
        if (0 != timeLeftBrokenDown[0]) {
            timeImageUrl = TIME_LEFT_DAYS;
            timeLeftStyleClass = "timeCellDays";
        } else if (0 != timeLeftBrokenDown[1]) {
            timeImageUrl = TIME_LEFT_HOURS;
            timeLeftStyleClass = "timeCellHours";
        } else if (timeLeftBrokenDown[2] >= 30) {
            timeImageUrl = TIME_LEFT_60;
            timeLeftStyleClass = "timeCellMins";
        } else if (timeLeftBrokenDown[2] >= 15) {
            timeImageUrl = TIME_LEFT_30;
            timeLeftStyleClass = "timeCellMins";
        } else if (timeLeftBrokenDown[2] >= 10) {
            timeImageUrl = TIME_LEFT_15;
            timeLeftStyleClass = "timeCellMins";
        } else if (timeLeftBrokenDown[2] >= 5) {
            timeImageUrl = TIME_LEFT_10;
            timeLeftStyleClass = "timeCellMins";
        } else {
            timeImageUrl = TIME_LEFT_5;
            timeLeftStyleClass = "timeCellMins";
        }
        return timeImageUrl;
    }
    
    public void setTimeLeftString(String timeLeftString) {
    }

    public String getTimeLeftString() {
        if (getTimeLeft() < 0) {
            return " Expired";
        }
        //put this here for now
        getTimeImageUrl();
        StringBuffer buf = new StringBuffer();
        buf.append("  ");
        if (0 != timeLeftBrokenDown[0]) {
            buf.append(Long.toString(timeLeftBrokenDown[0]));
            buf.append("d ");
        }

        if (0 != timeLeftBrokenDown[1]) {
            buf.append(Long.toString(timeLeftBrokenDown[1]));
            buf.append(":");
            if (timeLeftBrokenDown[2] < 10) {
                buf.append("0");
            }
        }

        buf.append(Long.toString(timeLeftBrokenDown[2]));
        buf.append(":");

        if (timeLeftBrokenDown[3] < 10) {
            buf.append("0");
        }

        buf.append(Long.toString(timeLeftBrokenDown[3]));

        return buf.toString();
    }

    public String getExpiredString(){
    	return this.auctionitem.getExpires().toString();
    }
    
    public Double getMinBid(){
    	 return bid.getBidValue() + auctionitem.getPrice();
    }
    public String getPrice(){
    	return String.valueOf(auctionitem.getPrice());
    }
    public String getTitle(){
    	return auctionitem.getTitle();
    }
    
    
    public void setExpired(boolean expired){
        this.expired = expired;
    }
    
    public String getExpiredStyleClass(){
        if(!expired){
            return "";
        }else{
            return "expiredStyleClass";
        }
    }
    public double getBidInput() {
        return this.getMinBid();
    }

    public void setBidInput(double bidInput) {
        this.bidInput = bidInput;
    }  
    /**
     * The users will want to know if they are the current "owner" of the item
     * or whether they should bid again.
     * 
     * @return
     */
    public boolean isOwner(){
    	
    	if (!this.auctionitem.getBids().isEmpty() || auctionitem.getBids()!=null){
    		//have bids on this item....see whose is the highest.
    		Bid bid = getMaxBid();
    		if (bid!=null){
	    		 if (bid.getUser().getUsername().equals(user.getUsername()))owner=true;
	    		
    		}
    	}
    	else owner=false;
    	return this.owner;
    }
    
    /**
     * see what the maximum amount bid on this item currently is
     * @return
     */
    private Bid getMaxBid(){
    	Bid b = null;
    	
    	if (!auctionitem.getBids().isEmpty()){
    		b = auctionitem.getBids().get(0);
    		for (int i=1; i<auctionitem.getBids().size(); i++){   		
    			if (auctionitem.getBids().get(i).getBidValue() > b.getBidValue())
    				b=auctionitem.getBids().get(i);
    		}
    	}
    	return b;
    }
    
    @Destroy @Remove
    public void destroy() {
        System.out.println("destroying.....");
    }
   
 
}
