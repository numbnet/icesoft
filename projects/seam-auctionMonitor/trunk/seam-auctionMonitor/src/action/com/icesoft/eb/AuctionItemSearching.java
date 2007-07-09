package com.icesoft.eb;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ValueChangeEvent;

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

   // ice:commandSortHeader methods
   public String getBidsColumnName();
   public String getItemNameColumnName();
   public String getPriceColumnName();
   public String getExpiresColumnName();   
   public String getSort() ;
   public void setSort(String sort);
   public boolean isAscending();
   public void setAscending(boolean ascending);

   // ice:selectInputText methods (autocomplete)
   public void updateList(ValueChangeEvent event);
   public List getList();

}