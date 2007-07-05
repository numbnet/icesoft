package com.icesoft.eb;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AuctionHouse {
    
    public void itemCreated(Auctionitem createdItem);
    public void itemDeleted(Auctionitem deletedItem);
    
    public void destroy();
    
    public List getAuctionitemList();
    public void setAuctionitemList(List auctionitemList);
    
    public boolean isAuctionitemListExists();
    public void setAuctionitemListExists(boolean auctionitemListExists);
    
    
}
