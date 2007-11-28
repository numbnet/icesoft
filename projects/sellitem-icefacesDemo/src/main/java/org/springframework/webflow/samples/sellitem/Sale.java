/*
 * Copyright 2004-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.webflow.samples.sellitem;

import java.io.Serializable;
import java.text.NumberFormat;

import org.springframework.core.style.ToStringCreator;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class Sale implements Serializable {

	private double price;

	private int itemCount;

	private String category;

	private boolean shipping;

	private String shippingType = "G";

    private SelectItem[] CITIES = {
            new SelectItem ("B", "Boston, MA"),
            new SelectItem ("C", "Chicago, IL"),
            new SelectItem ("D", "Detroit, MI"),
            new SelectItem ("E", "Eagleville, WI"),
            new SelectItem ("F", "Farley, ND"),
            new SelectItem ("G", "Galveston, TX") };

    private SelectItem[] sourceCities = CITIES.clone(); 
    private SelectItem[] destinationCities = CITIES.clone();

    private SelectItem[] shippingOptions = {
            new SelectItem ("G", "Standard Surface Mail (basic rate)" ),
            new SelectItem ("A", "Express Air Mail (1.5x basic rate)" ),
            new SelectItem ("C", "Overnight Courier (2x basic rate)") };

    private String sourceCity;
    private String destinationCity;

    private String shippingDescription;
    
    private int sourceCityIdx;
    private int destinationCityIdx;


    private float shippingRate[][] = { {0.05f, .2f, .33f, .015f, .4f, .6f},
                                       {.2f, 0.05f, .1f, .15f, .2f, .5f},
                                       {.3f, .1f, 0.05f, .2f,.2f, .5f},
                                       {.15f, .2f, .3f, .05f, .2f, .5f},
                                       {.4f, .2f, .3f, .25f, 0.05f, .5f},
                                       {.6f, .5f, .5f, .5f, .5f, 0.05f} };


    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = (null != category) ? category : "";
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
        if ((itemCount > 0) && (itemCount < 100)) {
            category = "";
        } else if ((itemCount >= 100) && (itemCount < 200) ) {
            category = "A";
        } else if (itemCount > 200) {
            category = "B";
        }
    }

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isShipping() {
        return shipping;
	}

	public void setShipping(boolean shipping) {
		this.shipping = shipping;
	}

	public String getShippingType() {
        return shippingType;
	}

    public String getShippingDescription() {
        return shippingDescription;

    } 

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
         for (int idx = 0; idx < shippingOptions.length; idx ++ ) {
            if (shippingOptions[idx].getValue().equals(shippingType)) {
                shippingDescription = shippingOptions[idx].getLabel();
            }
        }
    }

	// business logic methods

	/**
	 * Returns the base amount of the sale, without discount or delivery costs.
	 */
	public double getAmount() {
		return price * itemCount;
	}

	/**
	 * Returns the discount rate to apply.
	 */
	public double getDiscountRate() {
		double discount = 0.02;
		if ("A".equals(category)) {
			if (itemCount >= 100) {
				discount = 0.1;
			}
		}
		else if ("B".equals(category)) {
			if (itemCount >= 200) {
				discount = 0.2;
			}
		}
		return discount;
	}

	/**
	 * Returns the savings because of the discount.
	 */
	public double getSavings() {
		return getDiscountRate() * getAmount();
	}

	/**
	 * Returns the delivery cost per unit shipped, based on distance.
     * Returns 0 if shipping is not required
     * 
     * @return cost of shipping per item.
	 */
	public double getDeliveryRate() {

        if (!isShipping() ) {
            return 0;
        }
        
        double delCost = 0.0;
        float distanceRate = shippingRate[sourceCityIdx][destinationCityIdx];

        if ("G".equals(shippingType)) {
			delCost = distanceRate;
		} else if ("A".equals(shippingType)) {
			delCost = distanceRate*1.5;
		} else if ("C".equals(shippingType) ) {
            delCost = distanceRate * 2.0;
        } 
        return delCost;
	}


    /**
     * Get the deliver subtotal cost which is the item cost + shipping cost.
     * Final cost will be this subtotal minus discount rate.
     *
     * @return Subtotal of item and delivery costs. 
     */
    public double getDeliverySubtotal() {

        return (getDeliveryRate() * itemCount);
    }

    
    /**
	 * Returns the total cost of the sale, including discount and delivery cost.
	 */
	public double getTotalCost() {
        return getDeliverySubtotal() + getAmount() - getSavings();
    }

	public String toString() {
		return new ToStringCreator(this).append("price", price).append("itemCount", itemCount).toString();
	}


    public String getRealtimeCategory() {

        // Got to validate shipping item count numbers first
        int itemCount = getItemCount();
        if ((itemCount >= 0) && (itemCount < 100) ) {
            return "Default Discount   (2% for 0-100 items)";
        } else if ((itemCount >= 100) && (itemCount < 200)) {
            return "Category A   (10% for 100-200 items) ";
        } else {
            return "Category B   (20% for > 200 items)";
        } 
    }

    /**
     * Handle a value change callback on the itemCount 
     * @param e ValueChangeEvent containing the new value
     */
    public void handleItemCountChange(ValueChangeEvent e) {

        Object o = e.getNewValue();
        setItemCount( ((Integer)o).intValue() );
   }

    public SelectItem[] getSourceCities() {
        return sourceCities; 
    }

    public SelectItem[] getDestinationCities() {
        return destinationCities;
    }

    public SelectItem[] getShippingOptions() {
        return shippingOptions;  
    }


    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        sourceCityIdx = extractCityCode(sourceCity);
        this.sourceCity = sourceCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        destinationCityIdx = extractCityCode(destinationCity);
        this.destinationCity = destinationCity;
    }

    private int extractCityCode(String city) {
        for (int idx = 0; idx < CITIES.length; idx ++ ) {
            if (CITIES[idx].getValue().equals(city)) {
                return idx;
            }
        }
        return 0;         
    }
        
}
