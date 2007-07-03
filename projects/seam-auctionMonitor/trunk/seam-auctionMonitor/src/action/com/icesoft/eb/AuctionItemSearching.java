//$Id: HotelSearching.java,v 1.10 2007/02/25 19:09:39 gavin Exp $
package com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface AuctionItemSearching
{
   public int getPageSize();
   public void setPageSize(int pageSize);
   
   public String getSearchString();
   public void setSearchString(String searchString);
   
   public String getSearchPattern();

   public String getAuctionitems();
   
   public String find();
   public String nextPage();
   public boolean isNextPageAvailable();

   public void destroy();

   public void queryAuctionItems();
   
   public String getBidsColumnName();
   public String getItemNameColumnName();
   public String getPriceColumnName();
   public String getTimeLeftColumnName();
   
   public String getSort() ;
   public void setSort(String sort);

   public boolean isAscending();
   public void setAscending(boolean ascending);

}