package com.icesoft.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.log.Log;

import com.icesoft.eb.Auctionitem;
import com.icesoft.eb.AuctionitemBean;
import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.async.render.RenderManager;

/**
 * A central repository of Auctionitems.
*/
@Synchronized(timeout=1000)
@Stateful
@Startup
@Scope(ScopeType.APPLICATION)
@Name("auctionList")
public class AuctionHouse implements IAuctionHouse{
	
	@PersistenceContext
	private EntityManager em;
	
	@Logger 
	private Log log;
	
	@In
	private transient RenderManager renderManager = null;
	
    private static List auctionitemList = new ArrayList();
    
    List<Auctionitem> auctionitems;
    //actually will use the id of the items (String) as renderGroups
    List<OnDemandRenderer> items = new ArrayList<OnDemandRenderer>();
    private boolean initList = false;
    
    /**
     * By using @Unwrap when "auctionList" is injected 
     * elsewhere, you get the List of auctionItems
     * returned from this method
     * Don't think I'll need this....
     * @return items
     */
 
    public List<OnDemandRenderer> getItems(){
    	if (items ==null){
    		//have to setup our list of renderers
    		createList();
    		initList=true;
    	}
    	return items;
    }
    
    /** have to create the list
     * 
     */
    @Create
    public void createList(){
		List resultList = em.createQuery("SELECT i FROM Auctionitem i")
	            .getResultList();
		log.info("resultList has size="+resultList.size());
		Object[] oa;
		if (renderManager==null) {
	 	   log.info("check to see why renderManager was not injected");
	 	   renderManager = (RenderManager)Component.getInstance("RenderManager");
		}
		else {
	 	   log.info("  renderManager is "+renderManager);
	 	   if (resultList.size() > 0){
			 for (Object o : resultList){
				 Auctionitem itemBean = (Auctionitem)o;
				 String sItem = Long.toString(itemBean.getItemId());
				 log.info("########ITEM added to list of renderGroups:- "+sItem);
				 OnDemandRenderer renderer = renderManager.getOnDemandRenderer(sItem);
				 items.add(renderer);
			 }
	 	  }
	 	  else log.info(" No items available for auction!!!");
		}	 	
    }
    
    @Observer("itemAdded")
    public void addItem(String createdItem){
        //allow users to add auction items 
    	//for the auction (eventually).
    }
    
    @Observer("itemExpired")
    public void removeItem(String deletedItem){
        //not sure what to do yet with items that have expired
    	//but once the lists have all been updated that
    	//the auction for this item has expired, then
    	//we don't need a render group for it any more
    }
    
    @Destroy @Remove
    public void destroy() {
    	//need to remove all the renderGroups from the list
    	for (Object o: items){
    		items.remove(o);
    	}
    	log.info ("in destroy & items should be = zero :-"+items.size());   	
    }

}
