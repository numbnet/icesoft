package com.icesoft.icefaces.tutorial.component.table.sortable;

import java.util.Comparator;
import java.util.Arrays;

/**
 * <p>
 * A basic backing bean for a ice:dataTable component.  This bean contains a
 * Collection of IventoryItem objects which is used as the dataset for a
 * dataTable component.  Each instance variable in the InventoryItem obejct
 * is represented as a column in the dataTable component.
 * </p>
 * <p>This Bean also extends SortableList to aid in making the dataTable
 * sortable via commandSortHeader component. </p>
 *
 */
public class TableBean extends SortableList {

    //  List of sample inventory data.
    private InventoryItem[] carInventory = new InventoryItem[]{
            new InventoryItem(58285, "Dodge Grand Caravan", " Sto&Go/Keyless",  43500, 21695),
            new InventoryItem(57605, "Dodge SX 2.0", "Loaded/Keyless", 28000 ,14495),
            new InventoryItem(57805, "Chrysler Sebring Touring", "Keyless/Trac Cont", 31500, 15995),
            new InventoryItem(57965, "Chrysler PT Cruiser Convertible", "Touring/Loaded", 7000 , 22195),
            new InventoryItem(58095, "Chrysler Pacifica AWD", "Heated Lthr/19' Alloy", 43500, 31995),
            new InventoryItem(58165, "Jeep Liberty Sport", "Loaded/Keyless", 31000, 26995),
            new InventoryItem(58205, "Dodge SX 2.0", "Loaded/Keyless", 19500, 15495),
            new InventoryItem(58245, "Chrysler Pacifica AWD", "Moonroof/DVD", 15500, 35695),
            new InventoryItem(58295, "Pontiac Montana SV6 Ext", "Loaded/Quads", 40000, 22695),
            new InventoryItem(58355, "Jeep Grand Cherokee", "Laredo/Trailer", 26500, 27495),
            new InventoryItem(58365, "Jeep Grand Cherokee", "Laredo/Trailer", 27000, 28595),
            new InventoryItem(58375, "Chrysler PT Cruiser", "Cruise/KeylessD", 29500, 17795),
            new InventoryItem(58425, "Dodge Durango SLT", "Leather/3rd row", 32500, 26695),
            new InventoryItem(58475, "Dodge Grand Caravan", "Quads/Rear AC", 52000, 19895),
            new InventoryItem(58455, "Chrysler Sebring Touring", "Keyless/Trac Cont", 34000, 16695),
            new InventoryItem(58465, "Chrysler Sebring Touring", "Keyless/Trac Cont", 32500, 15995),
            new InventoryItem(58495, "Chrysler Sebring Touring", "Keyless/Trac Cont", 22500, 16695),
            new InventoryItem(58155, "GM G2500 Cargo Van", "Extended/Auto/Air", 34000, 27795),
            new InventoryItem(58275, "Dodge Dakota Q.C. SLT", "4x4/Loaded/Alloys", 22500, 27995),
            new InventoryItem(58265, "Chrysler 300 Touring", "Heated Leather", 40500, 26495)
    };

    // dataTableColumn Names
    private static final String stockColumnName = "Stock #";
    private static final String modelColumnName = "Model";
    private static final String descriptionColumnName = "Description";
    private static final String odometerColumnName = "Odometer";
    private static final String priceColumnName = "Price";


    public TableBean() {
        super(stockColumnName);
    }

    public String getStockColumnName() {
        return stockColumnName;
    }

    public String getModelColumnName() {
        return modelColumnName;
    }

    public String getDescriptionColumnName() {
        return descriptionColumnName;
    }

    public String getOdometerColumnName() {
        return odometerColumnName;
    }

    public String getPriceColumnName() {
        return priceColumnName;
    }

    /**
     * Gets the inventoryItem array of car data.
     * @return array of car inventory data.
     */
    public InventoryItem[] getCarInventory() {

        // we only want to sortColumnName if the column or ordering has changed.
        if (!oldSort.equals(sortColumnName) ||
                oldAscending != ascending){
             sort();
             oldSort = sortColumnName;
             oldAscending = ascending;
        }
        return carInventory;
    }

    /**
     * Determines the sortColumnName order.
     *
     * @param   sortColumn to sortColumnName by.
     * @return  whether sortColumnName order is ascending or descending.
     */
    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    /**
     *  Sorts the list of car data.
     */
    protected void sort() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                InventoryItem c1 = (InventoryItem) o1;
                InventoryItem c2 = (InventoryItem) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(stockColumnName)) {
                    return ascending ?
                            new Integer(c1.getStock()).compareTo(new Integer(c2.getStock())) :
                            new Integer(c2.getStock()).compareTo(new Integer(c1.getStock()));
                } else if (sortColumnName.equals(modelColumnName)) {
                    return ascending ? c1.getModel().compareTo(c2.getModel()) :
                            c2.getModel().compareTo(c1.getModel());
                } else if (sortColumnName.equals(descriptionColumnName)) {
                    return ascending ? c1.getDescription().compareTo(c2.getDescription()) :
                            c2.getDescription().compareTo(c1.getDescription());
                } else if (sortColumnName.equals(odometerColumnName)) {
                    return ascending ?
                            new Integer(c1.getOdometer()).compareTo(new Integer(c2.getOdometer())) :
                            new Integer(c2.getOdometer()).compareTo(new Integer(c1.getOdometer()));
                } else if (sortColumnName.equals(priceColumnName)) {
                    return ascending ?
                            new Integer(c1.getPrice()).compareTo(new Integer(c2.getPrice())) :
                            new Integer(c2.getPrice()).compareTo(new Integer(c1.getPrice()));
                } else return 0;
            }
        };
        Arrays.sort(carInventory, comparator);
    }

    /**
     * Inventory Item subclass stores data about a cars inventory data.  Properties
     * such a stock, model, description, odometer and price are stored.
     */
    public class InventoryItem {
        // slock number
        int stock;
        // model or type of inventory
        String model;
        // description of item
        String description;
        // number of miles on odometer
        int odometer;
        // price of car in Canadian dollars
        int price;

        /**
         * Creates a new instance of InventoryItem.
         * @param stock stock number.
         * @param model model or type of inventory.
         * @param description description of item.
         * @param odometer number of miles on odometer.
         * @param price price of care in Canadian dollars.
         */
        public InventoryItem(int stock, String model, String description, int odometer, int price) {
            this.stock = stock;
            this.model = model;
            this.description = description;
            this.odometer = odometer;
            this.price = price;
        }

        /**
         * Gets the stock number of this iventory item.
         * @return stock number.
         */
        public int getStock() {
            return stock;
        }

        /**
         * Gets the model number of this iventory item.
         * @return model number.
         */
        public String getModel() {
            return model;
        }

        /**
         * Gets the description of the this iventory item.
         * @return description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the odometer reading from this iventory item.
         * @return  odometer reading.
         */
        public int getOdometer() {
            return odometer;
        }

        /**
         * Gets the price of this item in Canadian Dollars.
         * @return price.
         */
        public int getPrice() {
            return price;
        }

    }

}
