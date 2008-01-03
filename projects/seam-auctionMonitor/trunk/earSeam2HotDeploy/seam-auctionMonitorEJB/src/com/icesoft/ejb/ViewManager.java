package com.icesoft.ejb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;
import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;

import com.icesoft.eb.Auctionitem;
import com.icesoft.eb.AuctionitemBean;
import com.icesoft.eb.Bid;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
/**
 * This class could be used to hold a collection of views (Renderables) for each User.
*/
@Stateful
@Name("viewManager")
@Scope(ScopeType.SESSION)
public class ViewManager implements IViewManager, Serializable{
    
	@Logger 
	private Log log;

	private String searchString=" ";
	
	private int pageSize=5;
	
	private Integer page=0;
	
	@In(value="renderManager")
	RenderManager renderManager;

	@DataModel
	private List<AuctionitemBean> auctionItemsList;
	
   @PersistenceContext(type=EXTENDED)
    EntityManager em;
   
   
   @Factory(value="auctionItems", scope = ScopeType.EVENT)
	public void loadList() {
	      log.info("!!!!!QUERYING!!!!! SEARCHSTRING: " + searchString);
	       auctionItemsList = new ArrayList();       
	       List resultList = em.createQuery("SELECT i, b FROM Auctionitem i LEFT JOIN i.bids b" +
	            " WHERE (i.bids IS EMPTY OR b.timestamp = (SELECT MAX(b1.timestamp) FROM i.bids b1))" +
	            " AND (lower(i.currency) like #{pattern} or lower(i.description) like #{pattern}" +
	            " or lower(i.imageFile) like #{pattern} or lower(i.location) like #{pattern} or lower(i.seller) like #{pattern}" +
	            " or lower(i.site) like #{pattern} or lower(i.title) like #{pattern})")
                .setMaxResults(pageSize)
                .setFirstResult( page * pageSize )
	            .getResultList();
	       Object[] oa;
	       for (Object o : resultList) {
	           oa = (Object[]) o;
	           AuctionitemBean auctionitemBean = new AuctionitemBean((Auctionitem) oa[0], (Bid) oa[1]);
	           auctionItemsList.add(auctionitemBean);	
	       }
	      log.info("size of auctionItems is "+auctionItemsList.size());
		//return auctionItemsList;
	}
	@Factory(value="pattern", scope = ScopeType.EVENT)
	public String getSearchPattern()
	{
	   return searchString==null ?
	         "%" : '%' + searchString.toLowerCase().replace('*', '%') + '%';
	}

	 public String getSearchString()
	 {
	    return searchString;
	}
	 
	
	public void updateList(ValueChangeEvent event) {
	     log.info("AUTOCOMPLETE UPDATING LIST!!!");
	      page = 0;
	      setSearchString( (String) event.getNewValue() );
	      if (renderManager==null)log.info(">>>!!!renderManager is NULL !!!");
	      else log.info(" !!!!RENDER MANAGER IS "+renderManager);
        loadList();
	}
	public boolean isNextPageAvailable() {
		return false;
	}

	  public List<AuctionitemBean> getAuctionItemsList(){
   //       sort(getSort(), isAscending());
		  if (searchString=="")loadList();
          return this.auctionItemsList;
  }

	public String nextPage() {
	      page++;
	      this.loadList();
	      return "";
	}

	public void setSearchString(String searchString)
	{
	   this.searchString = searchString;
	   log.info("looking for "+this.searchString);
	}
	   
    @Remove @Destroy
	public void destroy() {
		log.info("destroying session scoped bean");
		
	}
	public String getConvId() {
		return Manager.instance().getCurrentConversationId();
	}
	public String getLongRunning() {
		return String.valueOf(Manager.instance().isLongRunningConversation());
	}
	public int getPage() {
		return page;
	}
	public int getPageSize() {
		return pageSize;
	}

}
