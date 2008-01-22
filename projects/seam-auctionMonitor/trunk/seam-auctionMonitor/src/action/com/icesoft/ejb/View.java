package com.icesoft.ejb;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;

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
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Conversation;

import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.datamodel.DataModel;

import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.core.Manager;


import com.icesoft.eb.Auctionitem;
import com.icesoft.eb.AuctionitemBean;
import com.icesoft.eb.Bid;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

 //@Synchronized(timeout=1000)
@Stateful
@Name("auctionView")
@Scope(ScopeType.CONVERSATION)
public class View implements IView{
//public class View implements Renderable, Serializable{

	private static final long serialVersionUID = 3770544843122643358L;
	private boolean setup = false;
	private boolean gotViewName= false;
	
  @PersistenceContext
 	private EntityManager em;
	
    
    @RequestParameter
    private String item1String;
//    private String searchString;
    
    @RequestParameter
    private String item2String;   
    @RequestParameter
    private String item3String;   
       
    private String viewName="View of ";
    
	private int pageSize=5;
	
 	private String cid;
	
	private long pattern1=-1,pattern2=-1,pattern3=-1;
	private Integer page=0;

//	@In
//	private FacesMessages facesMessages;
	
	private String stringMsg="";
	    
	private transient List<AuctionitemBean> searchItems;
	

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
 //		log.debug("Create " + getClass().getName());
 		state = PersistentFacesState.getInstance();		
		log.info(" LR conversation is "+Manager.instance().isLongRunningConversation());
		if (!Manager.instance().isLongRunningConversation())
			Manager.instance().beginConversation();
		log.info(" LR conversation is "+Manager.instance().isLongRunningConversation());

		if (item1String!=null)log.info("ITEM 1 STRING="+item1String);
		if (item2String!=null)log.info("ITEM 2 STRING="+item2String);
		if (item3String!=null)log.info("ITEM 3 STRING="+item3String);
		this.setPatterns();	

	}

	
	public AuctionitemBean getAuctionitemBean(){
//		log.info("getgin auctionitemBean version="+this.auctionitemBean.toString());
		return this.auctionitemBean;
	}
	

	@Factory(scope=ScopeType.STATELESS)
 	public List<AuctionitemBean> getSearchItems() {
    	loadList();
		state=updatePFS();
 //       log.info("get list of items size="+searchItems.size()+" View version="+this);
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
		//get old values of previous list for expanded
        Map<Long, Boolean> expanded = getOldExpanded();
        setPatterns();
//	    log.info("!!!!!!!!!!!!!!!!QUERYING!!!!!!!!!!!!!!! SEARCHSTRING: " + searchString);
		searchItems = new ArrayList<AuctionitemBean>();   
//		doTestList();
		List resultList = em.createQuery("SELECT i, b FROM Auctionitem i LEFT JOIN i.bids b" +
		            " WHERE (i.bids IS EMPTY OR b.timestamp = (SELECT MAX(b1.timestamp) FROM i.bids b1))" +
		            " AND (i.itemId=:pattern1 or i.itemId=:pattern2" +
		            " or i.itemId=:pattern3)")
		            .setParameter("pattern1",pattern1)
		            .setParameter("pattern2",pattern2)
		            .setParameter("pattern3",pattern3)		            
	                .setMaxResults(pageSize)
	                .setFirstResult( page * pageSize )
		            .getResultList();
// 		 log.info("resultList has size="+resultList.size());
		 Object[] oa;
   /* should I bother adding the item if it is expired??? */
		 for (Object o : resultList) {
 //		    log.info("loading up the list");
		    oa = (Object[]) o;
		    AuctionitemBean auctionitemBean = new AuctionitemBean((Auctionitem) oa[0], (Bid) oa[1]);
		    if (!setup){
		      	 String renderGroup = Long.toString(auctionitemBean.getAuctionitem().getItemId());
//			     log.info("adding view to renderGroup ="+renderGroup);
		       	 renderManager.getOnDemandRenderer(renderGroup).add(this);	
			     this.viewName+=" :" + auctionitemBean.getAuctionitem().getTitle();
		    }
//		    else log.info("View has already been setup");
//		    log.info("adding item = "+auctionitemBean.getAuctionitem().getTitle());
		    searchItems.add(auctionitemBean);
		 }
		 createViewName();
         
		 //restore old expanded bid values
         if (searchItems.size() > 0)  {
             for (int i=0; i < searchItems.size() ;i++)  {
                AuctionitemBean searchItem = searchItems.get(i);
                Boolean oldState = 
                        expanded.get(searchItem.getAuctionitem().getItemId());
                if (null != oldState)  {
                    searchItem.setExpanded(oldState);
                }
             }
             createViewName();
         }

		 setup=true; 
	}


	private void createViewName() {
		if (!this.gotViewName){
			 log.info("setting conversation description to "+viewName);
			 Conversation.instance().setDescription(viewName);
			 gotViewName=true;
		 }
	}
	

	private Map<Long, Boolean> getOldExpanded(){
		Map<Long, Boolean> expanded = new HashMap<Long, Boolean>();
		if (searchItems !=null && searchItems.size()>0){
			for (int i=0; i<searchItems.size();i++){
				expanded.put(searchItems.get(i).getAuctionitem().getItemId(),
                searchItems.get(i).isExpanded());
			}
		}
		return expanded;

	}
	public void doTestList(){
		log.info("finding value for pattern1="+pattern1);
		Query query=em.createQuery(
				"from Auctionitem i where i.itemId=:pattern1 or i.itemId=:pattern2 or i.itemId=:pattern3");
		query.setParameter("pattern1", pattern1);
		query.setParameter("pattern2", pattern2);
		query.setParameter("pattern3", pattern3);
		List testList = query.getResultList();
		for (int i= 0; i< testList.size(); i++) {
	//		log.info("have a value for this pattern i="+i);
			Auctionitem au = (Auctionitem)testList.get(i);
			log.info("auctionitem["+i+"] is ="+au.getTitle());
		}if (testList.isEmpty()) log.info("testList is empty!");
	}
	
	@Observer("bidUpdated")
	public void updateDataModel(String itemId) {
		log.info("Observer pattern for itemID="+itemId);
	  // update the list by hitting the database   // only if the item is in this list
	}
	
    @Destroy @Remove
	public void destroy() {
	   log.info("View: destroy version="+this);
		cleanup();
	}

	public int getPageSize() {
		return this.pageSize;
	}
	
	public int getPage(){return this.page;}

	public PersistentFacesState getState() {
	//	log.info("getState ");
		return state;
	}

	private PersistentFacesState updatePFS(){
 		PersistentFacesState state_1 = PersistentFacesState.getInstance();
 		if (state_1 !=null){
 //			log.info("####   updating PFS");
 			state=state_1;
 		}
 //		else log.info("####  PFS is null!!! using stale state");
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
		else log.info("search items is null  ?  Why is that, Judy?");
	}

	public void setPatterns() {	
        if (item1String != null)  { pattern1 = parseLong(item1String); }
        if (item1String != null)  { pattern2 = parseLong(item2String); }
        if (item1String != null)  { pattern3 = parseLong(item3String); }
	}

    long parseLong(String longString)  {
        try {
            return Long.parseLong(longString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

	public void removeView(){
		cleanup();
		log.info("after cleanup()");
	}


	public String getCid(){
	 //	return "cid";
		log.info("getCid ="+Manager.instance().getCurrentConversationId() );
		return Manager.instance().getCurrentConversationId();
	}
	public void setCid(String cIn){		
		this.cid=cIn;
	}


	public String getItem1String() {
		return this.item1String;
	}
	
	public String getViewName(){
		log.info("getting viewName of "+viewName);
		return viewName;
		//return Conversation.instance().getDescription();
	}

}
