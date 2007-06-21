package com.icesoft.eb;

import java.io.Serializable;

import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.Destroy;

import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;

/**
 * This class is the UI representation of an Auctionitem with the most recent Bid.
 * It updates the UI with a render call to renderables in its OnDemandRenderer group.
*/ 
public class AuctionitemBean implements AuctionItemB, Serializable{

    private RenderManager renderManager;
    private Auctionitem auctionitem;
    private Bid bid;
    public OnDemandRenderer renderer;
    private Effect bidEffect;
    private boolean bidding = false;
    private double bidInput = 0.0;
    private boolean expanded = false;
    private static final String STYLE_CLASS_EXPANDED_ROW = "rowClassHilite";
    private static final String TRIANGLE_OPEN = "img/triangle_open.gif";
    private static final String TRIANGLE_CLOSED = "img/triangle_close.gif";
    
    public AuctionitemBean(Auctionitem auctionitem, Bid bid, RenderManager renderManager){
        this.auctionitem = auctionitem;
        this.bid = bid;
        this.renderManager = renderManager;
        auctionitem.setBidCount(auctionitem.getBids().size());
        if(renderer == null){
            System.out.println("INSTANTIATING RENDER GROUP FOR ITEM");
            renderer = renderManager.getOnDemandRenderer( Long.toString(auctionitem.getItemId()) );            
        }
    }

    public Auctionitem getAuctionitem() {
        return auctionitem;
    }

    public void setAuctionitem(Auctionitem auctionitem) {
        this.auctionitem = auctionitem;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }
    
    public void render(){
        System.out.println("CALLING RENDER FROM ITEM");
        renderer.requestRender();
    }
    
    
    public void addRenderable(Renderable renderable){
        renderer.add(renderable);
    }
    
    public void removeRenderable(Renderable renderable){
        renderer.remove(renderable);        
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
    
    public double getBidInput() {
        return bidInput;
    }

    public void setBidInput(double bidInput) {
        this.bidInput = bidInput;
    }

    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
    
    public String getExpandedStyleClass() {
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
    
    @Destroy @Remove
    public void destroy() {
        if(renderer != null){
            renderer.requestStop();
        }
    }

}
