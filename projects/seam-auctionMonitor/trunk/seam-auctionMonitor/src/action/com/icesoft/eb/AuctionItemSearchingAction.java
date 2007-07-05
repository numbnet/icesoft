//$Id: HotelSearchingAction.java,v 1.17 2007/02/25 19:09:39 gavin Exp $
package com.icesoft.eb;

import static javax.persistence.PersistenceContextType.EXTENDED;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
//@Restrict("#{identity.loggedIn}")
public class AuctionItemSearchingAction extends SortableList implements AuctionItemSearching, Renderable
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

   // sort column names
   private static String itemNameColumnName = "Item Name";
   private static String priceColumnName = "Price";
   private static String bidsColumnName = "Bids";
   private static String timeLeftColumnName = "Time Left";
   // comparator used to sort queues.
   private Comparator comparator;

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
   
   @In(create=true)
   private AuctionHouseAction auctionhouse;   

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
       AuctionitemBean auctionitemBean;
       if (globalAuctionItems == null){
           globalAuctionItems = new ArrayList<AuctionitemBean>();
       }
       for (Object o : resultList) {
           oa = (Object[]) o;
           auctionitemBean = new AuctionitemBean((Auctionitem) oa[0], (Bid) oa[1], renderManager);
           auctionitemBean.addRenderable(this);
           newAuctionitems.add(auctionitemBean);
           System.out.println("ADDING TO GLOBALAUCTIONITEMS" + auctionitemBean.getAuctionitem().getDescription() + auctionitemBean.getAuctionitem().getItemId() + " TO GLOBALAUCTIONITEMS");
           globalAuctionItems.add(auctionitemBean);
       }
       if(first){
           auctionitems = new ArrayList();
           System.out.println("FIRST SEARCH");
           first = false;
       }
       if(!auctionitems.isEmpty()){
           System.out.println("AUCTIONITEMS NOT EMPTY REMOVING RENDERABLES");
           for(int i=0; i<auctionitems.size(); i++){
               AuctionitemBean tempBean = ((AuctionitemBean)auctionitems.get(i));
               System.out.println("REMOVING FROM: " + tempBean.getAuctionitem().getTitle() + " " + tempBean.renderer.getName());
               tempBean.removeRenderable(this);
               globalAuctionItems.remove(tempBean);
           }
       }
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

   /**
    * Sort the list.
    */
   protected void sort(final String column, final boolean ascending) {
           comparator = new Comparator(){
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
                   else if (column.equals(timeLeftColumnName)) {
                       return ascending ?
                               c1.getAuctionitem().getExpires().compareTo(c2.getAuctionitem().getExpires()):
                               c2.getAuctionitem().getExpires().compareTo(c1.getAuctionitem().getExpires());
                   }
                   else return 0;
               }
           };

       Collections.sort(auctionitems, comparator);

   }

   /**
    * Determines the sort order.
    *
    * @param sortColumn to sort by.
    * @return whether sort order is ascending or descending.
    */
   protected boolean isDefaultAscending(String sortColumn) {
       return true;
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

public String getBidsColumnName() {
    return bidsColumnName;
}

public String getItemNameColumnName() {
    return itemNameColumnName;
}

public String getPriceColumnName() {
    return priceColumnName;
}

public String getTimeLeftColumnName() {
    return timeLeftColumnName;
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
    System.out.println("GETTING LIST!!!!");
    return matchesList;
}

/**
 * Called when a user has modifed the SelectInputText value.  This method
 * call causes the match list to be updated.
 *
 * @param event
 */
public void updateList(ValueChangeEvent event) {
    System.out.println("UPDATING LIST!!!");
    // get a new list of matches.
    setMatches(event);

    // Get the auto complete component from the event and assing
    if (event.getComponent() instanceof SelectInputText) {
        SelectInputText autoComplete =
                (SelectInputText) event.getComponent();
        // if no selected item then return the previously selected item.
        if (autoComplete.getSelectedItem() != null) {
            searchString = autoComplete.getSelectedItem().getLabel();
            System.out.println("FINDING FROM UPDATELIST");
            find();
        }
        // otherwise if there is a selected item get the value from the match list
        else {
            //searchString = null;
            System.out.println("UPDATELIST NO SELECTION!!!!!!!!!!!");
            //find();

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
    System.out.println("SETTING MATCHES: " + event.getNewValue());
    Object searchWord = event.getNewValue();
    //int maxMatches = ((SelectInputText) event.getComponent()).getRows();
    List matchList = new ArrayList(maxMatches);
    
    if(searchWord.equals("")){
        System.out.println("SEARCHWORD EQUALS EMPTY STRING");
        searchString = null;
        find();
        matchList = auctionitems;
        return;
    }

    try {

        int insert = Collections.binarySearch(auctionhouse.getAuctionitemList(), searchWord,
                                              LABEL_COMPARATOR);

        // less then zero if wer have a partial match
        if (insert < 0) {
            insert = Math.abs(insert) - 1;
        }

        for (int i = 0; i < maxMatches; i++) {
            // quit the match list creation if the index is larger then
            // max entries in the dictionary if we have added maxMatches.
            if ((insert + i) >= auctionhouse.getAuctionitemList().size() ||
                i >= maxMatches) {
                break;
            }
            matchList.add(auctionhouse.getAuctionitemList().get(insert + i));
        }
    } catch (Throwable e) {
        e.printStackTrace();
    }
    // assign new matchList
    if (this.matchesList != null) {
        this.matchesList.clear();
        this.matchesList = null;
    }
    this.matchesList = matchList;
}
/*
private City getMatch(String value) {
    City result = null;
    if (matchesList != null) {
        SelectItem si;
        Iterator iter = matchesList.iterator();
        while (iter.hasNext()) {
            si = (SelectItem) iter.next();
            if (value.equals(si.getLabel())) {
                result = (City) si.getValue();
            }
        }
    }
    return result;
}
*/
@Create
public void generateAuctionHouseList(){
    if(!auctionhouse.isAuctionitemListExists()){
    
        List auctionitemList = new ArrayList();
        System.out.println("AUCTION HOUSE GETTING ITEMS!");
        List resultList = em.createQuery("SELECT i FROM Auctionitem i " )
                .getResultList();
        
        for (Object o : resultList) {
            Auctionitem tempAuctionitem = (Auctionitem)o;
            System.out.println("AUCTION HOUSE ADDING " + tempAuctionitem.getTitle());
            auctionitemList.add(new SelectItem(tempAuctionitem,tempAuctionitem.getTitle())); 
        }
        
        auctionhouse.setAuctionitemList(auctionitemList);
        auctionhouse.setAuctionitemListExists(true);
    }       
}
}