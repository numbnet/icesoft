package com.icesoft.ejb;



import javax.ejb.Local;

import com.icesoft.eb.AuctionitemBean;

@Local
public interface IBidAction {
	
   public void bid(AuctionitemBean selectItem);
   public void destroy();
   public void cancel();
   

}
