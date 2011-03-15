package com.icesoft.icefaces.samples.jspStore;

import javax.faces.event.ActionEvent;
import java.text.NumberFormat;

/**
 * Item for sale in the JSP-JSF store. Includes a callback to the Store.
 */
public class StoreItem {
    
    private Store storeBean;
    private String label, sku;
    private int quantity, purchasedQuantity;
    private double price;
    private static NumberFormat numberFormatter;
    
    /**
     * Default constructor.
     */
    public StoreItem() {
    }
    
    /**
     * Instantiates a new StoreItem and all of its properties.
     * 
     * @param label the label used to identify the item in the store GUI
     * @param price the price of the item
     * @param quantity the initial amount of the item
     * @param sku the unique identifier used for a hash key
     */
    public StoreItem(Store storeBean, String label, double price, int quantity, String sku){
        numberFormatter = NumberFormat.getCurrencyInstance();
        this.storeBean = storeBean;
    	this.label = label;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
    }
    
    /**
     * Special purchase method for the JSF store 
     * to allow the use of command buttons.
     * 
     * @param e triggered by the command button
     */
    public void directPurchase(ActionEvent e){
    	decreaseQuantity();
    	storeBean.reRender();
    }
    
    /**
     * Special remove method for the JSF store 
     * to allow the use of command buttons.
     * 
     * @param e triggered by the command button
     */
    public void directRemove(ActionEvent e){
    	increaseQuantity();
    	storeBean.reRender();
    }

    /**
     * Convenience method for increasing the
     * quantity property of the StoreItem.
     */
    public void increaseQuantity(){
        if (purchasedQuantity > 0) {
	    	quantity++;
	        purchasedQuantity--;
        }
    }
    
    /**
     * Convenience method for decreasing the
     * quantity property of the StoreItem.
     */
    public void decreaseQuantity(){
        if (quantity > 0){
            quantity--;
            purchasedQuantity++;
        }
    }

    public String getLabel() {
        return label;
    }

    public String getPrice() {
        return numberFormatter.format(price);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSku() {
        return sku;
    }
    
    public String getSubTotal(){
        return numberFormatter.format(purchasedQuantity * price);
    }
}
