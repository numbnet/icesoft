//$Id: HotelSearchingAction.java,v 1.17 2007/02/25 19:09:39 gavin Exp $
package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.security.Restrict;

import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;
@Stateful
@Name("itemSearch")
@Scope(ScopeType.CONVERSATION)
//@Restrict("#{identity.loggedIn}")

public class AuctionItemSearchingAction implements AuctionItemSearching, Renderable
{
   
   @PersistenceContext(type=EXTENDED)
   private EntityManager em;   
   
   private String searchString;
   private int pageSize = 10;
   private int page;
   
   @DataModel
   private List<AuctionitemBean> auctionitems;
   
//   @In
//   ViewManagerAction viewManager;
   private PersistentFacesState state = PersistentFacesState.getInstance();
   
   private boolean first = true;

   @In
   private RenderManager renderManager;

    public AuctionItemSearchingAction(){
       
   }

   public PersistentFacesState getState() {
       return state;
   }

//   @Begin
   public String find()
   {
       page = 0;
      queryAuctionItems();
      return "";
   }
   public String nextPage()
   {
      page++;
      queryAuctionItems();
      return "";
   }

   @Factory("auctionitems")
   public void queryAuctionItems()
   {
       List newAuctionitems = new ArrayList();
/*
       newAuctionitems = em.createQuery("SELECT new com.icesoft.eb.AuctionitemBean(i, b) FROM Auctionitem i LEFT JOIN i.bids b" +
            " WHERE (i.bids IS EMPTY OR b.timestamp = (SELECT MAX(b1.timestamp) FROM i.bids b1))" +
            " AND (lower(i.currency) like #{pattern} or lower(i.description) like #{pattern}" +
            " or lower(i.imageFile) like #{pattern} or lower(i.location) like #{pattern} or lower(i.seller) like #{pattern}" +
            " or lower(i.site) like #{pattern} or lower(i.title) like #{pattern})")
            .setMaxResults(pageSize)
            .setFirstResult( page * pageSize )
            .getResultList();
*/
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
           newAuctionitems.add(new AuctionitemBean((Auctionitem) oa[0], (Bid) oa[1], renderManager));
       }
       if(first){
           auctionitems = newAuctionitems;
           first = false;
           return;
       }
       if(newAuctionitems.equals(auctionitems)){
           return;
       }else{
           if(!auctionitems.isEmpty()){
               for(int i=0; i<auctionitems.size(); i++){
                   AuctionitemBean tempBean = ((AuctionitemBean)auctionitems.get(i));
                   if(newAuctionitems.contains(tempBean)){
                       continue;
                   }else{
                       tempBean.removeRenderable(this);
                   }
               }
           }
           if(!newAuctionitems.isEmpty()){
               for(int i=0; i<newAuctionitems.size(); i++){
                   AuctionitemBean tempBean = ((AuctionitemBean)newAuctionitems.get(i));
                   if(auctionitems.contains(tempBean)){
                       continue;
                   }else{
                       tempBean.addRenderable(this);
                   }
               }
           }
       }

       auctionitems = newAuctionitems;
   }
   
   public boolean isNextPageAvailable()
   {
      return auctionitems!=null && auctionitems.size()==pageSize;
   }
   
   public int getPageSize() {
      return pageSize;
   }
   
   public void setPageSize(int pageSize) {
      this.pageSize = pageSize;
   }
   
   @Factory(value="pattern", scope = ScopeType.EVENT)
   public String getSearchPattern()
   {
      return searchString==null ? 
            "%" : '%' + searchString.toLowerCase().replace('*', '%') + '%';
   }
   
   public String getSearchString()
   {
       state = PersistentFacesState.getInstance();
      return searchString;
   }
   
   public void setSearchString(String searchString)
   {
      this.searchString = searchString;
   }
   
   /**
    * Callback method that is called if any exception occurs during an attempt
    * to render this Renderable.
    *
    * @param renderingException The exception that occurred when attempting
    * to render this Renderable.
    */
   public void renderingException(RenderingException renderingException) {

       if (renderingException instanceof TransientRenderingException ){
           
       }
       else if(renderingException instanceof FatalRenderingException){
           for(int i=0; i<auctionitems.size(); i++){
               AuctionitemBean tempBean = ((AuctionitemBean)auctionitems.get(i));
               tempBean.removeRenderable(this);
           }
       }
   }
   
   @Destroy @Remove
   public void destroy() {}

}