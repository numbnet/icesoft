package com.icesoft.ejb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Init;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Scope;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.core.Manager;
import static org.jboss.seam.ScopeType.EVENT;

import com.icesoft.eb.Auctionitem;
import com.icesoft.eb.AuctionitemBean;
import com.icesoft.eb.Bid;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

@Synchronized(timeout=1000)
@Stateful
@Name("auctionView")
@Scope(ScopeType.CONVERSATION)
public class View implements IView{
//public class View implements Renderable, Serializable{

	private static final long serialVersionUID = 3770544843122643358L;
	private boolean setup = false;
	private boolean gotSearch= false;
	
	@PersistenceContext
//	@In(create=true)
 	private EntityManager em;
	
	@In
	private IViewManager viewManager;
    
    @RequestParameter
    private String searchString;
    
	private int pageSize=5;
	
 	private String cid;
	
	private String pattern;
	private Integer page=0;

	@In
	private FacesMessages facesMessages;
	
    
//	@DataModel(scope=ScopeType.PAGE)
//	@DataModel
	private List<AuctionitemBean> searchItems=null;


	@In(required=false)
	@Out(required=false)
	private AuctionitemBean auctionitemBean;
	
    private PersistentFacesState state = PersistentFacesState.getInstance();
	
	@In
	private transient RenderManager renderManager = null;

	@Logger 
	private Log log;
	
 	@Create
	public void startUp(){
 		log.debug("Create " + getClass().getName());
 		state = PersistentFacesState.getInstance();		
		log.info(" LR conversation is "+Manager.instance().isLongRunningConversation());
		if (!Manager.instance().isLongRunningConversation())
			Manager.instance().beginConversation();
		log.info(" LR conversation is "+Manager.instance().isLongRunningConversation());

		if (searchString!=null){
			log.info("startUp() View version="+this);
			setPattern();
		}else {
			log.info("searchString is null so");
		}		
		gotSearch=true;
	}
	
	
	public String getSearchString() {
		return searchString;
	}

	
	public AuctionitemBean getAuctionitemBean(){
		log.info("getgin auctionitemBean version="+this.auctionitemBean.toString());
		return this.auctionitemBean;
	}
	
	@Factory(scope=ScopeType.STATELESS)
 	public List<AuctionitemBean> getSearchItems() {
 		log.info("getting searchItems & updating PFS");
 		if (searchItems==null || searchItems.isEmpty()){
 			log.info("%%having to rebuild searchList :-"+searchItems);
 			loadList();
 //			searchItems = viewManager.getAuctionItemsList();
 		}
 		else {
 			log.info("\t%% don't have to rebuild list :-"+searchItems);
 			loadList();
 //			searchItems = viewManager.getAuctionItemsList();
 		}
		state=updatePFS();
     log.info("get list of items size="+searchItems.size()+" version="+this);
		return searchItems;
	}

    /**
     * when we get this list (event-scoped) we really only have to get the auctionitem once
     * as this data doesn't change at all.  The table with the updated info is the bid
     * table.  Look at this method later to possibly refactor.
     */
//	@Factory(value="searchItems", scope=ScopeType.EVENT)
//	@Factory(value="searchItems")
	public void loadList(){
		if (!gotSearch)startUp();
	    log.info("!!!!!!!!!!!!!!!!QUERYING!!!!!!!!!!!!!!! SEARCHSTRING: " + searchString);
		searchItems = new ArrayList();       
		List resultList = em.createQuery("SELECT i, b FROM Auctionitem i LEFT JOIN i.bids b" +
		            " WHERE (i.bids IS EMPTY OR b.timestamp = (SELECT MAX(b1.timestamp) FROM i.bids b1))" +
		            " AND (lower(i.currency) like #{pattern} or lower(i.description) like #{pattern}" +
		            " or lower(i.imageFile) like #{pattern} or lower(i.location) like #{pattern} or lower(i.seller) like #{pattern}" +
		            " or lower(i.site) like #{pattern} or lower(i.title) like #{pattern})")
	                .setMaxResults(pageSize)
	                .setFirstResult( page * pageSize )
		            .getResultList();
		 log.info("resultList has size="+resultList.size());
		 Object[] oa;
   /* should I bother adding the item if it is expired??? */
		 for (Object o : resultList) {
		    log.info("loading up the list");
		    oa = (Object[]) o;
		    AuctionitemBean auctionitemBean = new AuctionitemBean((Auctionitem) oa[0], (Bid) oa[1]);
		    if (!setup){
		      	 String renderGroup = Long.toString(auctionitemBean.getAuctionitem().getItemId());
			     log.info("addomg view to renderGroup ="+renderGroup);
		       	 renderManager.getOnDemandRenderer(renderGroup).add(this);		  	
		    }
		    else log.info("View has already been setup");
		    log.info("adding item = "+auctionitemBean.getAuctionitem().getTitle());
		    searchItems.add(auctionitemBean);	
		 }
		 setup=true; 
		 log.info ("searchItems list is now of size="+searchItems.size());
	}	
	
	@Observer("bidUpdated")
	public void updateDataModel(String itemId) {
		log.info("Observer pattern for itemID="+itemId);
	  // update the list by hitting the database   // only if the item is in this list
	}
	
    @Destroy @Remove
	public void destroy() {
	   System.out.println("View: destroy");
		cleanup();
	}

	public int getPageSize() {
		return this.pageSize;
	}
	
	public int getPage(){return this.page;}

	public PersistentFacesState getState() {
		log.info("getState ");
		return state;
	}

	private PersistentFacesState updatePFS(){
 		PersistentFacesState state_1 = PersistentFacesState.getInstance();
 		if (state_1 !=null){
 			log.info("####   updating PFS");
 			state=state_1;
 		}
 		else log.info("####  PFS is null!!! using stale state");
 		return state;
	}
	
	public void renderingException(RenderingException renderingException) {
	      if (renderingException instanceof TransientRenderingException ){
	      }
	      else if(renderingException instanceof FatalRenderingException){
	    	  System.out.println("View: FatalRenderingException");
	    	  cleanup();

	      }
	}
	
	public void cleanup(){
		log.info("CLEANUP the renderable list");
		if (searchItems !=null){
	         for(int i=0; i<searchItems.size(); i++){
	            AuctionitemBean tempBean = ((AuctionitemBean)searchItems.get(i));
	            String renderGroup = Long.toString(tempBean.getAuctionitem().getItemId());
	        	log.info("removing this from auctionitem bean for item ="+tempBean.getAuctionitem().getTitle());
	        	try{
	        		renderManager.getOnDemandRenderer(renderGroup).remove(this);
	        	}catch (Exception e){
	        		log.info("can't remove this view from renderGroup="+renderGroup);
	        	}
	         }
         }
	}

	public void setPattern() {	
			pattern=getSearchPattern();
			log.info("View: pattern is "+pattern+" for searchString="+searchString);
	}
 	public void setSearchString(String sIn){this.searchString=sIn;}

	private String getSearchPattern()
	{
	   return searchString==null ?
	         "%" : '%' + searchString.toLowerCase().replace('*', '%') + '%';
	}
	@End
	public void removeView(){
		cleanup();
		log.info("after cleanup()");
	}
	
	public boolean haveString(){
		return !searchString.equals("");
	}

	public String getCid(){
		return "cid";
		//return Manager.instance().getCurrentConversationId();
	}
	public void setCid(String cIn){
		this.cid=cIn;
	}

}
