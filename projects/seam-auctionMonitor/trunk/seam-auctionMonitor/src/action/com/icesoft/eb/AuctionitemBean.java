package com.icesoft.eb;

import java.io.Serializable;

import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.ScopeType;

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
    private OnDemandRenderer renderer;
    private Effect bidEffect;
    private boolean bidding = false;
    private double bidInput = 0.0;
    
    public AuctionitemBean(Auctionitem auctionitem, Bid bid, RenderManager renderManager){
        this.auctionitem = auctionitem;
        this.bid = bid;
        this.renderManager = renderManager;
        auctionitem.setBidCount(auctionitem.getBids().size());
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
        if(renderer == null){
            renderer = renderManager.getOnDemandRenderer( Long.toString(auctionitem.getItemId()) );            
        }
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
    
    @Destroy @Remove
    public void destroy() {
        if(renderer != null){
            renderer.requestStop();
        }
    }

}
