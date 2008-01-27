package com.icesoft.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.icesoft.eb.AuctionitemBean;
import com.icesoft.faces.async.render.Renderable;


@Local
public interface IView extends Serializable, Renderable{

  public void destroy();
  public int getPageSize();
  public int getPage();
  public List<AuctionitemBean> getSearchItems();
  
  public void setPatterns();
  public void startUp();
  
  public void loadList();
  public void removeView();


  public String getCid();
  public String getViewName();
  public void setCid(String cIn);
  
  public String getItem1String();
  public void createView();
}
