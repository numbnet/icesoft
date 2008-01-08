package com.icesoft.ejb;



import com.icesoft.eb.AuctionitemBean;


public interface IBidAction {
	
   public boolean bid(AuctionitemBean selectItem);
   public void destroy();
   public void cancel();
   
   public void setBidInput(double bidInput);
   public double getBidInput();
}
