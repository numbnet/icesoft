package com.icesoft.eb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * A central repository of Auctionitems.
*/
@Scope(ScopeType.APPLICATION)
@Name("auctionhouse")
public class AuctionHouseAction implements AuctionHouse{
    
    private boolean auctionitemListExists = false;
    private static List auctionitemList = new ArrayList();
    
    public void itemCreated(Auctionitem createdItem){
                
    }
    
    public void itemDeleted(Auctionitem deletedItem){
        
    }
    
    @Destroy @Remove
    public void destroy() {}

    public List getAuctionitemList() {
        return auctionitemList;
    }
    
    public void setAuctionitemList(List auctionitemList) {
        AuctionHouseAction.auctionitemList = auctionitemList;
    }

    public boolean isAuctionitemListExists() {
        return auctionitemListExists;
    }

    public void setAuctionitemListExists(boolean auctionitemListExists) {
        this.auctionitemListExists = auctionitemListExists;
    }



}
