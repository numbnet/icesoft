package com.icesoft.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import com.icesoft.eb.Auctionitem;
import com.icesoft.eb.AuctionitemBean;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.context.effects.Effect;

@Local
public interface IViewManager {
	//profile settings
	public int getPage();
	public int getPageSize();
	
	//getters & setters
	public String getSearchString();
	public void setSearchString(String sIn);
	public String getItem1String();
	public String getItem2String();
	public String getItem3String();
	public void setItem1String(String item1String);
	public void setItem2String(String item2String);
	public void setItem3String(String item3String);
	public List<Auctionitem> getViewList();
	
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
	
	//for drag & drop
	public void iconListener(DragEvent event);
	public void remove(Auctionitem item);
	public void add(AuctionitemBean item);
	public void clearView(ActionEvent event);
}
