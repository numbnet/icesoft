package com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface AuctionitemBid {

    public String selectItem(AuctionitemBean selectedItem);
    public void bid();
    public void setBidDetails();
    public boolean isBidValid();
    public void confirm();
    public void cancel();
    public void destroy();
    
}
