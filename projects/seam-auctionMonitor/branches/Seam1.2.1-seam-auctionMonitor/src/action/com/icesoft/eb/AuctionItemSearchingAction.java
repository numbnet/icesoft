package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.security.Restrict;

import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

@Stateful
@Name("itemSearch")
@Scope(ScopeType.SESSION)
@Restrict("#{identity.loggedIn}")
public class AuctionItemSearchingAction extends SortableList implements AuctionItemSearching, Renderable
{
   @PersistenceContext(type=EXTENDED)
   private EntityManager em;

   private String searchString;
   private int pageSize = 10;
   private int page;

   @DataModel
   private List<AuctionitemBean> auctionitems;

   private PersistentFacesState state = PersistentFacesState.getInstance();

   private boolean first = true;

   @In
   private RenderManager renderManager;

   // sort column names
   private static String itemNameColumnName = "Item Name";
   private static String priceColumnName = "Price";
   private static String bidsColumnName = "Bids";
   private static String expiresColumnName = "Expires";
   // auctionitemBeanComparator used to sort AuctiontemBeans.
   private Comparator auctionitemBeanComparator;

   @In(required = false, scope = ScopeType.APPLICATION)
   @Out(required = false, scope = ScopeType.APPLICATION)
   private List<AuctionitemBean> globalAuctionItems;
   
   // list of possible matches.
   private List matchesList = new ArrayList();
   // Comparator utility for sorting SelectItem labels in autocomplete component.
   public static final Comparator LABEL_COMPARATOR = new Comparator() {
       String s1;
       String s2;
       // compare method for entries.
       public int compare(Object o1, Object o2) {
           s1 = ((SelectItem) o1).getLabel();
           if (o2 instanceof SelectItem) {
               s2 = ((SelectItem) o2).getLabel();
           } else {
               s2 = o2.toString();
           }
           // compare ingnoring case, give the user a more automated feel when typing
           return s1.compareToIgnoreCase(s2);
       }
   };   

    public AuctionItemSearchingAction(){
        // default sort header
        super(itemNameColumnName);
   }

   public PersistentFacesState getState() {
       return state;
   }

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
       System.out.println("!!!!!!!!!!!!!!!!QUERYING!!!!!!!!!!!!!!! SEARCHSTRING: " + searchString);
       List newAuctionitems = new ArrayList();
       
       List resultList = em.createQuery("SELECT i, b FROM Auctionitem i LEFT JOIN i.bids b" +
            " WHERE (i.bids IS EMPTY OR b.timestamp = (SELECT MAX(b1.timestamp) FROM i.bids b1))" +
            " AND (lower(i.currency) like #{pattern} or lower(i.description) like #{pattern}" +
            " or lower(i.imageFile) like #{pattern} or lower(i.location) like #{pattern} or lower(i.seller) like #{pattern}" +
            " or lower(i.site) like #{pattern} or lower(i.title) like #{pattern})")
            .setMaxResults(pageSize)
            .setFirstResult( page * pageSize )
            .getResultList();
       Object[] oa;
       AuctionitemBean auctionitemBean;
       if (globalAuctionItems == null){
           globalAuctionItems = new ArrayList<AuctionitemBean>();
       }
       // Instantiate search result AuctionitemBeans from result list.
       // Add the AuctionitemBean to the globalAuctionItems list so changes in
       // other views can be propagated across all views in the application.
       for (Object o : resultList) {
           oa = (Object[]) o;
           auctionitemBean = new AuctionitemBean((Auctionitem) oa[0], (Bid) oa[1], renderManager);
           newAuctionitems.add(auctionitemBean);
           System.out.println("ADDING TO GLOBALAUCTIONITEMS" + auctionitemBean.getAuctionitem().getDescription() + auctionitemBean.getAuctionitem().getItemId() + " TO GLOBALAUCTIONITEMS");
           globalAuctionItems.add(auctionitemBean);
       }
       if(first){
           auctionitems = new ArrayList();
           System.out.println("FIRST SEARCH");
           first = false;
       }
       // Remove this view as a renderable from the existing list of AuctionitemBeans.
       if(!auctionitems.isEmpty()){
           System.out.println("AUCTIONITEMS NOT EMPTY REMOVING RENDERABLES");
           for(int i=0; i<auctionitems.size(); i++){
               AuctionitemBean tempBean = ((AuctionitemBean)auctionitems.get(i));
               System.out.println("REMOVING FROM: " + tempBean.getAuctionitem().getTitle() + " " + tempBean.renderer.getName());
               tempBean.removeRenderable(this);
               globalAuctionItems.remove(tempBean);
           }
       }
       // Add this view as a renderable to the new list of AuctionitemBeans.
       if(!newAuctionitems.isEmpty()){
           System.out.println("NEWAUCTIONITEMS NOT EMPTY ADDING RENDERABLES");
           for(int i=0; i<newAuctionitems.size(); i++){
               AuctionitemBean tempBean = ((AuctionitemBean)newAuctionitems.get(i));
               System.out.println("ADDING TO: " + tempBean.getAuctionitem().getTitle() + " " + tempBean.renderer.getName());
               tempBean.addRenderable(this);
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

   protected void sort(final String column, final boolean ascending) {
           auctionitemBeanComparator = new Comparator(){
               public int compare(Object o1, Object o2) {
                   AuctionitemBean c1 = (AuctionitemBean) o1;
                   AuctionitemBean c2 = (AuctionitemBean) o2;
                   if (column == null) {
                       return 0;
                   }
                   else if (column.equals(itemNameColumnName)) {
                       return ascending ?
                               c1.getAuctionitem().getTitle().toLowerCase().compareTo( c2.getAuctionitem().getTitle().toLowerCase() ):
                               c2.getAuctionitem().getTitle().toLowerCase().compareTo( c1.getAuctionitem().getTitle().toLowerCase() );
                   }
                   else if (column.equals(priceColumnName)) {
                       return ascending ?
                               new Double(c1.getBid().getBidValue()).compareTo( new Double(c2.getBid().getBidValue()) ):
                               new Double(c2.getBid().getBidValue()).compareTo( new Double(c1.getBid().getBidValue()) );
                   }
                   else if (column.equals(bidsColumnName)) {
                       return ascending ?
                               new Integer(c1.getAuctionitem().getBidCount()).compareTo( new Integer(c2.getAuctionitem().getBidCount()) ):
                               new Integer(c2.getAuctionitem().getBidCount()).compareTo( new Integer(c1.getAuctionitem().getBidCount()) );
                   }
                   else if (column.equals(expiresColumnName)) {
                       return ascending ?
                               c1.getAuctionitem().getExpires().compareTo(c2.getAuctionitem().getExpires()):
                               c2.getAuctionitem().getExpires().compareTo(c1.getAuctionitem().getExpires());
                   }
                   else return 0;
               }
           };

       Collections.sort(auctionitems, auctionitemBeanComparator);

   }

   protected boolean isDefaultAscending(String sortColumn) {
       return true;
   }

    public String getBidsColumnName() {
        return bidsColumnName;
    }
    
    public String getItemNameColumnName() {
        return itemNameColumnName;
    }
    
    public String getPriceColumnName() {
        return priceColumnName;
    }
    
    public String getExpiresColumnName() {
        return expiresColumnName;
    }
    
    public String getAuctionitems(){
            sort(getSort(), isAscending());
            return "";
    }

    /**
     * The list of possible matches for the given SelectInputText value
     *
     * @return list of possible matches.
     */
    public List getList() {
        return matchesList;
    }
    
    /**
     * Called when a user has modifed the SelectInputText value.  This method
     * call causes the match list to be updated.
     *
     * @param event
     */
    public void updateList(ValueChangeEvent event) {
        System.out.println("AUTOCOMPLETE UPDATING LIST!!!");
        // get a new list of matches.
        setMatches(event);

        if (event.getComponent() instanceof SelectInputText) {
            SelectInputText autoComplete =
                    (SelectInputText) event.getComponent();
            if (autoComplete.getSelectedItem() != null) {
                searchString = autoComplete.getSelectedItem().getLabel();
                find();
            }
            else {
                // setMatches() will return a complete list by default.
            }
        }
    }
    
    /**
     * Utility method for building the match list given the current value of the
     * SelectInputText component.
     *
     * @param event
     */
    // maxMatches hard coded because of JIRA ICE-1320
    int maxMatches = 4;
    private void setMatches(ValueChangeEvent event) {
        System.out.println("AUTOCOMPLETE SETTING MATCHES: " + event.getNewValue());
        Object searchWord = event.getNewValue();
        //int maxMatches = ((SelectInputText) event.getComponent()).getRows();
        List matchList = new ArrayList(maxMatches);
        
        if(searchWord.equals("")){
            searchString = null;
            find();
            return;
        }
        searchString = searchWord.toString();
        List autoCompleteResultList = em.createQuery("SELECT i FROM Auctionitem i " +
                " WHERE (lower(i.currency) like #{pattern} or lower(i.description) like #{pattern}" +
                " or lower(i.imageFile) like #{pattern} or lower(i.location) like #{pattern} or lower(i.seller) like #{pattern}" +
                " or lower(i.site) like #{pattern} or lower(i.title) like #{pattern})")
                .setMaxResults(maxMatches)
                .getResultList();
        for (Object o : autoCompleteResultList) {
            Auctionitem tempAuctionitem = (Auctionitem)o;
            System.out.println("MATCHES ADDING " + tempAuctionitem.getTitle());
            matchList.add(new SelectItem(tempAuctionitem,tempAuctionitem.getTitle())); 
        }
        
        // assign new matchList
        if (this.matchesList != null) {
            this.matchesList.clear();
            this.matchesList = null;
        }
        this.matchesList = matchList;
    }
    
    @Destroy @Remove
    public void destroy() {
        for(int i=0; i<auctionitems.size(); i++){
            AuctionitemBean tempBean = ((AuctionitemBean)auctionitems.get(i));
            System.out.println("DESTROY METHOD REMOVING FROM AuctionItemSearchingAction: " + tempBean.getAuctionitem().getTitle() + tempBean.renderer.getName());
            tempBean.removeRenderable(this);
            globalAuctionItems.remove(tempBean);
        }
    }

}