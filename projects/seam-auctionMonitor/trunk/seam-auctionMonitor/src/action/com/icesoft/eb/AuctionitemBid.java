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
    
    public String getUserColumnName();
    public String getBidItemColumnName();
    public String getBidColumnName();
    public String getTimestampColumnName();
    
    public String getSort() ;
    public void setSort(String sort);

    public boolean isAscending();
    public void setAscending(boolean ascending);

}
