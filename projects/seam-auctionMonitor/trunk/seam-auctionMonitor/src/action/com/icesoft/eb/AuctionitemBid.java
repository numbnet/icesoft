package com.icesoft.eb;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AuctionitemBid {

    public String selectItem(AuctionitemBean selectedItem);
    public String bid();
    public void cancel();
    public void destroy();
    public List<Bid> getAuctionitemBidList();

}
