package com.icesoft.ejb;

import java.util.List;

import javax.ejb.Local;

import com.icesoft.eb.AuctionitemBean;
import com.icesoft.eb.Bid;


@Local
public interface itemBidHistory {

    public String selectItem(AuctionitemBean selectedItem);
    public void destroy();
    public List<Bid> getItemBidHistory();

    // ice:commandSortHeader methods
    public String getUserColumnName();
    public String getBidItemColumnName();
    public String getBidColumnName();
    public String getTimestampColumnName();    
    public String getSort() ;
    public void setSort(String sort);
    public boolean isAscending();
    public void setAscending(boolean ascending);
    public String getCurrentTime();
    
    public String getExpandTriangleImage();
    public void pressExpandButton(AuctionitemBean auctionitemBean);
	   public boolean isExpanded();
	    public void setExpanded(boolean expanded);
}
