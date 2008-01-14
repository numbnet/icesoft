package com.icesoft.ejb;

import java.util.List;

import javax.ejb.Local;

import com.icesoft.faces.async.render.OnDemandRenderer;


@Local
public interface IAuctionHouse {
    
//	public List<OnDemandRenderer> getItems();
	public void addItem(String createdItem);
	public void removeItem(String deletedItem);
    public void createList();
    public void destroy();  
    
}
