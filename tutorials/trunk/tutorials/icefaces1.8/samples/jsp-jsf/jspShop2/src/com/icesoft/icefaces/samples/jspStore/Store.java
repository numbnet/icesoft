package com.icesoft.icefaces.samples.jspStore;

import java.util.Hashtable;
import javax.faces.event.ActionEvent;

/**
 * Maintains a table of StoreItem's.
 * 
 * @see StoreItem
 */
public class Store {
    
	private Hashtable items;
	
    /**
     * Creates a new store and populate it with several items.
     */
    public Store() {
        items = new Hashtable();
        loadDefaults();
    }
    
    /**
     * Instantiates a set of default StoreItem's
     */
    public void loadDefaults(){
    	items.put("001",new StoreItem("Ice Cube", 12.00, 7, "001"));
        items.put("002",new StoreItem("Ice Breaker", 1.25, 11, "002"));
        items.put("003",new StoreItem("Ice Castle", 0.38, 13, "003"));
    }
    
    /**
     * Retrieves the StoreItem's in a format that can be used
     * by both JSP and JSF.
     * 
     * @return array of StoreItem's
     */
    public Object[] getItems() {
    	return items.values().toArray();
    }
    
    public void actionTest(ActionEvent e){
    	
    	System.out.println("action test");
    }
    
    /**
     * Identifies a StoreItem based on its hash key and
     * decreases its quantity if possible.
     * 
     * @param sku the lookup key
     */
    public void purchase(String sku){

        StoreItem temp = (StoreItem) items.get(sku);
            if (temp != null)
                temp.decreaseQuantity();         
    }
    
    /**
     * Identifies a StoreItem based on its hash key and
     * increases its quantity if possible.
     * 
     * @param sku
     */
    public void remove(String sku){
                
        StoreItem temp = (StoreItem) items.get(sku);
            if (temp != null)
                temp.increaseQuantity();      
    }
}