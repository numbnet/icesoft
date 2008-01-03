package com.icesoft.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.icesoft.eb.AuctionitemBean;
import com.icesoft.faces.async.render.Renderable;


@Local
public interface IView extends Serializable, Renderable{
  public String getSearchString();
  public void setSearchString(String sIn);
  public void destroy();
  public int getPageSize();
  public int getPage();
  public List<AuctionitemBean> getSearchItems();
  
  public AuctionitemBean getAuctionitemBean();
  public void setPattern();
  public void startUp();
  
  public void loadList();
  public void removeView();
  public boolean haveString();


  public String getCid();
  public void setCid(String cIn);
  public void updateDataModel(String itemId);
}
