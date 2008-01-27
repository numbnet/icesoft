package com.icesoft.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
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
 * If the IntervalRenderer is to be implemented it would be done in
 * this class.  Also, if we want to add new items or remove them
 * from the view, it would be done here as well--to be implemented 
 * later.
*/
@Synchronized(timeout=1000)
@Stateful
@Startup
@Scope(ScopeType.APPLICATION)
@Name("auctionList")
 public class AuctionHouse implements IAuctionHouse{

	
	@In
	private transient EntityManager entityManager;
	
	@Logger 
	private Log log;
	
	@In
 	private transient RenderManager renderManager = null;
	
    private static List auctionitemList = new ArrayList();
    
    private List<Auctionitem> auctionitems;
  
    private boolean initList = false;
    
  
    
    /** have to create the list
     * 
     */
    @Create
    public void createList(){

		List resultList = entityManager.createQuery("SELECT i FROM Auctionitem i")
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
//				 items.add(renderer);
				 this.auctionitemList.add(sItem);
			 }
	 	  }
	 	  else log.info(" No items available for auction!!!");
		}	 	
    }
    
 
    @Destroy @Remove
    public void destroy() {
    	//need to remove all the renderGroups from the list
    	//shouldn't RenderManager do this???
    	log.info("Application finished!!  Removing Render Groups from RenderManager");
     	for (Object o: auctionitemList){
    		renderManager.getOnDemandRenderer((String)o).dispose();
    	}
    }

}
