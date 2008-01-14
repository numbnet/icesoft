package com.icesoft.ejb;



import com.icesoft.eb.AuctionitemBean;


public interface IBidAction {
	
   public void bid(AuctionitemBean selectItem);
   public void destroy();
   public void cancel();
   

}
