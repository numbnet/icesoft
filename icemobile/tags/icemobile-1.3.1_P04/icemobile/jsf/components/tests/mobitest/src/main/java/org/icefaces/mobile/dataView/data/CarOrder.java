package org.icefaces.dataView.data;


import java.io.Serializable;
import java.util.Date;

public class CarOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String model;
    protected double cost;
    protected boolean sold;
    protected Date dateListed;
    protected String imageURL;

    public CarOrder() {}

    public CarOrder(int id, String model, double cost, boolean sold, Date dateListed, String imageURL) {
        this.id = id;
        this.model = model;
        this.cost = cost;
        this.sold = sold;
        this.dateListed = dateListed;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public Date getDateListed() {
        return dateListed;
    }

    public void setDateListed(Date dateListed) {
        this.dateListed = dateListed;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
