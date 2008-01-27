package com.icesoft.ejb;

import java.util.List;

import javax.ejb.Local;

import com.icesoft.faces.async.render.OnDemandRenderer;


@Local
public interface IAuctionHouse {
    
    public void createList();
    public void destroy();  
    
}
