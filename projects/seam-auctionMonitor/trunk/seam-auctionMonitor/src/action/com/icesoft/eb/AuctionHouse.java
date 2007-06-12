package com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface AuctionHouse {

    public void itemCreated(AuctionitemBean itemBean);
    public void itemExpired(AuctionitemBean itemBean);
    public void destroy();
}
