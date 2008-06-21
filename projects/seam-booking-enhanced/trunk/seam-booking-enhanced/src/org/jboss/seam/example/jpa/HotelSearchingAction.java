//$Id: HotelSearchingAction.java 5509 2007-06-25 16:19:40Z gavin $
package org.jboss.seam.example.jpa;

import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.log.Log;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.Manager;
import org.jboss.seam.security.Identity;
import org.jboss.seam.web.Session;

import com.icesoft.faces.component.ext.RowSelectorEvent;

@Name("hotelSearch")
@Scope(ScopeType.CONVERSATION)
public class HotelSearchingAction implements Serializable
{
   @Out
   private boolean showDetails=false;
   
   @Logger 
   private Log log;
	
   @In
   private EntityManager em;
 
   private String searchString;
   private int pageSize = 10;
   private int page;
   
   /* for development..always good to see cid & is conv LR*/
   private String cid;
   private String longRunning;
   
   @Out(required=false)
   private List<Hotel> hotels;
   
   
   @Create
   public void init(){
	   //only do it this way because of portal situation
	   //since this is a conversation-scoped bean and 
	   //the first one in the app is created automatically
	   //and a conversation begun
	   log.info("creating conversation search bean version="+this);
	   if (!Manager.instance().isLongRunningConversation()){
		   Manager.instance().beginConversation();
		   log.info("had to start it MANUALLY!!");
	   }
   }
   public void find()
   {
      page = 0;
      queryHotels();
   }

   public void nextPage()
   {
      page++;
      queryHotels();
   }
      
   private void queryHotels()
   {
      hotels = em.createQuery("select h from Hotel h where lower(h.name) like :search or lower(h.city) like :search or lower(h.zip) like :search or lower(h.address) like :search")
            .setParameter( "search", getSearchPattern() )
            .setMaxResults(pageSize)
            .setFirstResult( page * pageSize )
            .getResultList();
   }

   @Factory(value="pattern", scope=ScopeType.EVENT)
   public String getSearchPattern()
   {
	   log.info("getSearchPattern");
      return searchString==null ?
            "%" : '%' + searchString.toLowerCase().replace('*', '%') + '%';
   }
   
   public boolean isNextPageAvailable()
   {
      return hotels!=null && hotels.size()==pageSize;
   }
   
   public int getPageSize() {
      return pageSize;
   }

   public void setPageSize(int pageSize) {
      this.pageSize = pageSize;
   }

   public String getSearchString()
   {
      return searchString;
   }

   public void setSearchString(String searchString)
   {
      this.searchString = searchString;
   }
   public void handleSearchStringChange(ValueChangeEvent e) {
	      page = 0;
	      setSearchString( (String) e.getNewValue() );
	      queryHotels();
	   }
	   
   public List<SelectItem> getCities() {
	      return em.createQuery("select distinct new javax.faces.model.SelectItem(h.city) from Hotel h where lower(h.city) like :search order by h.city")
	            .setParameter("search", getSearchPattern())
	            .getResultList();
   }
	   
   public void handlePageSizeChange(ValueChangeEvent e)  {
	      page=0;
	      setPageSize( (Integer) e.getNewValue() );
	      queryHotels();
   }
   
   @Begin(join=true)
   public void rowSelection(RowSelectorEvent e){
	   int f = e.getRow();
	   Hotel selHotel = (Hotel)hotels.get(f);
	   Events.instance().raiseEvent("selectedHotel",selHotel);
	   this.showDetails=true;
   }

    @Observer({"bookingConfirmed","cancelDetails"})
    public void setShowDetails(){
 	   this.showDetails=false;
    }
   
   public boolean isShowDetails(){
	   return this.showDetails;
   }

   @End(beforeRedirect=true)
   public void logout(){
	   this.showDetails=false;
	   Identity.instance().unAuthenticate();
	   Session.instance().invalidate();
   }
   
   /** for devlopment stuff only to check values */
   public String getLongRunning(){
	   if (Manager.instance().isLongRunningConversation()) return "true";
	   else return "false";
   }
 
   /** for devlopment stuff only to check values */
   public String getCid(){
	   return Manager.instance().getCurrentConversationId();
   }
   

}
