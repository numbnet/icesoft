package com.icesoft.ejb;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
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
import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.context.effects.Scale;


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
	private String item1String="";
	private String item2String="";
	private String item3String="";
	private final int MAX_ITEMS_PER_VIEW = 3;
	 
	private int pageSize=5;
	
	private Integer page=0;
	
	Auctionitem currentItem;
	
	@In(value="renderManager")
	RenderManager renderManager;
	
	private List<Auctionitem> viewList = new ArrayList<Auctionitem>();

	@DataModel
	private List<AuctionitemBean> auctionItemsList;
	
   @PersistenceContext(type=EXTENDED)
    EntityManager em;
   
   // effects
   private Effect dragEffect;  
   
   @Factory(value="auctionItems", scope = ScopeType.EVENT)
	public void loadList() {
//	      log.info("!!!!!QUERYING!!!!! SEARCHSTRING: " + searchString);
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
//	      log.info("size of auctionItems is "+auctionItemsList.size());
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
//	     log.info("AUTOCOMPLETE UPDATING LIST!!!");
	      page = 0;
	      setSearchString( (String) event.getNewValue() );
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
//	   log.info("looking for "+this.searchString);
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
	   /**
     * Method used to handle the drag and drop events from the page Items can
     * be dragged from the dataTable list to the panel below to create a view.
     *
     * @param DragEvent event fired
     * @return void
     */
    public void iconListener(DragEvent event) {
        if (event.getEventType() == DndEvent.HOVER_START) {
            String targetId = event.getTargetClientId();
            if ((targetId != null) &&
                (targetId.indexOf("cartDropTarget") != -1)) {
                dragEffect = new Highlight();
            }
        }
        // only deal with DROPPED event types
        if (event.getEventType() == DndEvent.DROPPED) {
  //      	log.info("item is dropped");
            String targetId = event.getTargetClientId();
            if ((targetId != null) &&
                (targetId.indexOf("viewDropTarget") != -1)) {           	
                    String value = ((HtmlPanelGroup) event.getComponent())
                        .getDragValue().toString();
                    log.info("value dragged is "+value);
                    //only add a dragged item if room in the view
                    Auctionitem currentitem = null;
                	if (viewList.size() < MAX_ITEMS_PER_VIEW)
                      currentitem = findAuctionitem(value);
                	if (currentitem !=null){
                        // ensure the dropped target was the cart
                        if (targetId.endsWith("viewDropTarget")) {
                            // only add if it's not already there!!!
   //                     	log.info("hit target area so add item to list");
                        	if (viewList.contains(currentitem)){
                        		log.info("\t\t!!!already contains item!!!!");
                        	}
                        	else {
                        		viewList.add(currentitem);
                        		updateItemStrings();
                        	}
                        }else log.info("\t\t!!!!didn't hit target area");
                	}
                	else {
                		log.info("\t\t!!!!problem adding this item to the viewList");
                	}
            }
        }
    }
    
    /**
     * Use whatever the current list is to find the Auctionitem as
     * this list is the only thing you can drag from
     * First ensure that the max number of objects in view is not 
     * violated and then ensure that the item isn't already in the 
     * viewList to create.  
     * @param id
     * @return
     */
    private Auctionitem findAuctionitem(String id){
    	//just have to find the auctionitem in the list
    	log.info("looking for item with id="+id);
    	for (AuctionitemBean aib : auctionItemsList){
    		if (String.valueOf(aib.getAuctionitem().getItemId()).startsWith(id)){
    			log.info("\t\tadding to list "+aib.getAuctionitem().getTitle());
    			return aib.getAuctionitem();
    		}
    	}
    	log.info("!!!!not found!!!");
    	return null;
    }
    
    private void updateItemStrings(){
    		if (viewList.size()>0) item1String = Long.toString(viewList.get(0).getItemId());
    		else item1String=null;
    		if (viewList.size()>1) item2String = Long.toString(viewList.get(1).getItemId());
    		else item2String=null;
    		if (viewList.size()>2) item3String = Long.toString(viewList.get(2).getItemId());
    		else item3String=null;
    }
    
	public void add(AuctionitemBean item){
		log.info("trying to add "+item.getAuctionitem().getTitle()+" to list");
		Auctionitem ai = item.getAuctionitem();
		 if (viewList.isEmpty() || !viewList.contains(ai))viewList.add(ai);
		 updateItemStrings();
	}
    public void remove(Auctionitem item) {
         //have to remove the currentItem from the list 
    	//am thinking that panelSeries with contextMenu is best way to go
 //   	log.info("in remove for item ="+e.getComponent().getClass().getName());
    	log.info("want to remove from viewList item="+item.getTitle());
    	viewList.remove(item);	
		 updateItemStrings();
    }
	public String getItem1String() {
		return item1String;
	}
	public void setItem1String(String item1String) {
		this.item1String = item1String;
	}
	public String getItem2String() {
		return item2String;
	}
	public void setItem2String(String item2String) {
		this.item2String = item2String;
	}
	public String getItem3String() {
		return item3String;
	}
	public void setItem3String(String item3String) {
		this.item3String = item3String;
	}

	public List<Auctionitem> getViewList(){
		return this.viewList;
	}
	public void clearView(ActionEvent event){
		log.info("clearing view");
		viewList.clear();
	}
    

	
}
