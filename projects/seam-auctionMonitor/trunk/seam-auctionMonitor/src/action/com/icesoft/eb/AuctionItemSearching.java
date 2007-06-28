//$Id: HotelSearching.java,v 1.10 2007/02/25 19:09:39 gavin Exp $
package com.icesoft.eb;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.jsf.ListDataModel;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AuctionItemSearching
{
   public int getPageSize();
   public void setPageSize(int pageSize);
   
   public String getSearchString();
   public void setSearchString(String searchString);
   
   public String getSearchPattern();

//   public List<AuctionitemBean> getAuctionitems();
   
   public String find();
   public String nextPage();
   public boolean isNextPageAvailable();

   public void destroy();

   public void queryAuctionItems();
/*   
   public String getBidsColumnName();
   public String getItemNameColumnName();
   public String getPriceColumnName();
   public String getTimeLeftColumnName();
   
   public String getSort() ;
   public boolean isAscending();
*/
@Factory(value = "auctionitems", autoCreate = true, scope = ScopeType.CONVERSATION)
ListDataModel getAuctionItems();
}