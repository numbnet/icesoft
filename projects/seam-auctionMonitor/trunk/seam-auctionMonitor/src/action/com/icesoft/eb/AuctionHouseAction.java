package com.icesoft.eb;

import java.util.List;

import javax.ejb.Remove;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Scope;

@Scope(ScopeType.APPLICATION)
public class AuctionHouseAction implements AuctionHouse{

    private static List auctionitemBeans;

    AuctionHouseAction(){
        init();
    }
    
    private void init(){
        //This could populate the initial AuctionitemBeans List.       
    }
    
    public void itemCreated(AuctionitemBean itemBean){
                
    }
    
    public void itemExpired(AuctionitemBean itemBean){
        
    }
    
    @Destroy @Remove
    public void destroy() {}

}
