package com.icesoft.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.eb.AuctionitemBean;

@Local
public interface IViewManager {
	//profile settings
	public int getPage();
	public int getPageSize();
	
	
	public String getSearchString();
	public void setSearchString(String sIn);
	public void destroy();
	
	public String getSearchPattern();
	public void loadList();
	public void updateList(ValueChangeEvent event);
	public List<AuctionitemBean> getAuctionItemsList();
	
	public boolean isNextPageAvailable();
	public String nextPage();
	
	//profile settings
	
	public String getConvId();
	public String getLongRunning();

}
