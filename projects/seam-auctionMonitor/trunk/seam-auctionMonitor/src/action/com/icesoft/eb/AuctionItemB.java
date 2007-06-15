package com.icesoft.eb;

import javax.ejb.Local;

import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.context.effects.Effect;

@Local
public interface AuctionItemB {

    public Auctionitem getAuctionitem();
    public void setAuctionitem(Auctionitem auctionitem);
    public Bid getBid();
    public void setBid(Bid bid);
    public void render();
    public void addRenderable(Renderable renderable);
    public void removeRenderable(Renderable renderable);
    public Effect getBidEffect() ;
    public void setBidEffect(Effect bidEffect);
    public void buildBidEffect();
    public boolean isBidding();
    public void setBidding(boolean bidding);
    
    public double getBidInput();
    public void setBidInput(double bidInput);
}
