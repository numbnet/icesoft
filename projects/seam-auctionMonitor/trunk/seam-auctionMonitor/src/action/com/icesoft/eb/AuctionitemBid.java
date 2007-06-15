package com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface AuctionitemBid {

    public String selectItem(AuctionitemBean selectedItem);
    public String bid();
    public String cancel();
    public void destroy();
    
}
